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

        // screenshots folder under current RUN_ID
        File folder = new File(
                System.getProperty("user.dir")
                + "/Reports/"
                + ExtentManager.getRunId()
                + "/screenshots"
        );

        // create directories if not present
        if (!folder.exists()) {
            folder.mkdirs();   // mkdirs is safer than mkdir
        }

        // UTC timestamp (Grafana & CI friendly)
        String timestamp = Instant.now()
                .toString()
                .replaceAll("[:.T-]", "_");

        String fileNameWithTime = fileName + "_" + timestamp + ".png";

        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File dest = new File(folder, fileNameWithTime);

        FileHandler.copy(src, dest);

        // return RELATIVE path (important for Extent + Jenkins)
        return "screenshots/" + fileNameWithTime;
    }
}
