package com.pgmate.lib.vertx.main;

import java.net.URLDecoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.lib.util.lang.CommonUtil;

import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

/**
 * @author Administrator
 *
 */
public class VertXUtil {
	private static Logger logger 		= LoggerFactory.getLogger( com.pgmate.lib.vertx.main.VertXUtil.class );
	
	/**
	 * 
	 */
	public VertXUtil() {
		// TODO Auto-generated constructor stub
	}
	
	public static String getBodyAsString(RoutingContext rc){
		String body = CommonUtil.toString(rc.getBodyAsString());
		if(body.startsWith("=")){
			try{
			body = URLDecoder.decode(body.substring(1),"utf-8");
			}catch(Exception e){}
		}
		
		logger.info("req : [{}],[{}]",body,rc.getBody().length());
		return body;
		
	}
	
	public static String getContentType(RoutingContext rc){
		return CommonUtil.nToB(rc.request().getHeader(HttpHeaders.CONTENT_TYPE)).trim();
	}
	
	public static String getUserAgent(RoutingContext rc){
		return CommonUtil.nToB(rc.request().getHeader(HttpHeaders.USER_AGENT)).trim();
	}
	
	
	public static String getAcceptLanguage(RoutingContext rc){
		if(rc.request().getHeader(HttpHeaders.ACCEPT_LANGUAGE) == null){
			return "ko";
		}else{
			String lang = CommonUtil.nToB(rc.request().getHeader(HttpHeaders.ACCEPT_LANGUAGE));
			if(lang.indexOf(",") > 0){	lang = lang.split(",")[0]; }
			if(lang.indexOf("en") > -1){
				return "en";
			}else if(lang.indexOf("ja") > -1){
				return "ja";
			}else if(lang.indexOf("zh") > -1){
				return "zh";
			}else{
				return "ko";
			}
		}
	}
	
	public static String getRemoteIp(RoutingContext rc){
		return CommonUtil.nToB(rc.request().remoteAddress().host()).trim();	
	}
	
	public static HttpMethod getMethod(RoutingContext rc){
		return rc.request().method();	
	}
	
	public static boolean isMethod(RoutingContext rc,HttpMethod httpMethod){
		return rc.request().method() == httpMethod;	
	}
	
	
	public static String getClientIp(RoutingContext rc){
		String ip = getHeader(rc,"X-Forwarded-For");
		if(ip.equals("")){
			ip = getHeader(rc,"X-Forwarded-For");
		}
		if(ip.equals("")){
			ip = getHeader(rc,"Proxy-Client-IP");
		}
		if(ip.equals("")){
			ip = getHeader(rc,"WL-Proxy-Client-IP");
		}
		if(ip.equals("")){
			ip = CommonUtil.nToB(rc.request().remoteAddress().host());
		}
		
		return ip;
	}
	
	public static String getXRealIp(RoutingContext rc){
		return CommonUtil.nToB(getHeader(rc,"X-Real-IP")).trim();	
	}
	
	public static String getParameter(RoutingContext rc,String name){
		return CommonUtil.nToB(rc.request().getParam(name)).trim();
	}
	
	public static String getHeader(RoutingContext rc,String name){
		return CommonUtil.nToB(rc.request().getHeader(name)).trim();
	}
	
	public static String getSchemeHost(RoutingContext rc){
		return rc.request().scheme().toLowerCase()+"://"+rc.request().host().toLowerCase();
	}
	
	public static String getHost(RoutingContext rc){
		return rc.request().host().toLowerCase();
	}
	
	public static String getAccessLog(RoutingContext rc){
		StringBuilder sb = new StringBuilder();
		sb.append(rc.request().version());
		sb.append(",");
		sb.append(rc.request().uri());
		sb.append(",");
		sb.append(getMethod(rc));
		sb.append(",");
		sb.append(getClientIp(rc));
		sb.append(",");
		sb.append(getContentType(rc));
		sb.append(",");
		sb.append(getHeader(rc,"content-length"));
		sb.append(",");
		sb.append(getUserAgent(rc));
		
		
		return sb.toString();
	}

}
