package com.base.framework;

import java.io.IOException;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

/**
 * Clean, simplified ExtentReportListener.
 * - Creates Extent test logs
 * - Attaches screenshots on failure
 * - Inserts PASS / FAIL / SKIP results into MySQL using DbManager
 */
public class ExtentReportListener_New implements ITestListener {

    private static final ExtentReports extent = ExtentManager.getExtentReports();
    String RUN_ID = ExtentManager.getRunId();
    
    String extentPath=ExtentManager.getReportPath();


    @Override
    public void onStart(ITestContext context) {
        System.out.println("Suite Started: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("Suite Finished: " + context.getName());
        extent.flush();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        System.out.println(testName + ": Test Started");

        ExtentTest test = extent.createTest(testName);
        ExtentManager.setTest(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        System.out.println(testName + ": Test Passed");

        ExtentManager.getTest().pass("Test passed");

        long duration = getDuration(result);
        String browser = ConfigReader.get("browser");
        String executedBy = System.getProperty("user.name");

        DbManager.insertTestResult(
                RUN_ID, testName, "PASS",
                browser, duration,
                null, null, executedBy, extentPath
        );
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        System.out.println(testName + ": Test Failed");

        ExtentTest test = ExtentManager.getTest();
        test.fail(result.getThrowable());

        String screenshotPath = null;

        try {
            screenshotPath = TakeScreenshot.capture(
                    DriverManager.getDriver(),
                    testName + "_Failed"
            );
            test.addScreenCaptureFromPath(screenshotPath);

        } catch (IOException e) {
            System.err.println("Screenshot capture failed: " + e.getMessage());
        }

        long duration = getDuration(result);
        String browser = ConfigReader.get("browser");
        String executedBy = System.getProperty("user.name");
        String errorMessage = (result.getThrowable() != null)
                ? result.getThrowable().toString()
                : null;

        DbManager.insertTestResult(
                RUN_ID, testName, "FAIL",
                browser, duration,
                screenshotPath, errorMessage, executedBy, extentPath
        );
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        System.out.println(testName + ": Test Skipped");

        ExtentManager.getTest().skip("Test skipped");

        long duration = getDuration(result);
        String browser = ConfigReader.get("browser");
        String executedBy = System.getProperty("user.name");

        DbManager.insertTestResult(
                RUN_ID, testName, "SKIP",
                browser, duration,
                null, null, executedBy, extentPath
        );
    }

    private long getDuration(ITestResult result) {
        long start = result.getStartMillis();
        long end = result.getEndMillis();
        return (end > start) ? end - start : 0L;
    }
}
