package com.pgmate.lib.tomcat;

import java.io.File;
import java.io.IOException;
import java.util.logging.Handler;
import java.util.logging.LogManager;

import org.apache.catalina.Context;
import org.apache.catalina.Service;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.pgmate.lib.conf.ConfigLoader;
import com.pgmate.lib.tomcat.conf.TomcatConfigBean;
import com.pgmate.lib.util.lang.CommonUtil;

import java.io.BufferedWriter;
import java.io.FileWriter;


/**
 * @author Administrator
 *
 */
public class Tomcat8 {
	
	private static Logger logger 	= LoggerFactory.getLogger( com.pgmate.lib.tomcat.Tomcat8.class );
	private TomcatConfigBean tomcatConfig	= null;
	
	public Tomcat8() {
		tomcatConfig = ConfigLoader.getConfig().tomcat;
	}
	
	public void start() throws IOException {
		
		if(tomcatConfig == null){
			logger.info("Configuration is not set ( service.json)");
			System.exit(1);
		}
		try{
			String contextDir	= new File(tomcatConfig.getContextPath()).getCanonicalPath();
			logger.info("root directory : {}",contextDir);
			
			Tomcat tomcat = new Tomcat();
			
			
	        tomcat.setBaseDir(contextDir+File.separator+"web"+File.separator+"WEB-INF"+File.separator+"classes");
	        tomcat.setHostname(tomcatConfig.getHost());
	        
	       
	        Service service = tomcat.getService();
	        
	        setDefaultConnector(tomcat.getConnector());
	        service.addConnector(getAJPConnector());
	        
	        
	        if(tomcatConfig.getSsl().isSsl()){
	        	service.addConnector(getSSLConnector());
	        }
	        
	        java.util.logging.Logger rootLogger=LogManager.getLogManager().getLogger("");
		    Handler[] handlers=rootLogger.getHandlers();
		    for (final Handler handler : handlers) {
		    	rootLogger.removeHandler(handler);
		    }
		    
		    Context ctx = tomcat.addWebapp("/", contextDir+File.separator+"web");
		    ctx.setReloadable(true);

		    
		    //ctx.getNamingResources().addResource(getDbConnect());
		    logger.info("web directory : {}",contextDir+File.separator+"web");
		    rootLogger.addHandler(new SLF4JBridgeHandler());
		     
		    
		    tomcat.start();
	        tomcat.getServer().await();
			
		}catch(Exception e){
			logger.info("Tomcat8 start error : {}",CommonUtil.getExceptionMessage(e));
		}
	}
	
	
	private void setDefaultConnector(Connector connector){
		
	    connector.setPort(tomcatConfig.getPort());
	    connector.setSecure(false);
	    connector.setScheme("http");
	    connector.setProtocol("HTTP/1.1");
	    if(tomcatConfig.getSsl().isSsl()){
	    connector.setRedirectPort(tomcatConfig.getSsl().getPort());
	    }
	    connector.setAttribute("address", tomcatConfig.getHost());
	    connector.setAttribute("minThreads",tomcatConfig.getMinThreads());
	    connector.setAttribute("maxThreads",tomcatConfig.getMaxThreads());
	    connector.setAttribute("connectionTimeout",180000);
	    connector.setAttribute("maxPostSize", 20*1024*1024);
	    connector.setAttribute("maxSavePostSize", 20000);
	    
	    
	}
	
	private Connector getAJPConnector(){
		Connector connector = new Connector();

		int port = 8090;
		if(tomcatConfig.getPort() < 8080){
		}else if(tomcatConfig.getPort() == 8080){
			port = 8091;
		}else{
			port = 8091+(tomcatConfig.getPort()-8080);
		}
		connector.setPort(port);
		connector.setAttribute("address", tomcatConfig.getHost());
	    connector.setAttribute("protocol", "AJP/1.3");
	    connector.setAttribute("maxPostSize", 20*1024*1024);
	    connector.setAttribute("maxSavePostSize", 20000);
	    connector.setRedirectPort(tomcatConfig.getSsl().getPort());
	    
	    return connector;   
	}
	
	
	private Connector getSSLConnector(){
		Connector connector = new Connector();
	    connector.setPort(tomcatConfig.getSsl().getPort());
	    connector.setSecure(true);
	    connector.setScheme("https");
	    connector.setProtocol("org.apache.coyote.http11.Http11NioProtocol");
	    connector.setAttribute("address", tomcatConfig.getHost());
	    connector.setAttribute("keystoreType",tomcatConfig.getSsl().getKeystoreType());
	    connector.setAttribute("keystorePass",tomcatConfig.getSsl().getKeystorePass());
	    connector.setAttribute("keystoreFile",tomcatConfig.getSsl().getKeystoreFile());
	    connector.setAttribute("sslProtocol",tomcatConfig.getSsl().getSslProtocol());
	    connector.setAttribute("sslEnabledProtocols",tomcatConfig.getSsl().getSslEnabledProtocols());
	    connector.setAttribute("ciphers",tomcatConfig.getSsl().getCiphers());
	    connector.setAttribute("minThreads",tomcatConfig.getMinThreads());
	    connector.setAttribute("maxThreads",tomcatConfig.getMaxThreads());
	    //SSL default attribute
	    connector.setAttribute("clientAuth", "false");
	    connector.setAttribute("SSLEnabled", true);
	    connector.setAttribute("maxPostSize", 20*1024*1024);
	    connector.setAttribute("maxSavePostSize", 20*1024*1024);
	    			
	    return connector;
	}
	
	public static void main(String[] args){
		/*
		Tomcat8 tomcat = new Tomcat8();
		try{
			tomcat.start();
		}catch(Exception e){
			e.printStackTrace();
		}
		*/
		createFile();
	}
	
	private static void createFile() {
		
		String txt = "테스트입니다!!";
		
		String fileName = "/home/bkwinners/MARU_SUGI/bin/test11.txt";
		
		
		try{
			
			// 파일 객체 생성
			File file = new File(fileName);
			file.createNewFile();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}


}
