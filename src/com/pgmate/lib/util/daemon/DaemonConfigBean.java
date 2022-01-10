package com.pgmate.lib.util.daemon;

import com.pgmate.lib.util.gson.GsonUtil;

/**
 * @author Administrator
 *
 */
public class DaemonConfigBean {

	private String className	= "";
	private String pidName		= "";
	private long interval		= 10000;
	private String log			= "Y";
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getPidName() {
		return pidName;
	}
	public void setPidName(String pidName) {
		this.pidName = pidName;
	}
	public long getInterval() {
		return interval;
	}
	public void setInterval(long interval) {
		this.interval = interval;
	}
	public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log = log;
	}
	
	public String toJson(){
		return GsonUtil.toJson(this, true, "");
	}
	
	
}
