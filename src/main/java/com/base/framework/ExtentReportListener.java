package com.base.framework;

import java.io.IOException;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class ExtentReportListener implements ITestListener {

    private static ExtentReports extent = ExtentManager.getExtentReports();

    @Override
    public void onStart(ITestContext context) {
        // Runs once per <test> tag in testng.xml
        System.out.println("TestNG Suite started: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("TestNG Suite finished: " + context.getName());
        extent.flush();   // very important: writes report to file
    }

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println(result.getName() + ": Test Started");

        String testName = result.getMethod().getMethodName();
        ExtentTest test = extent.createTest(testName);
        ExtentManager.setTestNode(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println(result.getName() + ": Test Passed");
        ExtentManager.getTestNode().pass("Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println(result.getName() + ": Test Failed");

        ExtentTest test = ExtentManager.getTestNode();
        test.fail(result.getThrowable());   // log exception

        try {
            String screenshotPath = TakeScreenshot.capture(
                    DriverManager.getDriver(),
                    result.getName() + "_Failed"
            );
            test.addScreenCaptureFromPath(screenshotPath);
        } catch (IOException e) {
            e.printStackTrace();
            test.warning("Failed to attach screenshot due to exception: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println(result.getName() + ": Test Skipped");
        ExtentManager.getTestNode().skip("Test skipped");
    }
}
