package com.pgmate.lib.util.gson;

import java.io.FileWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pgmate.lib.util.lang.CommonUtil;

/**
 * @author Administrator
 *
 */
public class GsonUtil {

	private static Logger logger = LoggerFactory.getLogger( com.pgmate.lib.util.gson.GsonUtil.class );
	private static String DEFAULT_DATE_PATTERN = "yyyy/MM/dd HH:mm:ss.S";
	
	public static String toJson(Object obj){
		return toJson(obj,false,"");
	}
	
	public static String toJsonExcludeStrategies(Object obj){
		
		return toJsonExcludeStrategies(obj,false);
	}
	
	public static String toJsonExcludeStrategies(Object obj,boolean pretty){
		if(obj == null){ return "";}
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setExclusionStrategies(new UserExclusionStrategy());
		if(pretty){
			gsonBuilder.setPrettyPrinting();
		}
		return gsonBuilder.create().toJson(obj);
	}
	
	
	public static String toJson(Object obj,boolean pretty,String datePattern){
		if(obj == null){ return "";}
		GsonBuilder gsonBuilder = new GsonBuilder();
		if(pretty){
			gsonBuilder.setPrettyPrinting();
		}
		if(!datePattern.equals("")){
			gsonBuilder.setDateFormat(datePattern);
		}else{
			gsonBuilder.setDateFormat(DEFAULT_DATE_PATTERN);
		}
		return gsonBuilder.create().toJson(obj);
	}
	
	
	public static boolean toJsonFileWrite(Object obj,boolean pretty,String datePattern,String file){
		boolean isCreated = false;
		String json = toJson(obj, pretty, datePattern);
		try{
			FileWriter writer = new FileWriter(file);
			writer.write(json);
			writer.close();
			isCreated = true;
		}catch(Exception e){
			logger.debug(CommonUtil.getExceptionMessage(e));
		}
		return isCreated;
		
	}
	
	public static String toPrettyFormat(String jsonString){
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(jsonString).getAsJsonObject();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = gson.toJson(json);

        return prettyJson;
    }
	
	
	public static Object fromJson(String json,Object obj){
		return fromJson(json, obj.getClass(), DEFAULT_DATE_PATTERN);
	}
	public static Object fromJson(String json,Object obj,String datePattern){
		return fromJson(json, obj.getClass(), datePattern);
	}
	
	public static Object fromJson(String json,Class<?> clazz){
		return fromJson(json, clazz, DEFAULT_DATE_PATTERN);
	}
	
	public static Object fromJson(String json,Class<?> clazz,String datePattern){
		Object obj = null;
		try{
			obj = new GsonBuilder().setDateFormat(datePattern).create().fromJson(json, clazz);
		}catch(Exception e){
			logger.debug(CommonUtil.getExceptionMessage(e));
		}
		return obj;
	}
	
	
}
