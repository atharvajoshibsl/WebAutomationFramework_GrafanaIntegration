package com.base.framework;

import java.io.File;
import java.io.IOException;
import java.time.Instant;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;

public class TakeScreenshot {

	public static String capture(WebDriver driver, String fileName) throws IOException
	{
		File folder= new File("D:\\Selenium With Java\\Reports\\screenshots");
		if(!folder.exists())
		{
			folder.mkdir();
		}
		
		String timestamp = Instant.now().toString().replaceAll("[:.T-]", "_");

		
		String file =  fileName + "_" + timestamp + ".png";
//		String fullPath = folder + "/" + fileName + "_" + timestamp + ".png";
		File src= ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
		File dest=new File(folder, file);
		
		
		FileHandler.copy(src, dest);
		
		return "screenshots/"+ file;
	}
}
