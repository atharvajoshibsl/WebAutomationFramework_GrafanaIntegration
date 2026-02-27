package com.base.framework;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

@Listeners({
    com.base.framework.ExtentReportListener_New.class,
    com.base.framework.ScreenshotListener.class,
})
public class BaseTest {
	
	@BeforeMethod
	public void setUpMethod() throws Exception {
        String browser = ConfigReader.get("browser");  // chrome / edge / firefox
        String url = ConfigReader.get("url");

        DriverManager.initDriver(browser);
        System.out.println("Launching browser: "+ browser);
        DriverManager.getDriver().manage().window().maximize();
        DriverManager.getDriver().get(url);
	}
	
	@AfterMethod
	public void tearDownMethod() throws Exception {
		DriverManager.quitDriver();
	}
}
