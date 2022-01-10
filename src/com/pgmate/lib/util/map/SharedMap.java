package com.pgmate.lib.util.map;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.pgmate.lib.util.lang.CommonUtil;
import com.google.gson.GsonBuilder;

/**
 * @author Administrator
 *
 */
public class SharedMap<K, V> extends ConcurrentHashMap<K, V> {

	public SharedMap(){
	}
	
	public SharedMap(Map<K,V> map){
		if(map != null){
			super.putAll(map);
		}
	}
	
	public SharedMap(String json){
		super.putAll(new GsonBuilder().create().fromJson(json, ConcurrentHashMap.class));
	}
	
	
	
	public String toJson(){
		return new GsonBuilder().setPrettyPrinting().create().toJson(this);
	}
	
	public String getString(String key){
		return CommonUtil.toString(super.get(key));
	}
	
	
	public int getInt(String key) {
		return CommonUtil.parseInt(get(key));
	}
	
	public long getLong(String key) {
		return CommonUtil.parseLong(get(key));
	}
	
	public double getDouble(String key) {
		return CommonUtil.parseDouble(get(key));
	}
	
	public Date getDate(String key) {
		return (Date)get(key);
	}
	
	public Timestamp getTimestamp(String key){
		if(get(key) instanceof java.sql.Timestamp){
			return (Timestamp)get(key);
		}else{
			return CommonUtil.stringToTimestamp(getString(key));
		}
	}
	
	public boolean isTrue(String key){
		return getString(key).equals("true");
	}
	
	
	public boolean isNullOrSpace(String key){
		return CommonUtil.isNullOrSpace(getString(key));
	}
	
	
	
	public boolean startsWith(String key,String value){
		return getString(key).startsWith(value);
	}
	
	
	
	public boolean isEquals(String key,Object value){
		if(containsKey(key)){
			if(value instanceof java.lang.String){
				return getString(key).equals(CommonUtil.toString(value));
			}else if(value instanceof java.lang.Integer){
				return getInt(key) == CommonUtil.parseInt(value);
			}else if(value instanceof java.lang.Long){
				return getLong(key) == CommonUtil.parseLong(value);
			}else if(value instanceof java.lang.Double){
				return getDouble(key) == CommonUtil.parseDouble(value);
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	public boolean equalsIgnoreCase(String key,Object value){
		if(containsKey(key)){
			if(value instanceof java.lang.String){
				return getString(key).equalsIgnoreCase(CommonUtil.toString(value));
			}else if(value instanceof java.lang.Integer){
				return getInt(key) == CommonUtil.parseInt(value);
			}else if(value instanceof java.lang.Long){
				return getLong(key) == CommonUtil.parseLong(value);
			}else if(value instanceof java.lang.Double){
				return getDouble(key) == CommonUtil.parseDouble(value);
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for (Entry<K,V> entry : this.entrySet()) {
		    sb.append(CommonUtil.toString(entry.getKey()) +":" + CommonUtil.toString(entry.getValue()) + "\n");
		}
		return sb.toString();
	}
	
	
	public boolean like(String key,String value){
		if(getString(key).indexOf(value) > -1){
			return true;
		}else{
			return false;
		}
	}
	
	
	
	
	
	
	
	
}
