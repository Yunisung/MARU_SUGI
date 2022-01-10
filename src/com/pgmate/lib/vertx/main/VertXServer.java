package com.pgmate.lib.vertx.main;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.JksOptions;
import io.vertx.ext.web.Router;

import com.pgmate.lib.conf.ConfigLoader;
import com.pgmate.lib.util.lang.ClassUtil;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.vertx.conf.VertXConfigBean;
import com.pgmate.lib.vertx.conf.VertXSSLConfigBean;
import com.pgmate.lib.vertx.util.Runner;

/**
 * @author Administrator
 *
 */
public class VertXServer extends AbstractVerticle {
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.lib.vertx.main.VertXServer.class );
	private static VertXConfigBean vertxConfig = null;
	private HttpServer server = null;
	@Override
	public void start(Future<Void> fut) {
		
		if(vertxConfig == null){
			vertxConfig = ConfigLoader.getConfig().vertx;
		}
		try{
			logger.info("VertXServer init ... ");
			
			if(vertxConfig.isDisableCaching()){
				System.setProperty("vertx.disableFileCaching", "true");
				logger.info("FileCaching disabled");
			}
			
			Router router = Router.router(vertx);
			RouteWorker worker = (RouteWorker)ClassUtil.getObject(vertxConfig.getRouteClass());
			worker.execute(vertxConfig,router);
			
			VertxOptions vertxOptions=new VertxOptions();
			vertxOptions.setBlockedThreadCheckInterval(60000);
			vertxOptions.setWorkerPoolSize(100);
			vertxOptions.setEventLoopPoolSize(10);
			vertx=Vertx.vertx(vertxOptions);
			
			DeploymentOptions options = new DeploymentOptions().setWorker(true); 
			options.setInstances(10);
			options.setWorkerPoolSize(100);
			vertx.deployVerticle("Verticle", options);
			
		    server = 
				vertx.createHttpServer(createOptions())
				.requestHandler(router::accept)
				.listen(
						vertxConfig.getPort(), vertxConfig.getHost(),
						result -> {
							if (result.succeeded()) {
								fut.complete();
							}else{
								fut.fail(result.cause());
							}
						}
			);
			
			logger.info("VertXServer start [{},{}]",vertxConfig.getHost(),vertxConfig.getPort());
			logger.info("VertXServer module [{}]",vertxConfig.getModule());
			logger.info("VertXServer route [{}]",vertxConfig.getRouteClass());
			
			
			
			//VertX Thread 감시 실행
			new VertXSharedWatcher().start();

		}catch(Exception e){
			logger.info("VertXServer error : [{}], config : [{}]",CommonUtil.getExceptionMessage(e),vertxConfig.toJson());
			vertx.close();
			System.exit(1);
		}
	

	}

	@Override 
	public void stop(Future<Void> future) { 
		if (server == null) {
			future.complete(); 
			return; 
		} 
		server.close(result -> { 
			if (result.failed()) { 
				future.fail(result.cause()); 
			} else { 
				future.complete(); 
			} 
		}); 
	} 
	
	private HttpServerOptions createOptions(){
		HttpServerOptions options = new HttpServerOptions();
		options.setPort(vertxConfig.getPort());
		options.setIdleTimeout(vertxConfig.getIdleTimeout());
		options.setReceiveBufferSize(vertxConfig.getReceiveBufferSize());
		options.setTcpKeepAlive(vertxConfig.isTcpKeepAlive());
		options.setSoLinger(vertxConfig.getSoLinger());
	
		
		if(vertxConfig.getSsl() != null){
			VertXSSLConfigBean vertxSSL = vertxConfig.getSsl();
			if(vertxSSL.isSsl()){
				logger.info("VertXServer set ssl  ... ");
				options.setSsl(vertxSSL.isSsl());
				
				for(String cipherSuite : vertxSSL.getCipherSuite()){
					options.addEnabledCipherSuite(cipherSuite);
				}
				if(!CommonUtil.isNullOrSpace(vertxSSL.getSecurityProperteis())){
					System.setProperty("java.security.properties",vertxSSL.getSecurityProperteis());
				}
				
				
				logger.trace("size {}",options.getEnabledCipherSuites().size());
				for (Iterator<String> iterator = options.getEnabledCipherSuites().iterator(); iterator.hasNext();) {
					String key =  (String) iterator.next();
					logger.debug(key);
				}
				if(!CommonUtil.isNullOrSpace(vertxSSL.getProtocol())){
					System.setProperty("https.protocols",vertxSSL.getProtocol());
				}
				options.setKeyStoreOptions(new JksOptions().setPath(vertxSSL.getKeyStore()).setPassword(vertxSSL.getKeyStorePassword()));
			}
		}
		return options;
	}

	
	
	
	public static void main(String[] args){
		try{
			Runner.runJava("../war", VertXServer.class, false);
		}catch(Exception e){
			logger.debug(CommonUtil.getExceptionMessage(e));
		}
	}

}
