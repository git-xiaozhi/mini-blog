package com.clo.sdk.android.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class KenoConfig {
	public KenoConfig(){}
	private static Properties props = new Properties(); 
	static{
		try {
			props.load(KenoConfig.class.getResourceAsStream("/assets/config.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static String getValue(String key){
		return props.getProperty(key);
	}

    public static void updateProperties(String key,String value) {    
            props.setProperty(key, value); 
    } 
}
