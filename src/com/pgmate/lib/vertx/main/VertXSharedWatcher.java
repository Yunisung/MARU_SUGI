package com.pgmate.lib.vertx.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Administrator
 *
 */
public class VertXSharedWatcher extends Thread {

	private long SHARED_REMOVER_INTERVAL 	= 60*1000;
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.lib.vertx.main.VertXSharedWatcher.class );
	

	@Override
	public void run() {
		
		logger.info("vertx shared remover start");
		
		try{
		
			while(true){
				try{
					Thread.sleep(SHARED_REMOVER_INTERVAL);
					VertXShared.removeRoutingZombie();
				}catch(InterruptedException e){
					logger.info("interrupt error : {}",e.getMessage());
				}
				
			}
		}finally{
			logger.info("vertx shared remover exit");
		}
	}
	

}
