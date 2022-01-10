package com.pgmate.lib.util.db;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Enumeration;

import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;
import com.pgmate.sugi.util.SQLInjectionUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Administrator
 *
 */
public class DBUtil {

	private Logger logger = LoggerFactory.getLogger( getClass());
	
	public void setValues(PreparedStatement ps,Object[] args)throws SQLException{
		int parameterIndex = 1;
		
		if(args != null){
			for(int i=0;i<args.length;i++){
				Object arg = args[i];
				if (arg instanceof java.lang.String) {
					if (!CommonUtil.isNullOrSpace((String) arg)) {
						ps.setString(parameterIndex, SQLInjectionUtil.xssChange((String) arg));
					} else {  
						ps.setString(parameterIndex, (String)arg);
					}
				}else if(arg instanceof java.lang.Integer){
					ps.setInt(parameterIndex, (Integer)arg);
				}else if(arg instanceof java.lang.Long){
					ps.setLong(parameterIndex, (Long)arg);
				}else if(arg instanceof java.lang.Double){
					ps.setDouble(parameterIndex, (Double)arg);
				}else if(arg instanceof java.math.BigInteger){
					ps.setBigDecimal(parameterIndex, (BigDecimal)arg);
				}else if(arg instanceof java.sql.Timestamp){
					ps.setTimestamp(parameterIndex, (Timestamp)arg);
				}else if(arg instanceof java.util.Date){
					ps.setDate(parameterIndex, (Date)arg);
				}else if(arg instanceof java.lang.Float){
					ps.setFloat(parameterIndex, (Float)arg);
				}else if(arg instanceof java.sql.Time){
					ps.setTime(parameterIndex, (Time)arg);
				}else if(arg instanceof java.lang.Byte){
					ps.setByte(parameterIndex, (Byte)arg);
				}else if(arg instanceof java.lang.Byte[]){
					ps.setBytes(parameterIndex, (byte[])arg);
				}else{
					ps.setObject(parameterIndex,arg);
					logger.debug("PreparedStatement set error type{} was not supported ",arg.getClass());
				}
				parameterIndex++;
			}
			
			
		}
	}
	
	
	public void setValues(PreparedStatement ps,SharedMap<String,Object> record)throws SQLException{
		
		if(record != null){
			Enumeration<Object> elements = record.elements();
			for(int parameterIndex=1; elements.hasMoreElements(); parameterIndex++) {
				Object obj = elements.nextElement();
				if (obj instanceof java.lang.String) {
					if (!CommonUtil.isNullOrSpace((String) obj)) {
						ps.setString(parameterIndex, SQLInjectionUtil.xssChange((String) obj));
					} else {
						ps.setString(parameterIndex, (String)obj);
					}
					
				}else{
					ps.setObject(parameterIndex, obj);
				}
			}
		}
		
	}
	
	
	

	
}
