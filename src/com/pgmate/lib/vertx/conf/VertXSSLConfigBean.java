package com.pgmate.lib.vertx.conf;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 *
 */
public class VertXSSLConfigBean {

	private boolean ssl				= false;
	private String protocol			= "";
	private List<String> cipherSuite= new ArrayList<String>();
	private String keyStore			= "";
	private String keyStorePassword	= "";
	private String securityProperteis= "";
	
	public VertXSSLConfigBean() {
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
	 * @return the protocol
	 */
	public String getProtocol() {
		return protocol;
	}

	/**
	 * @param protocol the protocol to set
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	/**
	 * @return the cipherSuite
	 */
	public List<String> getCipherSuite() {
		return cipherSuite;
	}

	/**
	 * @param cipherSuite the cipherSuite to set
	 */
	public void setCipherSuite(List<String> cipherSuite) {
		this.cipherSuite = cipherSuite;
	}

	/**
	 * @return the keyStore
	 */
	public String getKeyStore() {
		return keyStore;
	}

	/**
	 * @param keyStore the keyStore to set
	 */
	public void setKeyStore(String keyStore) {
		this.keyStore = keyStore;
	}

	/**
	 * @return the keyStorePassword
	 */
	public String getKeyStorePassword() {
		return keyStorePassword;
	}

	/**
	 * @param keyStorePassword the keyStorePassword to set
	 */
	public void setKeyStorePassword(String keyStorePassword) {
		this.keyStorePassword = keyStorePassword;
	}

	/**
	 * @return the securityProperteis
	 */
	public String getSecurityProperteis() {
		return securityProperteis;
	}

	/**
	 * @param securityProperteis the securityProperteis to set
	 */
	public void setSecurityProperteis(String securityProperteis) {
		this.securityProperteis = securityProperteis;
	}
	
	

}
