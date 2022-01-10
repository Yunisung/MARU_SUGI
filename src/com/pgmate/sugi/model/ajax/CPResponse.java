package com.pgmate.sugi.model.ajax;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.pgmate.lib.util.map.SharedMap;

/**
 * @author Administrator
 *
 */
@XmlRootElement(name = "response")
public class CPResponse implements java.io.Serializable{

	@XmlElement(name = "result")
	public Result result	= null;
	
	@XmlElement(name = "type")
	public String type		= "";
	
	@XmlElement(name = "redirect")
	public String redirect	= "";
	
	@XmlElement(name = "page")
	public Page page		= null;
	
	@XmlElement(name = "thead")
	public String thead		= null;
	
	@XmlElement(name = "data")
	public List<SharedMap<String,Object>> data	= null;
	
	@XmlElement(name = "sum")
	public SharedMap<String,Object> sum	= null;
	
	@XmlElement(name = "file")
	public Files file		= null;
	
	
	
	
	public CPResponse() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @return the result
	 */
	public Result getResult() {
		return result;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the redirect
	 */
	public String getRedirect() {
		return redirect;
	}

	/**
	 * @return the page
	 */
	public Page getPage() {
		return page;
	}

	/**
	 * @return the data
	 */
	public List<SharedMap<String, Object>> getData() {
		return data;
	}

	/**
	 * @return the file
	 */
	public Files getFile() {
		return file;
	}
	
	
	public SharedMap<String, Object> getSum() {
		return sum;
	}

}
