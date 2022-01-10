package com.pgmate.sugi.model.ajax;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Administrator
 *
 */
@XmlRootElement(name = "page")
public class Page implements java.io.Serializable{

	@XmlElement(name = "current")
	public long current	= 1;
	
	@XmlElement(name = "total")
	public long total	= 0;
	
	@XmlElement(name = "size")
	public long size	= 20;
	
	@XmlElement(name = "hash")
	public String hash	= "";
	
	@XmlElement(name = "totalPage")
	public long totalPage	= 0;
	
	public Page() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @return the current
	 */
	public long getCurrent() {
		return current;
	}

	/**
	 * @return the total
	 */
	public long getTotal() {
		return total;
	}

	/**
	 * @return the size
	 */
	public long getSize() {
		return size;
	}

	/**
	 * @return the hash
	 */
	public String getHash() {
		return hash;
	}

	/**
	 * @return the totalPage
	 */
	public long getTotalPage() {
		return totalPage;
	}
}
