package com.pgmate.lib.util.comm;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.util.LinkedList;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import com.pgmate.lib.util.lang.CommonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Administrator
 *
 */
public class SslSocketService {

	private SSLServerSocket serverSocket 	= null;
	private Thread serviceThread 			= null;
	private boolean running 				= false;
	private boolean debug					= false;
	private SslSocketServer itsServer		= null;
	private LinkedList<Thread> threads	= new LinkedList<Thread>();
	private Logger logger = LoggerFactory.getLogger( getClass() );
	

	public SslSocketService(SslSocketServer server,int port ,String keyStore,String keyStorePasswd,boolean debug) throws Exception{
		this.itsServer = server;
		char[] ksPass = keyStorePasswd.toCharArray();
		this.debug = debug;
		KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(keyStore), ksPass);
        KeyManagerFactory kmf = 
        KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, ksPass);
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(kmf.getKeyManagers(), null, null);
        SSLServerSocketFactory ssf = sc.getServerSocketFactory();
       
		serverSocket 	= (SSLServerSocket) ssf.createServerSocket(port);
		printServerSocketInfo();
		serviceThread 	= new Thread( new Runnable() {
			public void run() {
				serviceThread();
			}
		});

		serviceThread.start();
	}

	public void close() throws Exception {
		waitForServiceThreadToStart();
		running = false;
		serverSocket.close();
		serviceThread.join();
		waitForServerThreads();
	}

	private void waitForServiceThreadToStart() {
		while(running == false)
			Thread.yield();
	}

	private void serviceThread() {
		running = true;
		while(running) {
			try {
				SSLSocket socket = (SSLSocket)serverSocket.accept();
				if(debug){
					printSocketInfo(socket);
				}
				startServerThread(socket);
			}catch(IOException e) {
				logger.debug("error : {}",CommonUtil.getExceptionMessage(e));
			}
		}
	}

	private void startServerThread(SSLSocket socket) {
		Thread serverThread = new Thread(new ServerRunner(socket));
		synchronized(threads) {
			threads.add(serverThread);
		}
		serverThread.start();
	}

	private void waitForServerThreads() throws InterruptedException {
		while(threads.size() > 0) {
			Thread t;
			synchronized(threads) {
				t = (Thread)threads.getFirst();
			}
			t.join();
		}
	}
	

	  
	private void printServerSocketInfo() {
		logger.debug("Server socket class : {}",serverSocket.getClass());
		logger.debug("Socker address : {} ",serverSocket.getInetAddress().toString());
		logger.debug("Socker port : {} ",serverSocket.getLocalPort());
		logger.debug("Need client authentication : {} ",serverSocket.getNeedClientAuth());
		logger.debug("Want client authentication : {} ",serverSocket.getWantClientAuth());
		logger.debug("Use client mode : {} ",CommonUtil.toString(serverSocket.getUseClientMode()));
	} 

	private void printSocketInfo(SSLSocket socket) {
		
		logger.debug("Socket class: "+socket.getClass());
		logger.debug("Remote address = "     +socket.getInetAddress().toString());
		logger.debug("Remote port = "+socket.getPort());
		logger.debug("Local socket address = " +socket.getLocalSocketAddress().toString());
		logger.debug("Local address = " +socket.getLocalAddress().toString());
		logger.debug("Local port = "+socket.getLocalPort());
		logger.debug("Need client authentication = "  +socket.getNeedClientAuth());
	    SSLSession ss = socket.getSession();
	    logger.debug("Cipher suite = "+ss.getCipherSuite());
	    logger.debug("Protocol = "+ss.getProtocol());
	}



	public class ServerRunner implements Runnable {

		private SSLSocket itsSocket = null;

		ServerRunner(SSLSocket socket){
			itsSocket = socket;
		}

		public void run() {
			try {
				itsServer.serve(itsSocket);
				synchronized(threads) {
					threads.remove(Thread.currentThread());
				}
				itsSocket.close();
			}catch(IOException e){
				logger.debug("error : {}",CommonUtil.getExceptionMessage(e));
			}
		}
	}
}
