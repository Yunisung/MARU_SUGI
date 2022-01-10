package com.pgmate.lib.util.comm;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

import com.pgmate.lib.util.lang.CommonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Administrator
 *
 */
public class SocketService {
	private ServerSocket serverSocket 	= null;
	private Thread serviceThread 		= null;
	private boolean running 			= false;
	private SocketServer itsServer		= null;
	private LinkedList<Thread> threads			= new LinkedList<Thread>();
	private Logger logger = LoggerFactory.getLogger( getClass() );

	public SocketService(int port , SocketServer server) throws Exception{
		itsServer = server;
		serverSocket 	= new ServerSocket(port);
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
				Socket socket = serverSocket.accept();
				startServerThread(socket);
			}catch(IOException e) {
				logger.debug("error : {}",CommonUtil.getExceptionMessage(e));
			}
		}
	}

	private void startServerThread(Socket socket) {
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



	public class ServerRunner implements Runnable {

		private Socket itsSocket = null;

		ServerRunner(Socket socket){
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
