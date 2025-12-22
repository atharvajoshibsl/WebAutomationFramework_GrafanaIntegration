package com.base.framework;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

    private static ExtentReports extent;
    private static String reportPath;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    // Create and configure ExtentReports (only once)
    public synchronized static ExtentReports getExtentReports() {
        if (extent == null) {
            reportPath = "D:\\Selenium With Java\\Reports/ExtentReport.html";

            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setReportName("Automation Test Results");
            spark.config().setDocumentTitle("Test Execution Report");

            extent = new ExtentReports();
            extent.attachReporter(spark);

            extent.setSystemInfo("Project", "Web Automation");
            extent.setSystemInfo("Tester", "Atharva");
        }
        return extent;
    }
    
    public static String getReportPath()
    {
//    	proxy python http server
    	String extentUrl = "http://localhost:8081/ExtentReport.html";    	
    	return extentUrl;
    }
    
    public static void setTest(ExtentTest extentTest) {
        test.set(extentTest);
    }

    public static ExtentTest getTest() {
        return test.get();
    }
}
