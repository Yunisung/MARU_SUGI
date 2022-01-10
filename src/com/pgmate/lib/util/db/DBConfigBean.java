package com.pgmate.lib.util.db;

import com.pgmate.lib.util.gson.GsonUtil;

/**
 * @author Administrator
 *
 */
public class DBConfigBean {

	private String dbType 		= "";
	private String poolName		= "";
	private String driver		= "";
	private String jdbcUrl		= "";
	private String userName		= "";
	private String passWord		= "";
	private int maxConnection	= 10;
	private int minConnection	= 5;
	private int partitionCount	= 1;
	
	
	public DBConfigBean(){	
	}


	public String getDbType() {
		return dbType;
	}


	public void setDbType(String dbType) {
		this.dbType = dbType;
	}


	public String getDriver() {
		return driver;
	}


	public void setDriver(String driver) {
		this.driver = driver;
	}


	public String getJdbcUrl() {
		return jdbcUrl;
	}


	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getPassWord() {
		return passWord;
	}


	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}


	public String getPoolName() {
		return poolName;
	}


	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}


	public int getMaxConnection() {
		return maxConnection;
	}


	public void setMaxConnection(int maxConnection) {
		this.maxConnection = maxConnection;
	}


	public int getMinConnection() {
		return minConnection;
	}


	public void setMinConnection(int minConnection) {
		this.minConnection = minConnection;
	}


	public int getPartitionCount() {
		return partitionCount;
	}


	public void setPartitionCount(int partitionCount) {
		this.partitionCount = partitionCount;
	}

	public String toJson(){
		return GsonUtil.toJson(this, true, "");
	}
	
	
	
}
