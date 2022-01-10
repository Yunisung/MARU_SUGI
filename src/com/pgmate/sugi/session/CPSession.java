package com.pgmate.sugi.session;

import java.sql.Timestamp;
import java.util.List;

import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

/**
 * @author Administrator
 *
 */
public class CPSession implements java.io.Serializable{

	

	private String regDay		= CommonUtil.getCurrentDate("yyyyMMdd");
	private String lastUrl 		= "";

	private String tmnId		= "";
	private String mchtId		= "";
	private String mchtName		= "";
	private String serial		= "";
	private String payKey		= "";
	public String getRegDay() {
		return regDay;
	}
	public void setRegDay(String regDay) {
		this.regDay = regDay;
	}
	public String getLastUrl() {
		return lastUrl;
	}
	public void setLastUrl(String lastUrl) {
		this.lastUrl = lastUrl;
	}
	public String getTmnId() {
		return tmnId;
	}
	public void setTmnId(String tmnId) {
		this.tmnId = tmnId;
	}
	public String getMchtId() {
		return mchtId;
	}
	public void setMchtId(String mchtId) {
		this.mchtId = mchtId;
	}
	public String getSerial() {
		return serial;
	}
	public void setSerial(String serial) {
		this.serial = serial;
	}
	public String getPayKey() {
		return payKey;
	}
	public void setPayKey(String payKey) {
		this.payKey = payKey;
	}
	public String getMchtName() {
		return mchtName;
	}
	public void setMchtName(String mchtName) {
		this.mchtName = mchtName;
	}



}
