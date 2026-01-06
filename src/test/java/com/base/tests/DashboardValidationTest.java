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

public class DashboardValidationTest extends BaseTest {

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void dashboardLoadedTest() throws Exception {

        WebDriver driver = DriverManager.getDriver();

        By usernameText = By.xpath("//p[contains(.,'Username')]");
        By passwordText = By.xpath("//p[contains(.,'Password')]");
        By usernameInput = By.name("username");
        By passwordInput = By.name("password");
        By loginBtn = By.xpath("//button[contains(.,'Login')]");
        By dashboardHeader = By.xpath("//h6[text()='Dashboard']");

        // 1️⃣ Wait for login page to load
        WaitUtils.waitForVisible(driver, usernameText);

        String username = driver.findElement(usernameText)
                .getText().split(":")[1].trim();
        String password = driver.findElement(passwordText)
                .getText().split(":")[1].trim();

        // 2️⃣ Enter credentials
        driver.findElement(usernameInput).sendKeys(username);
        driver.findElement(passwordInput).sendKeys(password);

        String ss1 = TakeScreenshot.capture(driver, "credentials_entered");
        ExtentManager.attachScreenshot(ss1, "Credentials entered");

        // 3️⃣ Click login
        driver.findElement(loginBtn).click();

        // 4️⃣ Wait for dashboard
        WaitUtils.waitForVisible(driver, dashboardHeader);

        String ss2 = TakeScreenshot.capture(driver, "dashboard_loaded");
        ExtentManager.attachScreenshot(ss2, "Dashboard page loaded");

        // 5️⃣ Assertion
        Assert.assertTrue(
                driver.findElement(dashboardHeader).isDisplayed(),
                "Dashboard not displayed"
        );
    }
}
