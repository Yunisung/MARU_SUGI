package com.pgmate.lib.vertx.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.lib.util.gson.GsonUtil;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.xml.XmlUtil;

import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.RoutingContext;

/**
 * @author Administrator
 *
 */
public class VertXMessage2 {

	private static Logger logger 		= LoggerFactory.getLogger( com.pgmate.lib.vertx.main.VertXMessage2.class );
	private static String TEMPLATE 		= "<html><head><title>CyrexPay API</title></head><body>statusCode : STATUS, statusMessage : STATUSMESSAGE, message : MESSAGE</body></html>";
	private static String CROSS_DOMAIN	= "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<cross-domain-policy>\n<allow-access-from domain=\"*\" secure=\"false\" />\n<allow-http-request-headers-from domain=\"*\" headers=\"*\" secure=\"false\" />\n</cross-domain-policy>";
	public static String CONTENT_HTML 	= "text/html";
	public static String CONTENT_JSON	= "application/json";
	public static String CONTENT_XML	= "application/xml";
	
	public static void set200(RoutingContext rc,String message){
		Object obj = message;
		set200(rc,VertXUtil.getContentType(rc),obj,"");
	}
	
	public static void set200(RoutingContext rc,Object message){
		set200(rc,VertXUtil.getContentType(rc),message,"");
	}
	
	
	public static void set200(RoutingContext rc,Object message,String callback){
		set200(rc,VertXUtil.getContentType(rc),message,callback);
	}
	
	public static void set200(RoutingContext rc,String contentsType,Object message,String callback){
		
		String str = "";
		if(message instanceof java.lang.String){
			if(contentsType.equals("")){ contentsType = CONTENT_HTML;}
			str = CommonUtil.toString(message);
		}else{
			try{
				if(contentsType.equalsIgnoreCase(CONTENT_XML)){
					str = XmlUtil.toXml(message, true, "utf-8");
				}else{
					str = GsonUtil.toJson(message,true,"");
					if(!callback.equals("")){
						str = callback+"("+str+");";
					}
				}
			}catch(Exception e){
				logger.error("response set error : [{}]",CommonUtil.getExceptionMessage(e));
				set500(rc);
			}
		}
		
		String contentsLength = CommonUtil.toString(str.getBytes().length);
		if(rc.response().getStatusCode() != 500){
			boolean flash = !VertXUtil.getHeader(rc,"AuthFlash").equals("");
			
			if(flash){
				rc.response()
				.putHeader(HttpHeaders.CONTENT_TYPE, CONTENT_JSON)
				.putHeader(HttpHeaders.CONTENT_LENGTH, contentsLength)
				.putHeader(HttpHeaders.CACHE_CONTROL, "no-store")
				.putHeader(HttpHeaders.EXPIRES, "-1")
				.putHeader(HttpHeaders.CONNECTION, "close")
				.putHeader(HttpHeaders.SERVER, "CyrexPay")
				 .write(str)
				 .end();
			}else{
			
		        rc.response()
				.putHeader(HttpHeaders.CONTENT_TYPE, CONTENT_JSON)	
				.putHeader(HttpHeaders.CONTENT_LENGTH, contentsLength)
				.putHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store")
		        .putHeader("Pragma", "no-cache")
				.putHeader(HttpHeaders.EXPIRES, "-1")
				.putHeader(HttpHeaders.CONNECTION, "close")
				.putHeader(HttpHeaders.SERVER, "CyrexPay")
				 .write(str)
				 .end();
			}
			

		}
		if(!VertXUtil.getClientIp(rc).equals("192.168.47.71")){
			logger.info("res : [{}],[{}]\n",str,contentsType);
		}
		
		
	}
	
	public static void set400(RoutingContext rc){
		setResponse(rc, 400, "Bad Request", "Bad Request - The request contains invalid data or the data structure is invalid");	
	}
	
	public static void set401(RoutingContext rc){
		setResponse(rc, 401, "Unauthorized", "Unauthorized Transaction - Your API Key is invalid");
	}
	
	public static void set401_PK(RoutingContext rc){
		setResponse(rc, 401, "Unauthorized", "Unauthorized Transaction - Your API Key is invalid or Please use PublicKey");
	}
	
	public static void set403(RoutingContext rc){
		setResponse(rc, 403, "Forbidden", "Forbidden Error ");
	}
	
	public static void set403_PK(RoutingContext rc){
		setResponse(rc, 403, "Forbidden", "Forbidden Error - Please use SecretKey");
	}
	
	public static void set404(RoutingContext rc){
		setResponse(rc, 404, "Resource not found", "Resource not found.");
	}
	
	public static void set404EmptryLog(RoutingContext rc){
		setEmptyLogResponse(rc, 404, "Resource not found", "Resource not found. uri");
	}
	
	public static void set405(RoutingContext rc){
		setResponse(rc, 405, "Method Not Allowed", "Method Not Allowed. method:"+rc.request().method());
	}
	

	public static void set500(RoutingContext rc){
		setResponse(rc, 500, "Internal Server Error", "Internal Server Error");
	}
	
	public static void set408(RoutingContext rc){
		setEmptyLogResponse(rc, 408, "Request Timeout", "");
	}
	
	
	public static void setResponse(RoutingContext rc,int code,String statusMessage,String message){
		try{
			String resStr = "";
			String contentsType = VertXUtil.getContentType(rc);
			if(contentsType.equals("")){ contentsType = CONTENT_HTML;}
			
			HttpErrorBean error = new HttpErrorBean();
			error.code 	= code;
			error.statusMessage = statusMessage;
			error.message		= message;
			
			if(contentsType.equalsIgnoreCase(CONTENT_JSON)){
				resStr = GsonUtil.toJson(error,true,"");
			}else if(contentsType.equalsIgnoreCase(CONTENT_XML)){
				resStr =  XmlUtil.toXml(error, true, "utf-8");
			}else{
				resStr = TEMPLATE.replaceAll("STATUSMESSAGE",statusMessage).replaceAll("MESSAGE",message).replaceAll("STATUS",CommonUtil.toString(code));
			}
			
			rc.response().setStatusCode(error.code)
			.setStatusMessage(error.statusMessage)
			.putHeader(HttpHeaders.CONTENT_TYPE, contentsType)
			.end(resStr);
			logger.info("res : [{}],[{}]\n",resStr,contentsType);
		}catch(Exception e){}
		
	}
	
	
	public static void setEmptyLogResponse(RoutingContext rc,int code,String statusMessage,String message){
		try{
			String resStr = "";
			String contentsType = VertXUtil.getContentType(rc);
			if(contentsType.equals("")){ contentsType = CONTENT_HTML;}
			
			HttpErrorBean error = new HttpErrorBean();
			error.code 	= code;
			error.statusMessage = statusMessage;
			error.message		= message;
			
			if(contentsType.equalsIgnoreCase(CONTENT_JSON)){
				resStr = GsonUtil.toJson(error,true,"");
			}else if(contentsType.equalsIgnoreCase(CONTENT_XML)){
				resStr =  XmlUtil.toXml(error, true, "utf-8");
			}else{
				resStr = TEMPLATE.replaceAll("STATUSMESSAGE",statusMessage).replaceAll("MESSAGE",message).replaceAll("STATUS",CommonUtil.toString(code));
			}
			
			rc.response().setStatusCode(error.code)
			.setStatusMessage(error.statusMessage)
			.putHeader(HttpHeaders.CONTENT_TYPE, contentsType)
			.end(resStr);
		}catch(Exception e){}
		
		
	}
	
	
	public static void setCrossDomain(RoutingContext rc){
		
		
		rc.response()
		.putHeader(HttpHeaders.CONTENT_TYPE, CONTENT_XML)
		.putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,"*")
		.putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true")
		.putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,"X-Custom-Header,Authorization,Content-Type,Origin")		
		.putHeader(HttpHeaders.CONTENT_LENGTH, ""+CROSS_DOMAIN.length())
		.putHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE,"386000")
		.putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,"GET,POST,PUT,OPTIONS")
		.putHeader(HttpHeaders.CACHE_CONTROL, "no-store")
		.putHeader(HttpHeaders.EXPIRES, "-1")
		.putHeader(HttpHeaders.CONNECTION, "close")
		.putHeader(HttpHeaders.SERVER, "CyrexPay")
		 .write(CROSS_DOMAIN).end();

			
		
	}
	
	public static void setRedirect(RoutingContext rc,String url,String message){
		StringBuffer sb = new StringBuffer();
		sb.append("<html>\n");
		sb.append("<head>\n");
		sb.append("<title>CyrexPay</title>");
		sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
		sb.append("<script language=\"javascript\">	");
		sb.append("function load()	{");
		sb.append("	document.cyrexpay.submit();	");
		sb.append("}");
		sb.append("</script>");
		sb.append("</head>");
		sb.append("<body onLoad='load()' >");
		sb.append("<script language='javascript'> ");
		sb.append("document.write('<center><br/><br/><br/><br/><h3>Please do not close the window, you will be forwarded....</h3></center>');");
		sb.append("</script>	");
		sb.append("<form method='post' name='cyrexpay' id='cyrexpay' action='"+url+"'>");
		sb.append("<input type=\"hidden\" name='response' value='"+CommonUtil.URLEncode(message)+"'/>");
		sb.append("<noscript>");
		sb.append("<center><br/><br/><br/>");
		sb.append("<h3>Please click the button below to continue with your payment.</h3><br/>");
		sb.append("<input type='submit' value='Continue'>");
		sb.append("</center>");
		sb.append("</noscript>");
		sb.append("</form>");
		sb.append("</body>");
		sb.append("</html>");
		
		
		rc.response()
		.putHeader(HttpHeaders.CONTENT_TYPE, CONTENT_HTML)		
		.putHeader(HttpHeaders.CONTENT_LENGTH, ""+sb.toString().getBytes().length)
		.putHeader(HttpHeaders.CACHE_CONTROL, "no-store")
		.putHeader(HttpHeaders.EXPIRES, "-1")
		.putHeader(HttpHeaders.CONNECTION, "close")
		.putHeader(HttpHeaders.SERVER, "CyrexPay")
		 .write(sb.toString()).end();
		logger.info("\n");
		
		
	}
	
	
	

	
	public static void setMobileRedirect(RoutingContext rc,String url,String message){
		String css = "https://api.cyrexpay.com/static/o/css/api_o_m_1.0.css";
		if(CommonUtil.nToB(rc.request().absoluteURI()).toLowerCase().indexOf("dev.cyrexpay.com") > -1){
			css = css.replaceAll("api.cyrexpay.com", "dev.cyrexpay.com");
		}
		StringBuffer sb = new StringBuffer();
		sb.append("<!DOCTYPE html>\n");
		sb.append("	<html>\n");
		sb.append("	 <head>\n");
		sb.append("		<meta charset=\"utf-8\">\n");
		sb.append("		<meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no\">\n");
		sb.append("	  <title>CyrexPay</title>\n");
		sb.append("		<link rel=\"stylesheet\" type=\"text/css\" href=\""+css+"\" />\n");
		sb.append("		<script type=\"text/javascript\">\n");
		sb.append("		<!--\n");
		sb.append("			setTimeout(function(){\n");
		sb.append("				document.cyrexpay.submit();\n");
		sb.append("			}, 5000);\n");
		sb.append("			function paySubmit(){\n");
		sb.append("				document.cyrexpay.submit();\n");
		sb.append("			}\n");
		sb.append("		//-->\n");
		sb.append("		</script>\n");
		sb.append("	 </head>\n");

		sb.append("	 <body>\n");
		sb.append("		<form name=\"cyrexpay\" method=\"post\" action='"+url+"'>\n");
		sb.append("			<input type=\"hidden\" name=\"response\" value=\""+CommonUtil.URLEncode(message)+"\" />\n");
		sb.append("		</form>\n");

		sb.append("		<div id=\"cyrexpay_wrap\">\n");
		sb.append("			<div id=\"cyrexpay_view\">\n");
		sb.append("				<div class=\"content\">\n");
		sb.append("					<ul class=\"result-box-true\">\n");
		sb.append("						<li class=\"cont\">결제가 완료되었습니다.</li>\n");
		sb.append("						<li class=\"desc\">이 창은 5초후 자동으로 사라집니다.</li>\n");
		sb.append("					</ul>\n");
		sb.append("					<p class=\"paynow-btn\" id=\"payOk\"><a href=\"javascript:paySubmit();\">OK</a></p>\n");
		sb.append("					<p class=\"cyrexpay\"><a href=\"http://www.cyrexpay.com\" target=\"_blank\">CYREXPAY.COM</a></p>\n");
		sb.append("				</div>\n");
		sb.append("			</div>\n");
		sb.append("		</div>\n");

		sb.append("	 </body>\n");
		sb.append("	</html>\n");
		
		
		rc.response()
		.putHeader(HttpHeaders.CONTENT_TYPE, CONTENT_HTML)		
		.putHeader(HttpHeaders.CONTENT_LENGTH, ""+sb.toString().getBytes().length)
		.putHeader(HttpHeaders.CACHE_CONTROL, "no-store")
		.putHeader(HttpHeaders.EXPIRES, "-1")
		.putHeader(HttpHeaders.CONNECTION, "close")
		.putHeader(HttpHeaders.SERVER, "CyrexPay")
		 .write(sb.toString()).end();
		logger.info("\n");
		
		
	}
	
	
	public static void setTemplate(RoutingContext rc,String template){
		
		
		rc.response()
		.putHeader(HttpHeaders.CONTENT_TYPE, CONTENT_HTML)		
		.putHeader(HttpHeaders.CONTENT_LENGTH, ""+template.getBytes().length)
		.putHeader(HttpHeaders.CACHE_CONTROL, "no-store")
		.putHeader(HttpHeaders.EXPIRES, "-1")
		.putHeader(HttpHeaders.CONNECTION, "close")
		.putHeader(HttpHeaders.SERVER, "CyrexPay")
		 .write(template.toString()).end();
		logger.info("\n");
		
		
	}


}

