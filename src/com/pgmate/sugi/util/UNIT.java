package com.pgmate.sugi.util;

import com.pgmate.lib.util.map.SharedCacheMap;


/**
 * @author Administrator
 *
 */
public class UNIT {

	/**
	 * 
	 */
	public UNIT() {
		// TODO Auto-generated constructor stub
	}
	
	
	public static final String PAY_HOST_LIVE 				= "wapi.pay-sharp.com";
	
	public static final String DATE_FORMAT 					= "yyyyMMddHHmmss";
	public static final String PATH							= "/";
	
	public static final int TOKEN_EXPIRE_MINUTE				= 20;
		
	public static final String HTTP_METHOD_GET				= "GET";
	public static final String HTTP_METHOD_POST				= "POST";
	public static final String HTTP_METHOD_HEAD				= "HEAD";
	public static final String HTTP_METHOD_PUT				= "PUT";
	public static final String HTTP_METHOD_TRACE			= "TRACE";
	public static final String HTTP_METHOD_DELETE			= "OPTIONS";
	
	public static final String[] ALLOW_METHOD				= new String[]{HTTP_METHOD_GET,HTTP_METHOD_POST,HTTP_METHOD_PUT};
	

	public static final String API_ECHO	 					= "/wapi/echo";
	public static final String API_USER		 				= "/wapi/user";				//개인사용자 관리 GET/POST/PUT
	public static final String API_MCHT		 				= "/wapi/mcht";				//사업자사용자관리 GET/POST/PUT
	public static final String API_STATUS	 				= "/wapi/status";			//STATUS 변경 PUT
	public static final String API_ACCNT					= "/wapi/accnt";			//환불계좌관리 GET/POST/PUT
	public static final String API_VACCNT					= "/wapi/vaccnt";			//입금 가상계좌 관리 GET
	public static final String API_BANK_TRANSFER			= "/wapi/bank/transfer";	//은행으로 이체 	GET/POST
	public static final String API_WALLET_TRANSFER			= "/wapi/wallet/transfer";	//월렛간 이체		GET/POST
	public static final String API_WALLET_REFUND			= "/wapi/wallet/refund";	//월렛간 환불		GET/POST
	public static final String API_WALLET_BALANCE			= "/wapi/wallet/balance";	//월렛간 잔액 조회	GET
	public static final String API_WALLET_CHARGE			= "/wapi/wallet/charge";	//월렛 카드충전	GET 
	public static final String API_WEBHOOKS					= "/wapi/webhooks";
	public static final String API_WEBHOOKS_CARD			= "/wapi/webhooks/card";
	public static final String API_WEBHOOKS_CARD_LINK		= "/wapi/webhooks/chargeLink";
	public static final String API_WEBHOOKS_VACCNT			= "/wapi/webhooks/vaccnt";
	
	
		
	public static final String[] IGNORE_AUTHRORISATION		= {"webhooks"};	
	
	
	public static final String ROUTE_ROOT					= "/";
	public static final String ROUTE_CROSSDOMAIN 			= "/crossdomain.xml";
	public static final String ROUTE_API					= "/wapi/*";
	public static final String ROUTE_API_WEBHOOKS			= "/wapi/webhooks/*";
	public static final String ROUTE_NOT_FOUND				= "/*";
	public static final String[] ROUTE_IGNORE				= {"robots","sitemap"};
	

	public static final String URI							= "uri";
	public static final String HOST							= "host";
	public static final String METHOD						= "method";           
	public static final String REMOTEIP						= "remoteIp";         
	public static final String CONTENTTYPE					= "contentType";      
	public static final String PAYLOAD						= "payLoad";          
	public static final String USERAGENT					= "userAgent";        
	public static final String ACCEPTLANGUAGE				= "acceptLanguage";   
	public static final String HTTPHEADER					= "header";           
	public static final String REQUEST						= "request";          
	public static final String KEYINITIAL					= "pk_";
	public static final String PTNMAP						= "ptnMap";
	public static final String PTNID						= "ptnId";
	public static final String PTNMNGMAP					= "ptnMngMap";
	public static final String RESPONSE						= "response";
	public static final String REQUEST_TYPE					= "requestType";    
	public static final String DIRECT						= "direct";    
	public static final String ROUTEURL						= "routeUrl";
	public static final String TRX_ID						= "trxId";
	public static final String CAPTURE_ID					= "capId";
	public static final String REG_DATE						= "regDate";
	public static final String TRACKID						= "trackId";
	
	public static final String RESPONSE_TYPE				= "responseType";
	public static final String RESPONSE_DEFAULT				= "default";
	public static final String RESPONSE_REDIRECT_MOBILE		= "redirectMobile";
	public static final String RESPONSE_REDIRECT_WEB		= "redirectWeb";
	public static final String RESPONSE_REDIRECT_URL		= "redirectUrl";
	
	
	public static final String RUNTIME_ENV					= "RUNTIME_ENV";
	public static final String RUNTIME_ENV_LIVE				= "LIVE";
	public static final String RUNTIME_ENV_DEMO				= "DEMO";
	
	public static final String ENCRYPT_KEY					= "696d697373796f7568616e6765656e61";
	
	
	public static final double VAT							= 0.1;
	  
	public static  SharedCacheMap cacheMap					= new SharedCacheMap(10);
	public static  SharedCacheMap htmlMap					= new SharedCacheMap(20);
	
	
	
}
