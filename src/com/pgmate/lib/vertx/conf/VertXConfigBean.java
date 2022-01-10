package com.pgmate.lib.vertx.conf;

import com.pgmate.lib.util.gson.GsonUtil;

/**
 * @author Administrator
 *
 */
public class VertXConfigBean {

	
	private String host				= "";
	private int port				= 8080;
	private int idleTimeout			= 2*60*1000;
	private int receiveBufferSize	= 4*1000*1024;
	private String routeClass		= "";
	private boolean tcpKeepAlive	= true;
	private int soLinger			= 0;
	private String module			= "";
	private String webroot			= "";
	private boolean disableCaching  = true;	
	
	private VertXSSLConfigBean ssl	= null;
	
	public VertXConfigBean() {
		// TODO Auto-generated constructor stub
	}


	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the idleTimeout
	 */
	public int getIdleTimeout() {
		return idleTimeout;
	}

	/**
	 * @param idleTimeout the idleTimeout to set
	 */
	public void setIdleTimeout(int idleTimeout) {
		this.idleTimeout = idleTimeout;
	}

	/**
	 * @return the receiveBufferSize
	 */
	public int getReceiveBufferSize() {
		return receiveBufferSize;
	}

	/**
	 * @param receiveBufferSize the receiveBufferSize to set
	 */
	public void setReceiveBufferSize(int receiveBufferSize) {
		this.receiveBufferSize = receiveBufferSize;
	}

	/**
	 * @return the routeClass
	 */
	public String getRouteClass() {
		return routeClass;
	}

	/**
	 * @param routeClass the routeClass to set
	 */
	public void setRouteClass(String routeClass) {
		this.routeClass = routeClass;
	}

	/**
	 * @return the tcpKeepAlive
	 */
	public boolean isTcpKeepAlive() {
		return tcpKeepAlive;
	}

	/**
	 * @param tcpKeepAlive the tcpKeepAlive to set
	 */
	public void setTcpKeepAlive(boolean tcpKeepAlive) {
		this.tcpKeepAlive = tcpKeepAlive;
	}

	/**
	 * @return the soLinger
	 */
	public int getSoLinger() {
		return soLinger;
	}

	/**
	 * @param soLinger the soLinger to set
	 */
	public void setSoLinger(int soLinger) {
		this.soLinger = soLinger;
	}

	/**
	 * @return the ssl
	 */
	public VertXSSLConfigBean getSsl() {
		return ssl;
	}

	/**
	 * @param ssl the ssl to set
	 */
	public void setSsl(VertXSSLConfigBean ssl) {
		this.ssl = ssl;
	}
	

	/**
	 * @return the module
	 */
	public String getModule() {
		return module;
	}


	/**
	 * @param module the module to set
	 */
	public void setModule(String module) {
		this.module = module;
	}

	/**
	 * @return the webroot
	 */
	public String getWebroot() {
		return webroot;
	}


	/**
	 * @param webroot the webroot to set
	 */
	public void setWebroot(String webroot) {
		this.webroot = webroot;
	}


	/**
	 * @return the disableCaching
	 */
	public boolean isDisableCaching() {
		return disableCaching;
	}


	/**
	 * @param disableCaching the disableCaching to set
	 */
	public void setDisableCaching(boolean disableCaching) {
		this.disableCaching = disableCaching;
	}


	public String toJson(){
		return GsonUtil.toJson(this, true, "");
	}
	

}
