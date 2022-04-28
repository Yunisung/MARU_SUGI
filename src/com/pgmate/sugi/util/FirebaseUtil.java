package com.pgmate.sugi.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.lib.util.gson.GsonUtil;

public class FirebaseUtil {
	private static Logger logger 				= LoggerFactory.getLogger( com.pgmate.sugi.util.FirebaseUtil.class );
	
	private static final String SERVER_KEY = "AAAArZy9bZU:APA91bE8VjaTqfTAZfK1XDQpnpxtdNQyaC7GbWjgICRhnnFeD_GUggCwhoy3iM2t9iTDcXS5AYXixUvMH5iaZf-RNHZNeOAGvVGQvJ74m00032OnECKTyRGNDofPWg3a0F7Kg415vghs";
	
	
	public FirebaseUtil() {
		
	}
	
	
	public static final void initFirebase() {
	
	}
	
	public static final void sendFCM(String token, String title,String body) {
		JSONObject noti = new JSONObject();
		JSONObject json = new JSONObject();
		json.put("body", body);
		json.put("title", title);
		noti.put("notification", json);
		noti.put("to", token);
		
		sendFCM(noti.toString());
	}
	public static final void sendFCM(String body) {
		 	String urlString = "https://fcm.googleapis.com/fcm/send";

	        String line = null;

	        InputStream in = null;
	        BufferedReader reader = null;
	        HttpsURLConnection httpsConn = null;
	        try { 
	            URL url = new URL(urlString);
	            httpsConn = (HttpsURLConnection) url.openConnection();
	           
	            // Set Hostname verification 
	            httpsConn.setHostnameVerifier(new HostnameVerifier() {
					@Override
					public boolean verify(String hostname, SSLSession session) {
						return true;
					}
	            });
	           
	            //SSL setting 
	            SSLContext context = SSLContext.getInstance("TLS"); 
	            context.init(null, null, null); 
	            httpsConn.setSSLSocketFactory(context.getSocketFactory());
	            
	            // Input setting 
	            httpsConn.setDoInput(true);
	            // Output setting 
	            httpsConn.setDoOutput(true);
	            // Caches setting 
	            httpsConn.setUseCaches(false);
	            // Read Timeout Setting 
	            httpsConn.setReadTimeout(10000);
	            // Connection Timeout setting 
	            httpsConn.setConnectTimeout(10000);
	            // Method Setting(GET/POST) 
	            httpsConn.setRequestMethod("POST");
	            
	            // Header Setting 
	            httpsConn.setRequestProperty("Authorization", "key="+SERVER_KEY);
	            httpsConn.setRequestProperty("content-type", "application/json");

	            logger.info("FCM request : "+ GsonUtil.toPrettyFormat(body));
	            //write
	            OutputStreamWriter wr = new OutputStreamWriter(httpsConn.getOutputStream());
             wr.write(body);
             wr.flush();
             wr.close();
	            
	            

	            int responseCode = httpsConn.getResponseCode();
	            
	       
	            // Print response from host 
	            if (responseCode == HttpsURLConnection.HTTP_OK) {
	                // 정상 호출 200 
	                in = httpsConn.getInputStream();
	            } else {
	                // 에러 발생 
	                in = httpsConn.getErrorStream();
	            }
	            reader = new BufferedReader(new InputStreamReader(in));
	            
	            StringBuilder sb = new StringBuilder();
	            
	            while ((line = reader.readLine()) != null) {
	                sb.append(line);
	            }
	           
	            logger.info("FCM response : "+ GsonUtil.toPrettyFormat(sb.toString()));
	          
	            reader.close();
	        } catch (	              UnknownHostException e) {
	            e.printStackTrace();
	        } catch (	                MalformedURLException e) {
	        	 e.printStackTrace();
	        } catch (	                IOException e) {
	        	 e.printStackTrace();
	        } catch (	                Exception e) {
	        	 e.printStackTrace();
	        } finally {
	            if (reader != null) {
	                try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
	            }
	            if (httpsConn != null) {
	                httpsConn.disconnect();
	            }
	        }
	}
	
	
}
