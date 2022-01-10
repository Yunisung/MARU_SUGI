package com.pgmate.lib.util.comm;

import javax.net.ssl.SSLSocket;

/**
 * @author Administrator
 *
 */
public interface SslSocketServer {
	public void serve(SSLSocket socket);
}
