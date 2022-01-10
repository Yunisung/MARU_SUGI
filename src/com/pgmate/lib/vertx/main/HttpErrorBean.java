package com.pgmate.lib.vertx.main;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Administrator
 *
 */
@XmlRootElement(name = "error")
public class HttpErrorBean {
	
	@XmlElement(name = "code")
	public int code	= 0;
	@XmlElement(name = "statusMessage")
	public String statusMessage	= "";
	@XmlElement(name = "message")
	public String message		= "";
	

	public HttpErrorBean() {
	}
	
}
