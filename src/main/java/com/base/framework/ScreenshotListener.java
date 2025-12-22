package com.base.framework;

import java.io.IOException;

import org.testng.ITestListener;
import org.testng.ITestResult;

public class ScreenshotListener implements ITestListener{

	@Override
	public void onTestFailure(ITestResult result) {
		System.out.println(result.getName()+": Test Failed");
		try {
			TakeScreenshot.capture(DriverManager.getDriver(), result.getName()+"_Failed");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onTestStart(ITestResult result) {
		System.out.println(result.getName()+": Test Started");
	}
}
