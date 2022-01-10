package com.pgmate.lib.tomcat.conf;

/**
 * @author Administrator
 *
 */
public class TomcatSSLConfigBean {

	private boolean ssl				= false;
	private int port				= 443;
	private String sslProtocol		= "TLS";
	private String keystoreFile		= "./cyrexpay_2015.jks";
	private String keystorePass		= "123456";
	private String keystoreType		= "JKS";
	private String sslEnabledProtocols	= "TLSv1,TLSv1.1,TLSv1.2";
	private String ciphers			= "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256,TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA,"
			  + "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384,TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,TLS_ECDHE_RSA_WITH_RC4_128_SHA,"
			  + "TLS_RSA_WITH_AES_128_CBC_SHA256,TLS_RSA_WITH_AES_128_CBC_SHA,TLS_RSA_WITH_AES_256_CBC_SHA256,"
			  + "TLS_RSA_WITH_AES_256_CBC_SHA,SSL_RSA_WITH_RC4_128_SHA";

	public TomcatSSLConfigBean() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the ssl
	 */
	public boolean isSsl() {
		return ssl;
	}

	/**
	 * @param ssl the ssl to set
	 */
	public void setSsl(boolean ssl) {
		this.ssl = ssl;
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
	 * @return the sslProtocol
	 */
	public String getSslProtocol() {
		return sslProtocol;
	}

	/**
	 * @param sslProtocol the sslProtocol to set
	 */
	public void setSslProtocol(String sslProtocol) {
		this.sslProtocol = sslProtocol;
	}

	/**
	 * @return the keystoreFile
	 */
	public String getKeystoreFile() {
		return keystoreFile;
	}

	/**
	 * @param keystoreFile the keystoreFile to set
	 */
	public void setKeystoreFile(String keystoreFile) {
		this.keystoreFile = keystoreFile;
	}

	/**
	 * @return the keystorePass
	 */
	public String getKeystorePass() {
		return keystorePass;
	}

	/**
	 * @param keystorePass the keystorePass to set
	 */
	public void setKeystorePass(String keystorePass) {
		this.keystorePass = keystorePass;
	}

	/**
	 * @return the keystoreType
	 */
	public String getKeystoreType() {
		return keystoreType;
	}

	/**
	 * @param keystoreType the keystoreType to set
	 */
	public void setKeystoreType(String keystoreType) {
		this.keystoreType = keystoreType;
	}

	/**
	 * @return the sslEnabledProtocols
	 */
	public String getSslEnabledProtocols() {
		return sslEnabledProtocols;
	}

	/**
	 * @param sslEnabledProtocols the sslEnabledProtocols to set
	 */
	public void setSslEnabledProtocols(String sslEnabledProtocols) {
		this.sslEnabledProtocols = sslEnabledProtocols;
	}

	/**
	 * @return the ciphers
	 */
	public String getCiphers() {
		return ciphers;
	}

	/**
	 * @param ciphers the ciphers to set
	 */
	public void setCiphers(String ciphers) {
		this.ciphers = ciphers;
	}

	



}
