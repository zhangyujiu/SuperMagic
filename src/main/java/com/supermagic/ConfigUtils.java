package com.supermagic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigUtils {
	Map<String,String> config = new HashMap<String,String>();
	public static ConfigUtils instance;
	
	public static ConfigUtils getInstance() {
		if (instance == null) {
			instance = new ConfigUtils();
		}
		return instance;
	}
	
	public ConfigUtils() {
		InputStream is3 = null;
	    try {
	    	is3 = new FileInputStream("superman.ini");
		    Properties prop = new Properties();
	        prop.load(is3);
	        //读取资源文件的所有的key值（列举）
	        Enumeration<?> en= prop.propertyNames();
	        System.out.println("读取superman开始...");
	        while(en.hasMoreElements()) {
	            String key = (String) en.nextElement();
	            System.out.println(key + "=" + prop.getProperty(key));
	            config.put(key, prop.getProperty(key));

	        }
	        System.out.println("读取superman结束...");
	    } catch (Exception e) {
	        System.out.println("无防关联配置文件,使用默认配置");
	    } finally {
	        if(null != is3) {
	            try {
	                is3.close();
	                System.out.println("关闭superman...");
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}
	
	public String getConfig(String key,String defaultValue) {
		String value = config.get(key);
		if (value != null) {
			return value;
		} else {
			return defaultValue;
		}
	}

}
