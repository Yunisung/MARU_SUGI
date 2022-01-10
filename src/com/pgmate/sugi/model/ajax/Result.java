package com.pgmate.sugi.model.ajax;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Administrator
 *
 */
@XmlRootElement(name = "result")
public class Result implements java.io.Serializable{

	@XmlElement(name = "code")
	public int code		= 200;
	
	@XmlElement(name = "message")
	public String message	= "";
	
	@XmlElement(name = "error")
	public String error		= "";
	
	
	public Result() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}


	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}


	/**
	 * @return the error
	 */
	public String getError() {
		return error;
	}
}
