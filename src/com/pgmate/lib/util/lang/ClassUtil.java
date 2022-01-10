package com.pgmate.lib.util.lang;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Administrator
 *
 */
public class ClassUtil {

	private static Logger logger = LoggerFactory.getLogger( com.pgmate.lib.util.lang.ClassUtil.class );
	
	public static String getClassName(Object obj){
		return CommonUtil.nToB(obj.getClass().getName());
	}
	
	public static Method[] getClassMethod(Object obj){
		return obj.getClass().getDeclaredMethods();
	}
	
	public static Field[] getFields(Object obj){
		return obj.getClass().getDeclaredFields();
	} 
	
	public static List<String> getFieldInspect(Object obj){
		List<String> list = new ArrayList<String>();
		Field[] fields = getFields(obj);
		
		for(Field field : fields){
			list.add(field.getName());
		}
		return list;
	}
	
	public static List<String> getFieldDetailInspect(Object obj){
		List<String> list = new ArrayList<String>();
		Field[] fields = getFields(obj);
		
		for(Field field : fields){
			list.add(Modifier.toString(field.getModifiers())+" "+field.getType().getSimpleName() +" " +field.getName());
		}
		return list;
	}
	public static Object getObject(String className){
		Object obj = null;
		try{
			Class<?> cls = Class.forName(className.trim());
			obj = cls.newInstance();
		}catch(Exception e){
			logger.debug("create instance error : {}",CommonUtil.getExceptionMessage(e));
		}finally{
			if(obj == null){
				logger.debug("create instance error , class : {}",className);
			}
		}
		
		return obj;
	}
	
	public static Object getFieldValue(Object classInstance,String fieldName)throws Exception{
		Field field = classInstance.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		return field.get(classInstance);
	}
	public static String getFieldString(Object classInstance,String fieldName){
		String value = "";
		try{
			value = CommonUtil.toString(getFieldValue(classInstance,fieldName)); 
		}catch(Exception e){}
		return value;
	}
	
	public static void setFieldValue(Object classInstance,String fieldName,Object newValue)throws Exception{
		Field field = classInstance.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(classInstance,newValue);
		
	}
	

	
	
}
