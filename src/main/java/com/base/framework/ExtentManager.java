package com.base.framework;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

    // -------- PUBLIC (used by DB / Jenkins / Grafana) --------
    public static String RUN_ID;

    // -------- EXTENT CORE --------
    private static ExtentReports extent;
    private static ExtentTest suiteNode;

    // -------- THREAD SAFE --------
    private static ThreadLocal<ExtentTest> testNode = new ThreadLocal<>();

    // -------- CLASS NODE CACHE (FIXES DUPLICATION) --------
    private static ConcurrentHashMap<String, ExtentTest> classNodeMap =
            new ConcurrentHashMap<>();

    // =======================================================
    // CREATE EXTENT (ONCE PER JVM)
    // =======================================================
    public synchronized static ExtentReports getExtentReports() {

        if (extent == null) {

            // 1️⃣ Jenkins build number (or LOCAL)
            String build = System.getenv("BUILD_NUMBER");
            if (build == null) {
                build = "LOCAL";
            }

            // 2️⃣ Run ID (human readable)
            RUN_ID = "Run_" + DateTimeFormatter
                    .ofPattern("yyyyMMdd_HHmmss")
                    .format(LocalDateTime.now());

            // 3️⃣ FINAL Jenkins-safe directory structure
            // Reports/build_<BUILD>/Run_<RUN_ID>/
            String reportDir =
                    System.getProperty("user.dir")
                            + File.separator + "Reports"
                            + File.separator + "build_" + build
                            + File.separator + RUN_ID;

            new File(reportDir + "/screenshots").mkdirs();

            // 4️⃣ Report file
            String reportPath = reportDir + "/ExtentReport.html";

            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setReportName("Automation Test Results");
            spark.config().setDocumentTitle("Test Execution Report");

            extent = new ExtentReports();
            extent.attachReporter(spark);

            extent.setSystemInfo("Project", "Web Automation");
            extent.setSystemInfo("Build", build);
            extent.setSystemInfo("RunId", RUN_ID);
            extent.setSystemInfo("Executed By", System.getProperty("user.name"));
        }

        return extent;
    }

    // =======================================================
    // SUITE NODE
    // =======================================================
    public static synchronized void createSuite(String suiteName) {
        if (suiteNode == null) {
            suiteNode = extent.createTest(suiteName);
        }
    }

    public static ExtentTest getSuite() {
        return suiteNode;
    }

    // =======================================================
    // CLASS NODE (CACHED)
    // =======================================================
    public static synchronized ExtentTest getOrCreateClassNode(String className) {
        return classNodeMap.computeIfAbsent(
                className,
                name -> suiteNode.createNode(name)
        );
    }

    // =======================================================
    // TEST NODE (THREAD LOCAL)
    // =======================================================
    public static void setTest(ExtentTest test) {
        testNode.set(test);
    }

    public static ExtentTest getTest() {
        return testNode.get();
    }

    // =======================================================
    // SCREENSHOT ATTACHMENT
    // =======================================================
    public static void attachScreenshot(String relativePath, String title) {
        if (getTest() != null && relativePath != null) {
            getTest().addScreenCaptureFromPath(relativePath, title);
        }
    }

    // =======================================================
    // JENKINS REPORT URL (USED BY DB / GRAFANA)
    // =======================================================
    public static String getReportPath() {

        String build = System.getenv("BUILD_NUMBER");
        if (build == null) {
            build = "LOCAL";
        }

        return "job/WebAutomation_Framework/"
                + build
                + "/artifact/Reports/build_"
                + build
                + "/"
                + RUN_ID
                + "/ExtentReport.html";
    }

    public static String getRunId() {
        return RUN_ID;
    }
}
