package com.base.framework;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

    public static String RUN_ID;                 // <---- ADD THIS (public)
    private static String reportFolderPath;
    private static String reportFilePath;    
    
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    // Create and configure ExtentReports (only once)
    public synchronized static ExtentReports getExtentReports() {
        if (extent == null) {
        	
            // ----- CREATE PUBLIC RUN_ID -----
            RUN_ID = "Run_" + DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").format(LocalDateTime.now());

            // ----- CREATE REPORT FOLDER -----
            reportFolderPath = System.getProperty("user.dir")+ "/Reports/"+ RUN_ID;        	
            
            reportFilePath = reportFolderPath + "/ExtentReport.html";

            ExtentSparkReporter spark = new ExtentSparkReporter(reportFilePath);
            spark.config().setReportName("Automation Test Results");
            spark.config().setDocumentTitle("Test Execution Report");

            extent = new ExtentReports();
            extent.attachReporter(spark);

            extent.setSystemInfo("Project", "Web Automation");
            extent.setSystemInfo("Tester", "Atharva");
        }
        return extent;
    }
    
    public static String getRunId() {
        return RUN_ID;
    }
    
    public static String getReportPath()
    {
//    	proxy python http server
    	String extentUrl = "http://localhost:8081/Reports/"+ RUN_ID +"/ExtentReport.html";    	
    	return extentUrl;
    }
    
    public static void setTest(ExtentTest extentTest) {
        test.set(extentTest);
    }

    public static ExtentTest getTest() {
        return test.get();
    }
}
