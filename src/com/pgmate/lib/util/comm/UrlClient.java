package com.pgmate.lib.util.comm;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.pgmate.lib.util.cipher.Base64;
import com.pgmate.lib.util.lang.CommonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Administrator
 *
 */
public class UrlClient {

	private Logger logger = LoggerFactory.getLogger( getClass() );
	
	private String host 		= "";
	private String method 		= "GET";
	private int connectTimeout	= 0;
	private int readTimeout		= 0;
	private boolean input		= true;
	private boolean output		= true;
	private boolean useCaches	= false;
	private int HTTP_CODE		= -1;
	
	private Map<String,String> map = null;
	
	
	
	/**
	 * 아래와 같이 전달하면 됩니다.
	 * host = http://www.trustmate.net 
	 * method = "GET","POST","PUT","DELETE"
	 * contentType = "text/plain"
	 * @param host
	 * @param method
	 * @param contentType
	 */
	public UrlClient(String host,String method,String contentType){
		this.host 	= host;
		this.method = method.toUpperCase();
		this.map = new HashMap<String,String>();
		if(!contentType.equals("")){
			this.map.put("Content-Type",contentType);
		}
	}
	
	/**
	 * URL 로의 connect Timeout, 및 readTimeout 을 설정한다.
	 * @param connectTimeout
	 * @param readTimeout
	 */
	public void setTimeout(int connectTimeout,int readTimeout){
		this.connectTimeout = connectTimeout;
		this.readTimeout 	= readTimeout;
	}
	
	/**
	 * GET 을 제외한 INPUT, OUTPUT  여부를 설정한다.
	 * 기본값은 TRUE,TRUE로 설정된다.
	 * @param input
	 * @param output
	 */
	public void setDoInputOutput(boolean input,boolean output){
		this.input	= input;
		this.output = output;
	}
	
	
	/**
	 * System.setProperty("https.protocols", "SSLv3");, System.setProperty("javax.net.ssl.trustStore", certPath);
	 * @param property
	 * @param value
	 */
	public void setHttpsProperty(String key,String value){
		System.setProperty(key, value);
	}
	
	
	public void setUseCache(boolean useCaches){
		this.useCaches = useCaches;
	}
	
	
	/**
	 * set Authentication 
	 * @param userName
	 * @param password
	 */
	public void setAuthorization(String userName,String password){
		String userCredentials = userName+":"+password;
		String basicAuth = "Basic " + Base64.encodeString(userCredentials);
		map.put("Authorization", basicAuth);
	}
	
	/**
	 * 추가 RequestProperty 에 대해서 MAP 을 통하여 전달한다.
	 * ("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,;q=0.8
	 * ("Accept-Charset", "windows-949,utf-8;q=0.7,*;q=0.3
	 * ("Accept-Encoding", "gzip,deflate,sdch");
	 * ("Accept-Language", "ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4");
	 * ("Connection", "keep-alive");
	 * ("Host", #URL HOST#);
	 * ("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.7 (KHTML, 
	 * @param map
	 */
	public void setRequestProperty(Map<String,String> map){
		this.map.putAll(map);
	}
	
	
	public String getRequestProperty(){
		return map.toString();
	}
	
	
	/**
	 * Request 는 QueryString 형태로 전달한다.
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String connect(String request) throws Exception {
		initProtocol(request);
		
		String response = "";
		URL url 	= null;
		HttpURLConnection conn	= null;
		try{
			url = new URL(host);
			conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod(method);
			conn.setConnectTimeout(connectTimeout);
			conn.setReadTimeout(readTimeout);
			conn.setUseCaches(useCaches);
			
			map.put("Content-Length", CommonUtil.toString(request.getBytes().length));
			setRequestProperty(conn);
			
			conn.setDoOutput(output);
			conn.setDoInput(input);
			if(output){
				request(conn,request);
			}
			HTTP_CODE = conn.getResponseCode();
			
			
			if(input){
				response = response(conn);
			}
			
			logger.debug("HTTP_CODE : {}, HTTP_MESSAGE : {}",HTTP_CODE,conn.getResponseMessage());
		
		}catch(Exception e){
			logger.debug("url connect error HTTP_CODE :{} , message : {}",HTTP_CODE,CommonUtil.getExceptionMessage(e));
			throw e;
		}finally{
			conn.disconnect();
		}
		return response;
		
	}
	
	
	
	public byte[] connect2(String request) throws Exception {
		initProtocol(request);
		
		byte[] response = null ;
		URL url 	= null;
		HttpURLConnection conn	= null;
		try{
			url = new URL(host);
			conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod(method);
			conn.setConnectTimeout(connectTimeout);
			conn.setReadTimeout(readTimeout);
			conn.setUseCaches(useCaches);
			
			map.put("Content-Length", CommonUtil.toString(request.getBytes().length));
			setRequestProperty(conn);
			
			conn.setDoOutput(output);
			conn.setDoInput(input);
			if(output){
				request(conn,request);
			}
			HTTP_CODE = conn.getResponseCode();
			
			
			if(input){
				response = response2(conn);
			}
			
			logger.debug("HTTP_CODE : {}, HTTP_MESSAGE : {}",HTTP_CODE,conn.getResponseMessage());
		
		}catch(Exception e){
			logger.debug("url connect error HTTP_CODE :{} , message : {}",HTTP_CODE,CommonUtil.getExceptionMessage(e));
			throw e;
		}finally{
			conn.disconnect();
		}
		return response;
		
	}
	
	/**
	 * HTTP 접속에 대한 최종 HTTP_RESPONSE_CODE 를 반환한다.
	 * @return
	 */
	public int getHttpCode(){
		return HTTP_CODE;
	}
	
	
	
	private void initProtocol(String request){
		String protocol = host.toLowerCase();
		if(protocol.startsWith("http://") || protocol.startsWith("https://") ){
		}else{
			host = "http://"+host;
			logger.debug(" add protocol : {} ",host);
		}
		
		if(method.equals("GET")){
			if(!CommonUtil.isNullOrSpace(request)){
				host = host+"?"+request;
			}
		}
	}
	


	
	private void setRequestProperty(HttpURLConnection conn){
		
		if(map.size() !=0){
			for (Entry<String, String> entry : map.entrySet()) {
				conn.setRequestProperty(entry.getKey(),entry.getValue());
	        }
		}
	}
	

	private void request(HttpURLConnection conn,String request)throws Exception{

		DataOutputStream out = null;
		try{
			out =new DataOutputStream(conn.getOutputStream());
			out.writeBytes(request);
			out.flush();
			out.close();
		}catch(Exception e){
			logger.debug("url request error {}",CommonUtil.getExceptionMessage(e));
			throw new Exception(e);
		}
	}
	
	private String response(HttpURLConnection conn)throws Exception{
		StringBuilder sb = new StringBuilder();
		BufferedReader in 	= null;
	    try{
	    	in = new BufferedReader( new InputStreamReader( conn.getInputStream()));
	    	String recv 	= "";
	        while ((recv = in.readLine()) != null) {
	        	sb.append(recv+"\n");
	        }
	        in.close();
	    }catch (IOException e) {
	    	logger.debug("url response error {}",CommonUtil.getExceptionMessage(e));
	    	throw new Exception(e);
	    }
	    return sb.toString();
	}
	
	
	private byte[] response2(HttpURLConnection conn)throws Exception{
		ByteArrayOutputStream ous = null;
		InputStream ios = null;
		
		try {
			byte[] buffer = new byte[4096];
			ous = new ByteArrayOutputStream();
			ios = conn.getInputStream();
			int read = 0;
		    while ( (read = ios.read(buffer)) != -1 ) {
		    	ous.write(buffer, 0, read);
		    }
		    
		}catch(IOException e){
			logger.debug("error : {}",CommonUtil.getExceptionMessage(e));
			throw new IOException(e);
		}finally {
			ous.close();
			ios.close();
		}
		return ous.toByteArray();
	}
	
	
	
	
	
	
	
	
	
}
