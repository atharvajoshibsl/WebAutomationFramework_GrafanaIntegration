package com.base.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.base.framework.BaseTest;
import com.base.framework.DriverManager;
import com.base.framework.RetryAnalyzer;

public class LoginTest extends BaseTest{
	
	@Test(retryAnalyzer = RetryAnalyzer.class)
	public void logintest() throws InterruptedException
	{
		WebDriver driver= DriverManager.getDriver();
		
		Thread.sleep(2000);
		String username = driver.findElement(By.xpath("//p[contains(.,'Username')]")).getText().split(":")[1].trim();
        String password = driver.findElement(By.xpath("//p[contains(.,'Password')]")).getText().split(":")[1].trim();

        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.xpath("//button[contains(.,'Logni')]")).click();

        Thread.sleep(1000);
        Assert.assertTrue(driver.getTitle().contains("OrangeHRM"), "Login failed!");		
	}
}
