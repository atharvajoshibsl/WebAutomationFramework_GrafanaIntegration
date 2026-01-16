package com.base.framework;

import java.io.File;
import java.io.IOException;
import java.time.Instant;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;

public class TakeScreenshot {

    public static String capture(WebDriver driver, String fileName) throws IOException {

        if (driver == null) {
            throw new IllegalArgumentException("WebDriver is null. Cannot capture screenshot.");
        }

        // Jenkins build number or LOCAL
        String build = System.getenv("BUILD_NUMBER");
        if (build == null) {
            build = "LOCAL";
        }

        // FINAL screenshots folder (Jenkins-safe)
        File folder = new File(
                System.getProperty("user.dir")
                + File.separator + "Reports"
                + File.separator + "build_" + build
                + File.separator + ExtentManager.getRunId()
                + File.separator + "screenshots"
        );

        // Create directories safely
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // UTC timestamp (CI + Grafana friendly)
        String timestamp = Instant.now()
                .toString()
                .replaceAll("[:.T-]", "_");

        String screenshotName = fileName + "_" + timestamp + ".png";

        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File dest = new File(folder, screenshotName);

        FileHandler.copy(src, dest);

        // IMPORTANT: relative path from ExtentReport.html
        return "screenshots/" + screenshotName;
 
    }
}
