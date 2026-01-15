package com.base.framework;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

    public static String RUN_ID;

    private static ExtentReports extent;
    private static ExtentTest suiteNode;

    private static ThreadLocal<ExtentTest> testNode = new ThreadLocal<>();

    // Cache class nodes (IMPORTANT FIX)
    private static ConcurrentHashMap<String, ExtentTest> classNodeMap = new ConcurrentHashMap<>();

    public synchronized static ExtentReports getExtentReports() {

        if (extent == null) {

            RUN_ID = "Run_" + DateTimeFormatter
                    .ofPattern("yyyyMMdd_HHmmss")
                    .format(LocalDateTime.now());

            String reportDir = System.getProperty("user.dir")
                    + File.separator + "Reports"
                    + File.separator + RUN_ID;

            new File(reportDir).mkdirs();
            new File(reportDir + "/screenshots").mkdirs();

            String reportPath = reportDir + "/ExtentReport.html";

            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setReportName("Automation Test Results");
            spark.config().setDocumentTitle("Test Execution Report");

            extent = new ExtentReports();
            extent.attachReporter(spark);

            extent.setSystemInfo("Project", "Web Automation");
            extent.setSystemInfo("RunId", RUN_ID);
            extent.setSystemInfo("Executed By", System.getProperty("user.name"));
        }
        return extent;
    }

    // ---------- SUITE ----------
    public static void createSuite(String suiteName) {
        suiteNode = extent.createTest(suiteName);
    }

    public static ExtentTest getSuite() {
        return suiteNode;
    }

    // ---------- CLASS ----------
    public static synchronized ExtentTest getOrCreateClassNode(String className) {
        return classNodeMap.computeIfAbsent(className,
                name -> suiteNode.createNode(name));
    }

    // ---------- TEST ----------
    public static void setTest(ExtentTest test) {
        testNode.set(test);
    }

    public static ExtentTest getTest() {
        return testNode.get();
    }

    // ---------- SCREENSHOT ----------
    public static void attachScreenshot(String relativePath, String title) {
        if (getTest() != null && relativePath != null) {
            getTest().addScreenCaptureFromPath(relativePath, title);
        }
    }

    // ---------- REPORT LINK (JENKINS SAFE) ----------
    public static String getReportPath() {
        String build = System.getenv("BUILD_NUMBER");
        if (build == null) build = "LOCAL";

        return "job/WebAutomation_Framework/"
                + build
                + "/artifact/Reports/"
                + RUN_ID
                + "/ExtentReport.html";
    }

    public static String getRunId() {
        return RUN_ID;
    }
}
