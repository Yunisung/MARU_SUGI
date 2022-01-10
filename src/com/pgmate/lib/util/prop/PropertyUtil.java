package com.pgmate.lib.util.prop;

import java.io.File;
import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.PropertyResourceBundle;

import com.pgmate.lib.util.lang.CommonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Administrator
 *
 */
public class PropertyUtil {

	private Logger logger = LoggerFactory.getLogger( getClass() );
	private Map<String,String> propMap 	= null;
	
	
	public PropertyUtil(){
	}
	
	public PropertyUtil(String propertyFile){
		loadProperty(propertyFile);
	}
	
	private void loadProperty(String propertyFile){
		propMap = new HashMap<String,String>();	
		PropertyResourceBundle props = null;
		try {
			props = new PropertyResourceBundle(new FileInputStream(propertyFile));
		}catch (Exception e) {
			logger.debug("Property file load error : {}",propertyFile);
		}
		
		Enumeration<String> keyEnum = props.getKeys();
		while(keyEnum.hasMoreElements()){
			String key = (String)keyEnum.nextElement();
			propMap.put(key,props.getString(key.trim()).trim());
		 }
	}
	
	
	
	public static String getJavaProperty(String key){
		return CommonUtil.nToB(System.getProperty(key));
	}
	
	public static void setJavaProperty(String key,String value){
		System.setProperty(key, value);
	}
	
	
	public static Map<String,String> getJavaProperties(){
		Properties properties = System.getProperties();
		Map<String, String> map = new HashMap<String, String>();
		for (String key : properties.stringPropertyNames()) {
		    map.put(key, properties.getProperty(key));
		}
		return map;
	}
	
	
	public static String getCyrexConf(){
		
		
		String webDir = System.getProperty("webapp.root");
		if(!CommonUtil.isNullOrSpace(webDir)){
			System.setProperty("CP_CONF", webDir+"WEB-INF");
		}
		if(CommonUtil.isNullOrSpace(System.getProperty("CP_CONF"))){
			System.setProperty("CP_CONF",getJavaProperty("user.dir")+File.separator+"conf");
		}
		
		return System.getProperty("CP_CONF");
	}
	
	public static void setCyrexConf(String value){
		System.setProperty("CP_CONF", value);
	}
	
	public static Map<String,String> getSystemProperties(){
		return System.getenv();
	}
		
	
}
