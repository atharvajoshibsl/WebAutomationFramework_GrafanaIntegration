package com.base.framework;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

    // Public so DB / Screenshot / Grafana can reuse it
    public static String RUN_ID;

    private static String reportFolderPath;
    private static String reportFilePath;

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static ThreadLocal<ExtentTest> suiteTest = new ThreadLocal<>();
    private static ThreadLocal<ExtentTest> classTest = new ThreadLocal<>();


    // Create and configure ExtentReports (only once per run)
    public synchronized static ExtentReports getExtentReports() {

        if (extent == null) {

            // 1️⃣ Generate RUN_ID
            RUN_ID = "Run_" + DateTimeFormatter
                    .ofPattern("yyyyMMdd_HHmmss")
                    .format(LocalDateTime.now());
            

            // 2️⃣ Create report folder inside workspace
            reportFolderPath = System.getProperty("user.dir")+ "/Reports/" + RUN_ID;

            // Create folders
            new File(reportFolderPath).mkdirs();
            new File(reportFolderPath + File.separator + "screenshots").mkdirs();

            // 3️⃣ Report file path
            reportFilePath = reportFolderPath + "/ExtentReport.html";

            // 4️⃣ Configure Extent
            ExtentSparkReporter spark = new ExtentSparkReporter(reportFilePath);
            spark.config().setReportName("Automation Test Results");
            spark.config().setDocumentTitle("Test Execution Report");

            extent = new ExtentReports();
            extent.attachReporter(spark);

            extent.setSystemInfo("Project", "Web Automation");
            extent.setSystemInfo("Tester", System.getProperty("user.name"));
            extent.setSystemInfo("RunId", RUN_ID);
        }

        return extent;
    }

    // Attach screenshot to current test
    public static void attachScreenshot(String relativePath, String title) {
        if (getTest() != null && relativePath != null) {
            getTest().addScreenCaptureFromPath(relativePath, title);
        }
    }

    // Used by DB & Grafana (artifact-friendly path)
    public static String getReportPath() {
    	String buildNo= getBuildNumber();
        return "job/WebAutomation_Framework/"+ buildNo + "/artifact/Reports/" + RUN_ID + "/ExtentReport.html";
    }

    public static String getRunId() {
        return RUN_ID;
    }

    public static String getBuildNumber() {
        String build = System.getenv("BUILD_NUMBER");
        return (build != null) ? build : "LOCAL";
    }    
    
    public static void setTest(ExtentTest extentTest) {
        test.set(extentTest);
    }

    public static ExtentTest getTest() {
        return test.get();
    }
    
 // Getters and setter method for Suite creation for Suite Report
    public static void setSuiteTest(ExtentTest test) {
        suiteTest.set(test);
    }

    public static ExtentTest getSuiteTest() {
        return suiteTest.get();
    }

    public static void setClassTest(ExtentTest test) {
        classTest.set(test);
    }

    public static ExtentTest getClassTest() {
        return classTest.get();
    }
    
}
