package com.pgmate.lib.tomcat.conf;


/**
 * @author Administrator
 *
 */
public class TomcatConfigBean {

	private String serverName		= "CP_WEB_V1";
	private int port				= 80;
	private String host				= "cp.cyrexpay.com";	
	private int minThreads			= 20;
	private int maxThreads			= 150;
	private TomcatSSLConfigBean ssl	= null;
	private String contextPath		= "../";
	
	public TomcatConfigBean() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the serverName
	 */
	public String getServerName() {
		return serverName;
	}

	/**
	 * @param serverName the serverName to set
	 */
	public void setServerName(String serverName) {
		this.serverName = serverName;
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
	 * @return the minThreads
	 */
	public int getMinThreads() {
		return minThreads;
	}

	/**
	 * @param minThreads the minThreads to set
	 */
	public void setMinThreads(int minThreads) {
		this.minThreads = minThreads;
	}

	/**
	 * @return the maxThreads
	 */
	public int getMaxThreads() {
		return maxThreads;
	}

	/**
	 * @param maxThreads the maxThreads to set
	 */
	public void setMaxThreads(int maxThreads) {
		this.maxThreads = maxThreads;
	}

	/**
	 * @return the ssl
	 */
	public TomcatSSLConfigBean getSsl() {
		return ssl;
	}

	/**
	 * @param ssl the ssl to set
	 */
	public void setSsl(TomcatSSLConfigBean ssl) {
		this.ssl = ssl;
	}

	/**
	 * @return the contextPath
	 */
	public String getContextPath() {
		return contextPath;
	}

	/**
	 * @param contextPath the contextPath to set
	 */
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}


}
