package com.base.framework;

import java.io.File;
import java.io.IOException;
import java.time.Instant;

import org.openqa.selenium.*;

import org.openqa.selenium.io.FileHandler;

public class TakeScreenshot {

    public static String capture(WebDriver driver, String name) throws IOException {

        String build = System.getenv("BUILD_NUMBER");
        if (build == null) build = "LOCAL";

        File folder = new File(
                System.getProperty("user.dir")
                + "/Reports/build_" + build + "/latest/screenshots"
        );

        folder.mkdirs();

        String ts = Instant.now().toString().replaceAll("[:.T-]", "_");
        String file = name + "_" + ts + ".png";

        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File dest = new File(folder, file);

        FileHandler.copy(src, dest);

        return "screenshots/" + file;
    }
}
