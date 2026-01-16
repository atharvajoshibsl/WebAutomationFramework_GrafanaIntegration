package com.base.framework;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

    private static ExtentReports extent;

    private static String BUILD_ID;
    private static String RUN_ID;

    private static String reportDir;
    private static String reportPath;

    private static ExtentTest suiteNode;
    private static ConcurrentHashMap<String, ExtentTest> classNodeMap = new ConcurrentHashMap<>();
    private static ThreadLocal<ExtentTest> testNode = new ThreadLocal<>();

    // -------------------------------------------------------------
    // 1) EXTENT REPORT INIT (CREATE FOLDER => BUILD_<JENKINS_BUILD>)
    // -------------------------------------------------------------
    public synchronized static ExtentReports getExtentReports() {

        if (extent == null) {

            // 1️⃣ Build number (Jenkins or LOCAL)
            BUILD_ID = System.getenv("BUILD_NUMBER");
            if (BUILD_ID == null) BUILD_ID = "LOCAL";

            // 2️⃣ Run ID (timestamp)
            RUN_ID = "Run_" + DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
                    .format(LocalDateTime.now());

            // 3️⃣ FINAL folder: Reports/build_<BUILD>/Run_<RUNID>
            reportDir = System.getProperty("user.dir")
                    + File.separator + "Reports"
                    + File.separator + "build_" + BUILD_ID
                    + File.separator + RUN_ID;

            new File(reportDir).mkdirs();
            new File(reportDir + "/screenshots").mkdirs();

            // 4️⃣ Full HTML report path
            reportPath = reportDir + "/ExtentReport.html";

            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setReportName("Automation Test Results");
            spark.config().setDocumentTitle("Test Execution Report");

            extent = new ExtentReports();
            extent.attachReporter(spark);

            extent.setSystemInfo("Project", "Web Automation");
            extent.setSystemInfo("Build", BUILD_ID);
            extent.setSystemInfo("Run", RUN_ID);
            extent.setSystemInfo("Executed By", System.getProperty("user.name"));
        }

        return extent;
    }

    // -------------------------------------------------------------
    // SUITE NODE
    // -------------------------------------------------------------
    public static void createSuiteNode(String suiteName) {
        suiteNode = extent.createTest(suiteName);
    }

    public static ExtentTest getSuiteNode() {
        return suiteNode;
    }

    // -------------------------------------------------------------
    // CLASS NODE
    // -------------------------------------------------------------
    public static synchronized ExtentTest getOrCreateClassNode(String className) {
        return classNodeMap.computeIfAbsent(
                className,
                n -> suiteNode.createNode(className)
        );
    }

    // -------------------------------------------------------------
    // TEST NODE
    // -------------------------------------------------------------
    public static void setTestNode(ExtentTest node) {
        testNode.set(node);
    }

    public static ExtentTest getTestNode() {
        return testNode.get();
    }

    // -------------------------------------------------------------
    // SCREENSHOTS
    // -------------------------------------------------------------
    public static void attachScreenshot(String relativePath, String title) {
        ExtentTest test = getTestNode();
        if (test != null && relativePath != null) {
            test.addScreenCaptureFromPath(relativePath, title);
        }
    }

    // -------------------------------------------------------------
    // FINAL REPORT PATH FOR DB / GRAFANA
    // -------------------------------------------------------------
    public static String getReportLink() {
        return "job/WebAutomation_Framework/" + BUILD_ID
                + "/artifact/Reports/build_" + BUILD_ID
                + "/" + RUN_ID + "/ExtentReport.html";
    }

    public static String getRunId() {
        return RUN_ID;
    }
}
