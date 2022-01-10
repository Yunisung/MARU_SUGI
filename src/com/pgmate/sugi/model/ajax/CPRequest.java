package com.pgmate.sugi.model.ajax;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.pgmate.lib.util.lang.CommonUtil;

/**
 * @author Administrator
 *
 */
@XmlRootElement(name = "request")
public class CPRequest implements java.io.Serializable{
	
	//insert,update,delete,submit,pdf,excel,...
	@XmlElement(name = "type")
	public String type		= "";
	
	@XmlElement(name = "reason")
	public String reason	= "";
	
	@XmlElement(name = "redirect")
	public String redirect	= "";
	
	@XmlElement(name = "page")
	public Page page		= null;
	
	@XmlElement(name = "data")
	public List<Data> data	= null;
	
	@XmlElement(name = "thead")
	public String thead	= null;
	
	public CPRequest() {
		// TODO Auto-generated constructor stub
	}
	
	public CPRequest(String type){
		this.type = type; 
	}
	
	public void replaceKeyName(String name,String replace){
		if(data == null){return;}
		else{
			for(int i=0;i<data.size();i++){
				Data dataObj = (Data)data.get(i);
				if(dataObj.name.equals(name) && dataObj.key == true){
					dataObj.name = replace;
					data.remove(i);
					data.add(i, dataObj);
				}
			}
		}
	}
	
	public void replaceName(String name,String replace){
		if(data == null){return;}
		else{
			for(int i=0;i<data.size();i++){
				Data dataObj = (Data)data.get(i);
				if(dataObj.name.equals(name) && dataObj.key == false){
					dataObj.name = replace;
					data.remove(i);
					data.add(i, dataObj);
				}
			}
		}
	}
	
	public void replaceKeyValue(String name,String replace){
		if(data == null){return;}
		else{
			for(int i=0;i<data.size();i++){
				Data dataObj = (Data)data.get(i);
				if(dataObj.name.equals(name) && dataObj.key == true){
					dataObj.val = replace;
					data.remove(i);
					data.add(i, dataObj);
				}
			}
		}
	}
	
	
	public void replaceValue(String name,String replace){
		if(data == null){return;}
		else{
			for(int i=0;i<data.size();i++){
				Data dataObj = (Data)data.get(i);
				if(dataObj.name.equals(name) && dataObj.key == false){
					dataObj.val = replace;
					data.remove(i);
					data.add(i, dataObj);
				}
			}
		}
	}
	
	
	public void replaceKey(String name,boolean key){
		if(data == null){return;}
		else{
			for(int i=0;i<data.size();i++){
				Data dataObj = (Data)data.get(i);
				if(dataObj.name.equals(name)){
					dataObj.key = key;
					data.remove(i);
					data.add(i, dataObj);
				}
			}
		}
	}
	
	
	public String getValue(String name){
		if(data == null){return "";}
		else{
			String value = "";
			for(int i=0;i<data.size();i++){
				Data dataObj = (Data)data.get(i);
				if(dataObj.name.equals(name) && dataObj.key == false){
					value = CommonUtil.toString(dataObj.val);
					break;
				}
			}
			return value;
		}
	}
	
	public String getValue(String name,String replaceValue){
		if(data == null){return "";}
		else{
			String value = "";
			for(int i=0;i<data.size();i++){
				Data dataObj = (Data)data.get(i);
				if(dataObj.name.equals(name) && dataObj.key == false){
					value = CommonUtil.toString(dataObj.val);
					if(value.equals("")){value=replaceValue;}
					break;
				} 
			}
			return value;
		}
	}
	
	public long getLongValue(String name){
		if(data == null){return 0;}
		else{
			long value = 0;
			for(int i=0;i<data.size();i++){
				Data dataObj = (Data)data.get(i);
				if(dataObj.name.equals(name) && dataObj.key == false){
					value = CommonUtil.parseLong(dataObj.val);
					break;
				}
			}
			return value;
		}
	}
	
	public double getDoubleValue(String name){
		if(data == null){return 0;}
		else{
			double value = 0;
			for(int i=0;i<data.size();i++){
				Data dataObj = (Data)data.get(i);
				if(dataObj.name.equals(name) && dataObj.key == false){
					value = CommonUtil.parseDouble(dataObj.val);
					break;
				}
			}
			return value;
		}
	}
	
	
	public String getKeyValue(String name){
		if(data == null){return "";}
		else{
			String value = "";
			for(int i=0;i<data.size();i++){
				Data dataObj = (Data)data.get(i);
				if(dataObj.name.equals(name) && dataObj.key == true){
					value = CommonUtil.toString(dataObj.val);
					break;
				}
			}
			return value;
		}
	}
	
	public long getKeyLongValue(String name){
		if(data == null){return 0;}
		else{
			long value = 0;
			for(int i=0;i<data.size();i++){
				Data dataObj = (Data)data.get(i);
				if(dataObj.name.equals(name) && dataObj.key == true){
					value = CommonUtil.parseLong(dataObj.val);
					break;
				}
			}
			return value;
		}
	}
	
	public double getKeyDoubleValue(String name){
		if(data == null){return 0;}
		else{
			double value = 0;
			for(int i=0;i<data.size();i++){
				Data dataObj = (Data)data.get(i);
				if(dataObj.name.equals(name) && dataObj.key == true){
					value = CommonUtil.parseDouble(dataObj.val);
					break;
				}
			}
			return value;
		}
	}
	
	public Data getKeyData(String name){
		
		if(data == null){return null;}
		else{
			Data dataValue = null;
			for(int i=0;i<data.size();i++){
				Data dataObj = (Data)data.get(i);
				if(dataObj.name.equals(name) && dataObj.key == true){
					dataValue = dataObj;
					break;
				}
			}
			return dataValue;
		}
	}
	
	public Data getData(String name){
		
		if(data == null){return null;}
		else{
			Data dataValue = null;
			for(int i=0;i<data.size();i++){
				Data dataObj = (Data)data.get(i);
				if(dataObj.name.equals(name) && dataObj.key == false){
					dataValue = dataObj;
					break;
				}
			}
			return dataValue;
		}
	}
	
	
	public void deleteData(String name){
		if(data == null){return;}
		else{
			for(int i=0;i<data.size();i++){
				Data dataObj = (Data)data.get(i);
				if(dataObj.name.equals(name) && dataObj.key == false){
					data.remove(i);
				}
			}
		}
	}
	
	public void deleteKeyData(String name){
		if(data == null){return;}
		else{
			for(int i=0;i<data.size();i++){
				Data dataObj = (Data)data.get(i);
				if(dataObj.name.equals(name) && dataObj.key == true){
					data.remove(i);
				}
			}
		}
	}
	
	public void insertData(Data addData){
		if(data == null){
			data = new ArrayList<Data>();
		}
		data.add(addData);
		
	}
	
	public void setData(String name,String value,String oper,String order,boolean key){
		Data dataObj = new Data();
		dataObj.name = name;
		dataObj.val = value;
		dataObj.oper = oper;
		dataObj.order = order;
		dataObj.key = key;
		insertData(dataObj);
	}
	
	
	public void setData(String name,Object value,String oper,String order,boolean key){
		Data dataObj = new Data();
		dataObj.name = name;
		dataObj.val = value;
		dataObj.oper = oper;
		dataObj.order = order;
		dataObj.key = key;
		insertData(dataObj);
	}
	
	public void setData(String name,String value){
		Data dataObj = new Data();
		dataObj.name = name;
		dataObj.val = value;
		insertData(dataObj);
	}
	
	
	public void setData(String name,long value){
		Data dataObj = new Data();
		dataObj.name = name;
		dataObj.val = value;
		insertData(dataObj);
	}
	
	
	public void setData(String name,Timestamp value){
		Data dataObj = new Data();
		dataObj.name = name;
		dataObj.val = value;
		insertData(dataObj);
	}
	
	

}
