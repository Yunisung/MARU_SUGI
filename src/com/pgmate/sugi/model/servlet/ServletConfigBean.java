package com.pgmate.sugi.model.servlet;

/**
 * @author Administrator
 *
 */
public class ServletConfigBean {

	
	private String firmServer	= "";
	private String zmqServer	= "";
	
	public ServletConfigBean() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the firmServer
	 */
	public String getFirmServer() {
		return firmServer;
	}

	/**
	 * @param firmServer the firmServer to set
	 */
	public void setFirmServer(String firmServer) {
		this.firmServer = firmServer;
	}

	/**
	 * @return the zmqServer
	 */
	public String getZmqServer() {
		return zmqServer;
	}

	/**
	 * @param zmqServer the zmqServer to set
	 */
	public void setZmqServer(String zmqServer) {
		this.zmqServer = zmqServer;
	}
	
	

}
