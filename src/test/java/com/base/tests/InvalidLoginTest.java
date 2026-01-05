package com.base.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.base.framework.BaseTest;
import com.base.framework.DriverManager;
import com.base.framework.ExtentManager;
import com.base.framework.RetryAnalyzer;
import com.base.framework.TakeScreenshot;
import com.base.framework.WaitUtils;

public class InvalidLoginTest extends BaseTest {

	@Test(retryAnalyzer = RetryAnalyzer.class)
    public void invalidLoginTest() throws Exception {

        WebDriver driver = DriverManager.getDriver();

        By usernameInput = By.name("username");
        By passwordInput = By.name("password");
        By loginBtn = By.xpath("//button[contains(.,'Login')]");
        By errorMsg = By.xpath("//p[contains(@class,'oxd-alert-content-text')]");
        
        WaitUtils.waitForVisible(driver, usernameInput);        

        driver.findElement(usernameInput).sendKeys("wrongUser");
        driver.findElement(passwordInput).sendKeys("wrongPass");

        String ss1 = TakeScreenshot.capture(driver, "invalid_credentials_entered");
        ExtentManager.attachScreenshot(ss1, "Credentials entered");        
        
        WaitUtils.waitForClickable(driver, loginBtn);        
        driver.findElement(loginBtn).click();

        WaitUtils.waitForVisible(driver, errorMsg);
        String ss2 = TakeScreenshot.capture(driver, "invalid_login_error");
        ExtentManager.attachScreenshot(ss2, "Login Error Captured");

        Assert.assertTrue(
                driver.findElement(errorMsg).getText().contains("Invalid"),
                "Invalid login message not shown");
    }
}
