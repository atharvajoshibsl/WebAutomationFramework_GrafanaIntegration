package com.base.framework;

import java.io.IOException;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class ExtentReportListener_New implements ITestListener {

    private static final ExtentReports extent = ExtentManager.getExtentReports();

    @Override
    public void onStart(ITestContext context) {
        System.out.println("Suite Started: " + context.getSuite().getName());
        ExtentManager.createSuite(context.getSuite().getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }

    @Override
    public void onTestStart(ITestResult result) {

        String className =
                result.getTestClass().getRealClass().getSimpleName();
        String methodName =
                result.getMethod().getMethodName();

        ExtentTest classNode =
                ExtentManager.getOrCreateClassNode(className);

        ExtentTest methodNode =
                classNode.createNode(methodName);

        ExtentManager.setTest(methodNode);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentManager.getTest().pass("Test passed");
        insert(result, "PASS", null, null);
    }

    @Override
    public void onTestFailure(ITestResult result) {

        ExtentManager.getTest().fail(result.getThrowable());

        String screenshot = null;
        try {
            screenshot = TakeScreenshot.capture(
                    DriverManager.getDriver(),
                    result.getMethod().getMethodName());
            ExtentManager.attachScreenshot(
                    screenshot,
                    "Failure Screenshot");
        } catch (IOException e) {
            e.printStackTrace();
        }

        insert(result, "FAIL", screenshot,
                result.getThrowable().toString());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentManager.getTest().skip("Test skipped");
        insert(result, "SKIP", null, null);
    }

    private void insert(ITestResult result,
                        String status,
                        String screenshot,
                        String error) {

        DbManager.insertTestResult(
                ExtentManager.getRunId(),
                result.getMethod().getMethodName(),
                status,
                ConfigReader.get("browser"),
                result.getEndMillis() - result.getStartMillis(),
                screenshot,
                error,
                System.getProperty("user.name"),
                ExtentManager.getReportPath()
        );
    }
}
