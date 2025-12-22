package com.base.framework;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import io.github.bonigarcia.wdm.WebDriverManager;


public class DriverManager {
	
	public static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	
	 public static void initDriver(String browser) {

	        if (browser == null || browser.isEmpty()) {
	            browser = "chrome";
	        }

	        switch (browser.toLowerCase()) {
	        case "edge":
	            WebDriverManager.edgedriver().setup();
	            driver.set(new EdgeDriver());
	            break;

	        case "firefox":
	        case "ff":
	            WebDriverManager.firefoxdriver().setup();
	            driver.set(new FirefoxDriver());
	            break;

	        default:
	            WebDriverManager.chromedriver().setup();
	            driver.set(new ChromeDriver());
	            break;
	        }
	    }

	
	public static WebDriver getDriver()
	{
		return driver.get();
	}
	
	public static void quitDriver()
	{
		if(driver.get()!=null)
		{
			driver.get().quit();
			driver.remove();
		}
	}
}
