package com.pgmate.lib.util.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.converters.SqlTimestampConverter;
import org.apache.commons.beanutils.locale.converters.DateLocaleConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BeanUtil {

	private static Logger logger = LoggerFactory.getLogger( com.pgmate.lib.util.lang.BeanUtil.class );
	
	
	public static List<String> getValues(Object bean){
		List<String> list = null; 
		Map<String,String> map = getMap(bean);
		if(map != null){
			new ArrayList<String>(map.values());
		}
		return list;
	}
	
	public static List<String> getKeys(Object bean){
		List<String> list = null; 
		Map<String,String> map = getMap(bean);
		if(map != null){
			new ArrayList<String>(map.keySet());
		}
		return list;
	}
	
	public static Map<String,String> getMap(Object bean){
		Map<String,String> map = null;
		try{
			map = BeanUtils.describe(bean);
		}catch(Exception e){
			logger.debug(CommonUtil.getExceptionMessage(e));
		}
		return map;
	}
	
	public static String toString(Object bean){
		String str = "";
		try{
			str= BeanUtils.describe(bean).toString();
		}catch(Exception e){
			logger.debug(CommonUtil.getExceptionMessage(e));
		}
		
		
		/*
		
		StringBuilder sb = new StringBuilder();
		Map<String,String> map = toMap(bean);
		if(map.size() !=0){
			for (Entry<String, String> entry : map.entrySet()) {
				sb.append("[").append(entry.getKey()).append("=").append(entry.getValue()).append("] ");
	        }
		}*/
		return str;
	}
	
	/*
	public static Map<String,String> toMap(Object bean){
		
		
		Map<String,String> map = new HashMap<String,String>();
		try{
			BeanInfo bi = Introspector.getBeanInfo(bean.getClass());
			
			for (PropertyDescriptor pds : bi.getPropertyDescriptors()) {
	            Expression exp = new Expression(bean,pds.getReadMethod().getName(),new Object[0]);
	            
	            if(!pds.getReadMethod().getName().equals("getClass")){
	            	String fieldName = pds.getReadMethod().getName();
	            	fieldName = fieldName.substring(0,4).toLowerCase().replace("get","")+fieldName.substring(4);
	            	map.put(fieldName, CommonUtil.toString(exp.getValue()));
	            }
	        }
		}catch(Exception e){
			logger.debug(CommonUtil.getExceptionMessage(e));
		}
		return map;
		
	}
	*/
	public static void setValue(Object bean,String key,String value){
		
		try{
			BeanUtils.setProperty(bean, key, value);
		}catch(Exception e){
			logger.debug(CommonUtil.getExceptionMessage(e));
		}
		
		/*
		Map<String,String> map = new HashMap<String,String>();
		map.put(key, value);
		setStringValue(bean,map);*/
	}
	
	public static void setValue(Object bean,String key,Object value){
		try{
			BeanUtils.setProperty(bean, key, value);
		}catch(Exception e){
			logger.debug(CommonUtil.getExceptionMessage(e));
		}
		/*
		Map<String,Object> map = new HashMap<String,Object>();
		map.put(key, value);
		setObjectValue(bean,map);*/
	}
	
	public static void setObjectValue(Object bean,Map<String,Object> map){
		
		try{
			BeanUtils.populate(bean, map);
		}catch(Exception e){
			logger.debug(CommonUtil.getExceptionMessage(e));
		}
	}
	
	public static void setValue(Object bean,Map<String,String> map){
		
		try{
			BeanUtils.populate(bean, map);
		}catch(Exception e){
			logger.debug(CommonUtil.getExceptionMessage(e));
		}
		/*
		try{
			Statement stmt = null;
			if(map.size() !=0){
				for (Entry<String, String> entry : map.entrySet()) {
					String setter = "set"+entry.getKey().substring(0,1).toUpperCase()+entry.getKey().substring(1);
					
					Type type = bean.getClass().getDeclaredField(entry.getKey()).getType();
					Object valueObj = null;
					if(type == int.class){
						valueObj = CommonUtil.parseInt(entry.getValue());
					}else if(type == long.class){
						valueObj = CommonUtil.parseLong(entry.getValue());
					}else if(type == double.class){
						valueObj = CommonUtil.parseDouble(entry.getValue());
					}else if(type == boolean.class){
						valueObj = Boolean.parseBoolean(entry.getValue());
					}else if(type == String.class){
						valueObj = entry.getValue();
					}else{
						logger.debug("set error {} , type : {} was not defined ",setter,type);
					}
					
					stmt = new Statement(bean,setter,new Object[]{valueObj});
					stmt.execute();
		        }
			}
		}catch(Exception e){
			logger.debug(CommonUtil.getExceptionMessage(e));
		}*/
	}
	/*
	public static void setObjectValue(Object bean,Map<String,Object> map){
		try{
			Statement stmt = null;
			if(map.size() !=0){
				for (Entry<String, Object> entry : map.entrySet()) {
					String setter = "set"+entry.getKey().substring(0,1).toUpperCase()+entry.getKey().substring(1);
					stmt = new Statement(bean,setter,new Object[]{entry.getValue()});
					stmt.execute();
		        }
			}
		}catch(Exception e){
			logger.debug(CommonUtil.getExceptionMessage(e));
		}
	}*/
	
	public static void setTimestampFormat(Locale locale,String pattern){

		SqlTimestampConverter converter =new SqlTimestampConverter();
		converter.setPattern(pattern);
		
	    ConvertUtils.register(converter, java.sql.Timestamp.class);
	}
	
	
	
	
	
	
	
}
