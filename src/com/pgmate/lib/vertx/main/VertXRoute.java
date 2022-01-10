package com.pgmate.lib.vertx.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.LoggerHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.TimeoutHandler;

/**
 * @author Administrator
 *
 */
public class VertXRoute {

	private static Logger logger = LoggerFactory.getLogger( com.pgmate.lib.vertx.main.VertXRoute.class );
	
	/**
	 * 
	 */
	public VertXRoute() {
		// TODO Auto-generated constructor stub
	}
	
	public static void setCorsHandler(Router router){
		logger.debug("router set CorsHandler");
		router.route().handler(CorsHandler.create("*")
				  .allowCredentials(true)
			      .allowedMethod(HttpMethod.GET)
			      .allowedMethod(HttpMethod.POST)
			      .allowedMethod(HttpMethod.PUT)
			      .allowedMethod(HttpMethod.DELETE)
			      .allowedMethod(HttpMethod.OPTIONS)
			      .allowedHeader("X-PINGARUNER")
			      .allowedHeader("www-authenticate")
			      .allowedHeader(HttpHeaders.AUTHORIZATION.toString())
			      .allowedHeader(HttpHeaders.CONTENT_TYPE.toString())
			      .allowedHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD.toString())
			      .allowedHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS.toString())
			      .allowedHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN.toString())
			      .allowedHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS.toString())
			      .allowedHeader(HttpHeaders.ORIGIN.toString())
			      .allowedHeader(HttpHeaders.ACCEPT.toString())
			      .allowedHeader("AuthFlash")
			      );
		
		
	    router.get("/access-control-with-get").handler(ctx -> {

	      ctx.response().setChunked(true);

	      MultiMap headers = ctx.request().headers();
	      for (String key : headers.names()) {
	        ctx.response().write(key);
	        ctx.response().write(headers.get(key));
	        ctx.response().write("\n");
	      }

	      ctx.response().end();
	    });

	    router.post("/access-control-with-post-preflight").handler(ctx -> {
	      ctx.response().setChunked(true);

	      MultiMap headers = ctx.request().headers();
	      for (String key : headers.names()) {
	        ctx.response().write(key);
	        ctx.response().write(headers.get(key));
	        ctx.response().write("\n");
	      }

	      ctx.response().end();
	    });
	}
	
	public static void setBodyHandler(Router router){
		logger.debug("router set BodyHandler");
		router.route().handler(BodyHandler.create().setBodyLimit(1024*1024));
	}
	
	public static void setLoggerHandler(Router router){
		logger.debug("router set LoggerHandler");
		router.route().handler(LoggerHandler.create());
	}
	
	public static void setTimeoutHandler(Router router,long timeout){
		logger.debug("router set TimeoutHandler");
		router.route().handler(TimeoutHandler.create(timeout));
	}
	
	public static void setStaticHander(Router router,String path,String webroot){
		logger.debug("router set StaticHandler [{}],[{}]",path,webroot);
		StaticHandler staticHandler = StaticHandler.create(webroot)
		.setCachingEnabled(false)
    	.setDirectoryListing(false)
    	.setFilesReadOnly(true)
    	.setMaxAgeSeconds(0);
		
		router.route(path).handler(staticHandler).failureHandler(fc -> {
			if(fc.statusCode() == 404){
				logger.debug("{} not found : {}",path,fc.request().uri());
			}
	    	VertXMessage.set404(fc);
		});
	}
	
	
	public static void setAPIStaticHander(Router router,String path,String webroot){
		
		logger.debug("router set StaticHandler [{}],[{}]",path,webroot);
		StaticHandler staticHandler = StaticHandler.create(webroot)
		.setCachingEnabled(true)
    	.setDirectoryListing(false)
    	.setFilesReadOnly(true)
    	.setMaxAgeSeconds(10*60)
    	.setAllowRootFileSystemAccess(true)
    	.setIncludeHidden(false);
		
		router.route(path).handler(staticHandler).failureHandler(fc -> {
			if(fc.statusCode() == 404){
				logger.debug("{} not found : {}",path,fc.request().uri());
			}
	    	VertXMessage.set404(fc);
		});
		

		
		
	}

}
