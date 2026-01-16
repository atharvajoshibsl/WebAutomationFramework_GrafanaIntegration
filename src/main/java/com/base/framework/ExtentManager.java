package com.base.framework;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;

import com.aventstack.extentreports.*;

import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

    public static String RUN_ID;
    private static ExtentReports extent;
    private static ExtentTest suiteNode;

    private static final ThreadLocal<ExtentTest> testNode = new ThreadLocal<>();
    private static final ConcurrentHashMap<String, ExtentTest> classNodeMap = new ConcurrentHashMap<>();

    public synchronized static ExtentReports getExtentReports() {

        if (extent != null) return extent;

        RUN_ID = "Run_" + DateTimeFormatter
                .ofPattern("yyyyMMdd_HHmmss")
                .format(LocalDateTime.now());

        String build = System.getenv("BUILD_NUMBER");
        if (build == null) build = "LOCAL";

        String baseDir = System.getProperty("user.dir")
                + File.separator + "Reports"
                + File.separator + "build_" + build;

        String runDir = baseDir + File.separator + RUN_ID;
        String latestDir = baseDir + File.separator + "latest";

        new File(runDir + "/screenshots").mkdirs();
        new File(latestDir + "/screenshots").mkdirs();

        String runReport = runDir + "/ExtentReport.html";
        String latestReport = latestDir + "/ExtentReport.html";

        ExtentSparkReporter spark = new ExtentSparkReporter(runReport);
        spark.config().setReportName("Automation Test Results");
        spark.config().setDocumentTitle("Test Execution Report");

        extent = new ExtentReports();
        extent.attachReporter(spark);

        extent.setSystemInfo("Build", build);
        extent.setSystemInfo("RunId", RUN_ID);
        extent.setSystemInfo("User", System.getProperty("user.name"));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                org.apache.commons.io.FileUtils.copyFile(
                        new File(runReport),
                        new File(latestReport)
                );
            } catch (Exception ignored) {}
        }));

        return extent;
    }

    public static void createSuite(String name) {
        suiteNode = extent.createTest(name);
    }

    public static ExtentTest getOrCreateClassNode(String className) {
        return classNodeMap.computeIfAbsent(className,
                c -> suiteNode.createNode(c));
    }

    public static void setTest(ExtentTest test) {
        testNode.set(test);
    }

    public static ExtentTest getTest() {
        return testNode.get();
    }

    public static void attachScreenshot(String path, String title) {
        if (getTest() != null && path != null) {
            getTest().addScreenCaptureFromPath(path, title);
        }
    }

    public static String getReportPath() {
        String build = System.getenv("BUILD_NUMBER");
        if (build == null) build = "LOCAL";

        return "job/WebAutomation_Framework/"
                + build
                + "/artifact/Reports/build_"
                + build
                + "/latest/ExtentReport.html";
    }

    public static String getRunId() {
        return RUN_ID;
    }
}
