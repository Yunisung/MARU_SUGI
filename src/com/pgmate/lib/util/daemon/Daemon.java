package com.pgmate.lib.util.daemon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.lib.conf.ConfigLoader;
import com.pgmate.lib.util.lang.CommonUtil;


/**
 * @author Administrator
 *
 */
public class Daemon extends Thread {

	private Logger logger = LoggerFactory.getLogger( com.pgmate.lib.util.daemon.Daemon.class);
	
	private DaemonAbstract daemon			= null;
	private DaemonConfigBean configBean		= null; 	
	
	
	public Daemon(){
		this("DAEMON");
	}
	public Daemon(String configName){
		loadConfig(configName);
		if(configBean == null){
			System.exit(1);
		}else{
			if(loadClass()){
				super.setDaemon(true);
				super.setName(configBean.getPidName());
				start();
				System.out.println("TRUSTMATE REALTIMEDAEMON START = "+ CommonUtil.getCurrentDate() +" Version = 1.2 , Update=2014-06-02");
				System.out.println("CLASSNAME =["+ configBean.getClassName()+"] INTERVAL=["+configBean.getInterval()+"]");
				logger.debug("TRUSTMATE REALTIMEDAEMON START = "+ CommonUtil.getCurrentDate() +" Version = 1.2 , Update=2014-06-02");
				logger.debug("CLASSNAME =["+ configBean.getClassName()+"] INTERVAL=["+configBean.getInterval()+"]");
				while(true){
					try { 
						sleep(configBean.getInterval());
					} catch( InterruptedException e ) {
					}
				}
				
			}
		}
	}
	
	
	public void run(){   
        while(true) {   
            try {   
            	sleep(configBean.getInterval());
            	if(configBean.getLog().equals("Y")){
            		logger.debug("DAEMON EXECUTE	="+daemon.execute(),this);
            	}else{
            		daemon.execute();
            	}
            	System.gc(); 
            } catch( Exception e ) {
            	logger.debug("Daemon execute error : {} "+CommonUtil.getExceptionMessage(e));
			}
        }
    }
	
	
	private boolean loadClass(){
		boolean loaded = false;
		try{
			daemon = (DaemonAbstract)Class.forName(configBean.getClassName().trim()).newInstance();
			loaded = true;
		}catch(Exception e){
			e.printStackTrace();
			logger.debug("class load error : {}",CommonUtil.getExceptionMessage(e));
		}
		return loaded;
	}
	
	
	private void loadConfig(String configName){
		try{
			if(configBean == null){
				configBean = ConfigLoader.getConfig().daemon;
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
			logger.debug("Daemon Configuration load error : {} "+CommonUtil.getExceptionMessage(e));
		}
	}
	
	
	public static void main(String[] args){
		if(args.length == 0){
			new Daemon();
		}else if(args.length == 1){
			new Daemon(args[0]);
		}else{
			System.out.println("com.pgmate.lib.util.daemon.Daemon 기본 DAEMON , 그외 ");
		}
	}
	
	
	
	
	
	
}
