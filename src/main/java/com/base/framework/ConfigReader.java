package com.base.framework;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

	private static Properties props= new Properties();
	
	static {
		try(InputStream input= ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
			
			if (input == null) {
				System.out.println("configuration properties missing");
			}else {
				props.load(input);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String get(String key)
	{
		return props.getProperty(key);
	}
}
