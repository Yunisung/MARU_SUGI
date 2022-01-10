package com.pgmate.sugi.model.ajax;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Administrator
 *
 */
@XmlRootElement(name = "data")
public class Data implements java.io.Serializable{

	@XmlElement(name = "name")
	public String name	= "";
	
	@XmlElement(name = "val")
	public Object val	= "";
	
	@XmlElement(name = "oper")
	public String oper	= "";
	
	@XmlElement(name = "order")
	public String order	= "";
	
	@XmlElement(name = "key")
	public boolean key	= false;
	
	public Data() {
		// TODO Auto-generated constructor stub
	}

}
