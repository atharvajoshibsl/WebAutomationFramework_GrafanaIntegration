package com.base.framework;

import java.io.IOException;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

/**
 * ExtentReportListener
 * - Creates Extent test logs
 * - Attaches screenshots on failure
 * - Inserts PASS / FAIL / SKIP results into MySQL
 * - CI/Jenkins safe (NO localhost / NO python server)
 */
public class ExtentReportListener_New implements ITestListener {

    // Extent is initialized ONCE per run
    private static final ExtentReports extent = ExtentManager.getExtentReports();

    @Override
    public void onStart(ITestContext context) {
        System.out.println("Suite Started: " + context.getName());
        System.out.println("RUN_ID: " + ExtentManager.getRunId());
        ExtentTest suite = extent.createTest(context.getSuite().getName());
        ExtentManager.setSuiteTest(suite);       
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("Suite Finished: " + context.getName());
        extent.flush();
    }

//    @Override
//    public void onTestStart(ITestResult result) {
//
//        String testName = result.getMethod().getMethodName();
//        System.out.println(testName + ": Test Started");
//
//        ExtentTest test = extent.createTest(testName);
//        ExtentManager.setTest(test);
//    }
    
    @Override
    public void onTestStart(ITestResult result) {

        String className = result.getTestClass().getRealClass().getSimpleName();
        String methodName = result.getMethod().getMethodName();

//        ExtentTest suiteNode = ExtentManager.getSuiteTest();

        ExtentTest classNode = ExtentManager.getOrCreateClassNode(className);
        ExtentManager.setClassTest(classNode);

        ExtentTest methodNode = classNode.createNode(methodName);
        ExtentManager.setTest(methodNode);
    }
    

    @Override
    public void onTestSuccess(ITestResult result) {

        String testName = result.getMethod().getMethodName();
        System.out.println(testName + ": Test Passed");

        ExtentManager.getTest().pass("Test passed");

        insertResult(result, "PASS", null, null);
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

            ExtentManager.attachScreenshot(
                    screenshotPath,
                    testName + " - Failure Screenshot"
            );

        } catch (IOException e) {
            System.err.println("Screenshot capture failed: " + e.getMessage());
        }

        String errorMessage = result.getThrowable() != null
                ? result.getThrowable().toString()
                : null;

        insertResult(result, "FAIL", screenshotPath, errorMessage);
    }

    @Override
    public void onTestSkipped(ITestResult result) {

        String testName = result.getMethod().getMethodName();
        System.out.println(testName + ": Test Skipped");

        ExtentManager.getTest().skip("Test skipped");

        insertResult(result, "SKIP", null, null);
    }

    // ----------------- COMMON DB INSERT -----------------

    private void insertResult(ITestResult result,
                              String status,
                              String screenshotPath,
                              String errorMessage) {

        long duration = getDuration(result);
        String browser = ConfigReader.get("browser");
        String executedBy = System.getProperty("user.name");

        DbManager.insertTestResult(
                ExtentManager.getRunId(),
                result.getMethod().getMethodName(),
                status,
                browser,
                duration,
                screenshotPath,
                errorMessage,
                executedBy,
                ExtentManager.getReportPath()
        );
    }

    private long getDuration(ITestResult result) {
        long start = result.getStartMillis();
        long end = result.getEndMillis();
        return (end > start) ? end - start : 0L;
    }
}
