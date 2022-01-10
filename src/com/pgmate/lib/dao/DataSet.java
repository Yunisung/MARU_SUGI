package com.pgmate.lib.dao;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

/**
 * @author Administrator
 *
 */
public class DataSet implements Serializable {

	private List<SharedMap<String,Object>> rows	= null;
	private int idx				= -1;
	public String[] columns 	= null;
	
	public DataSet() {
		this.rows = new ArrayList<SharedMap<String,Object>>();
	}
	
	
	public int size(){
		if(rows == null){
			return 0;
		}
		return rows.size();
	}
	
	public boolean next(){
		if(rows == null || rows.size() <= (idx + 1)){
			return false;
		}
		idx = idx + 1;
		return true;
	}
	
	public void move(int idx) {
		this.idx = idx;
	}
	
	public int getIdx() {
		return idx;
	}

	public int addRow() {
		rows.add(new SharedMap<String,Object>());
		idx++;
		return idx;
	}
	
	public int addRow(SharedMap<String,Object> row) {
		rows.add(row);
		idx++;
		return idx;
	}
	
	public boolean prev() {
		idx = idx - 1;
		if(idx < 0) {
			idx = 0;
			return false;
		} else {
			return true;
		}
	}

	public boolean first() {
		idx = -1;
		return true;
	}
	
	public void put(String name, int i) {
		this.put(name, CommonUtil.toString(i));
	}

	public void put(String name, Object value) {
		if(value == null){
			value = "";
		}
		SharedMap<String,Object> row = (SharedMap<String, Object>)rows.get(idx);
		if(value instanceof String) {
			row.put(name.trim(), value);	
		} else {
			row.put(name.trim(), ""+value);
		}
		
	}
	
	public List<SharedMap<String,Object>> getRows(){
		return rows;
	}
	
	public SharedMap<String,Object> getRow(){
		if(idx > -1) {
			return (SharedMap<String,Object>)rows.get(idx);
		} else {
			return null;
		}
	}
	
	public SharedMap<String,Object> getRow(int idxx){
		if(rows.size() > idxx) {
			return (SharedMap<String,Object>)rows.get(idxx);
		} else {
			return null;
		}
	}
	
	public SharedMap<String,Object> getRowFirst(){
		if(rows.size() > 0) {
			return (SharedMap<String,Object>)rows.get(0);
		} else {
			return new SharedMap<String,Object>();
		}
	}
	
	
	public Object getObject(String name) {
		if(rows == null) return null;
		Object value = null;
		try {
			SharedMap<String,Object> row = (SharedMap<String, Object>)rows.get(idx);
			value =row.get(name.trim());
		} catch(Exception e) {}

		return value;
	}
	
	public String getString(String name) {
		return CommonUtil.toString(getObject(name));
	}
	
	public int getInt(String name) {
		return CommonUtil.parseInt(getObject(name));
	}
	
	public long getLong(String name) {
		return CommonUtil.parseLong(getObject(name));
	}
	
	public double getDouble(String name) {
		return CommonUtil.parseDouble(getObject(name));
	}
	
	public Date getDate(String name) {
		return (Date)getObject(name);
	}
	
	public Timestamp getTimestamp(String name){
		if(getObject(name) instanceof java.sql.Timestamp){
			return (Timestamp)getObject(name);
		}else{
			return CommonUtil.stringToTimestamp(getString(name));
		}
	}
	
	public String[] getKeys() {
		 if(idx < 0){
			 return null;
		 }
		 SharedMap<String,Object> row = (SharedMap<String,Object>)rows.get(idx);
		 String[] keys = new String[row.size()];
		 int i=0;
		 for (Iterator<String> iterator = row.keySet().iterator(); iterator.hasNext();) {
			 keys[i] = (String) iterator.next();
             i++;
		 }
		 return keys;
	}
	
	
	public boolean isEquals(String name,Object value){
		if(rowContainKeys(name)){
			if(value instanceof java.lang.String){
				return getString(name).equals(CommonUtil.toString(value));
			}else if(value instanceof java.lang.Integer){
				return getInt(name) == CommonUtil.parseInt(value);
			}else if(value instanceof java.lang.Long){
				return getLong(name) == CommonUtil.parseLong(value);
			}else if(value instanceof java.lang.Double){
				return getDouble(name) == CommonUtil.parseDouble(value);
			}return false;
		}else{
			return false;
		}
	}
	
	public boolean rowContainKeys(String name){
		try {
			SharedMap<String,Object> row = (SharedMap<String, Object>)rows.get(idx);
			return row.containsKey(name);
		} catch(Exception e) {}
		return false;
	}
	
	public boolean containKeys(String name){
		return rows.contains(name);
	}
	
	public String[] getColumns() {
		 
		columns = getKeys();
		return columns;
	}
}

