package com.pgmate.sugi.model.ajax;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Administrator
 *
 */
@XmlRootElement(name = "file")
public class Files implements java.io.Serializable{
	
	@XmlElement(name = "link")
	public String link	= "";
	
	@XmlElement(name = "auth")
	public String auth	= "";
	
	public Files() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the link
	 */
	public String getLink() {
		return link;
	}

	/**
	 * @return the auth
	 */
	public String getAuth() {
		return auth;
	}
	
}
