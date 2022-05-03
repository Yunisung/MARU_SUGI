package com.pgmate.sugi.ctl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


import com.pgmate.sugi.bean.DirectPaymentRequest;
import com.pgmate.sugi.bean.DirectPaymentResponse;
import com.pgmate.sugi.bean.Request;
import com.pgmate.sugi.dao.SmsDAO;
import com.pgmate.lib.util.db.DBFactory;
import com.pgmate.lib.util.db.DBManager;
import com.pgmate.lib.util.gson.GsonUtil;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;


/**
 * @author Administrator
 *
 */
@Controller
public class SmsController {
	
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.sugi.ctl.SmsController.class );
	private static final String BASE_URL = "http://10.100.100.10:10002";
	
	@RequestMapping(value="/sms/{smsKey}/pay")
	public ModelAndView smsPay(HttpServletRequest request ,@PathVariable("smsKey") String smsKey) {
		SmsDAO smsDAO = new SmsDAO();
		
		SharedMap<String, Object> smsPay = smsDAO.getSmsPay(smsKey);
		request.setAttribute("baseUrl", "https://api.bkwinners.kr");
		if(smsPay != null){
			if("N".equals(smsPay.getString("status"))){
				smsPay.put("payerTel", smsDAO.getAESDec(smsPay.getString("payerTel")));
				request.setAttribute("mcht", smsPay);
				return new ModelAndView("/sms/smsPay");
			}else{
				request.setAttribute("bill", smsDAO.getSmsBill(smsKey));
				return new ModelAndView("/sms/smsBill");
			}
		} else {
			return new ModelAndView("/sms/error");
		}
		
	}

	@RequestMapping(value="/sms/smsPayWebHook", method = RequestMethod.POST, consumes = {"application/x-www-form-urlencoded"})
	public @ResponseBody Object smsPayWebHook(HttpServletRequest request) {
		
		
		String jsonStr = CommonUtil.nToB(request.getParameter("response"));
		
		Request req =  (Request) GsonUtil.fromJson(jsonStr, Request.class);
		String resultCd = req.result.resultCd;
		String trxId = req.pay.trxId;
		String smsKey = req.pay.udf1;
		
		SmsDAO smsDAO = new SmsDAO();
		
		HashMap<String, String> map = new HashMap<String, String>();
		if("0000".equals(resultCd)){
			map.put("resData", "result=0000");
			smsDAO.smsPayComplete(trxId,smsKey);
		}else {
			map.put("resData", "result=9999");
		}
		return map;
	}

	@RequestMapping(value="/sms/smsPayComplete")
	public ModelAndView smsPayComplete(HttpServletRequest request){
		String jsonStr = CommonUtil.nToB(request.getParameter("result"));
		SharedMap<String, Object> map = new SharedMap<String, Object>();
		Request req =  (Request) GsonUtil.fromJson(jsonStr, Request.class);
		
		String create = req.result.create;
		map.put("regDay", create.substring(0, 8));
		map.put("regTime", create.substring(8));
		map.put("trxId", req.pay.trxId);
		map.put("authCd", req.pay.authCd);
		map.put("amount", req.pay.amount);
		map.put("bin", req.pay.card.bin);
		map.put("last4", req.pay.card.last4);
		
		request.setAttribute("bill", map);
		return new ModelAndView("/sms/smsComplete");
		
	}
	
	
	@RequestMapping(value="/sms/pay", method = RequestMethod.POST, consumes = {"application/x-www-form-urlencoded"})
	public @ResponseBody Object smsPay(HttpServletRequest request) {
		logger.info("========== /sms/pay START ===========  : ");
		String payKey = request.getParameter("payKey");
		String smsKey = request.getParameter("smsKey");
		String trackId = getTrackId();
		String amount = request.getParameter("amount");
		String payerName = request.getParameter("payerName");
		String payerEmail = request.getParameter("payerEmail");
		String payerTel = request.getParameter("payerTel");
		
		String cardNumber = request.getParameter("cardNumber");
		String expiry = request.getParameter("expiry");
		String installment = request.getParameter("installment");
		
		String cardAuth = request.getParameter("cardAuth");
		String authPw = request.getParameter("authPw");
		String authDob = request.getParameter("authDob");
		
		logger.info("========== /sms/pay 2 ===========  : ");
		

		DirectPaymentRequest DPrequest = new DirectPaymentRequest();
		logger.info("========== /sms/pay 3 ===========  : ");
		DPrequest.pay.put("payRoute", "ONTR");
		DPrequest.pay.put("trxType", "ONTR");
		DPrequest.pay.put("trackId", trackId);
		DPrequest.pay.put("amount", amount);
		DPrequest.pay.put("payerName", payerName);
		DPrequest.pay.put("payerEmail", payerEmail);
		DPrequest.pay.put("payerTel", payerTel);
		DPrequest.pay.put("udf1", "");
		DPrequest.pay.put("udf2", "");
		
		logger.info("========== /sms/pay 4 ===========  : ");
	    ArrayList<Object> products = new ArrayList<>();
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("name", "");
        dataMap.put("qty", 1);
        dataMap.put("price", amount);
        dataMap.put("desc", "");
        products.add(dataMap);
        DPrequest.pay.put("products", products);
		
        logger.info("========== /sms/pay 5 ===========  : ");
        final HashMap<String, Object> cardMap = new HashMap<>();
        cardMap.put("number", cardNumber);
        cardMap.put("expiry", expiry);
        cardMap.put("installment", installment);
        DPrequest.pay.put("card", cardMap);

        HashMap<String, Object> metadata = new HashMap<>();
        if(!CommonUtil.isNullOrSpace(cardAuth) && cardAuth.equals("true")) {
        	logger.info("========== /sms/pay 6 ===========  : ");
        	metadata.put("cardAuth", cardAuth);
        	metadata.put("authPw", authPw);
        	metadata.put("authDob", authDob);
		}

        logger.info("========== /sms/pay 7 ===========  : ");
        DPrequest.pay.put("metadata", metadata);
        
        //API SEND
		DirectPaymentResponse DPresponse = sendPaymentApi("/api/pay", DPrequest, payKey);
		logger.info("========== /sms/pay 8 ===========  : " +DPrequest);
		
        HashMap<String, Object> map = new HashMap<>();
		
		if(DPresponse!=null && !DPresponse.result.isNullOrSpace("resultCd")) {
			String resultCd = DPresponse.result.getString("resultCd");
			String resultMsg = DPresponse.result.getString("resultMsg");
			
			map.put("resultCd",resultCd);
			map.put("resultMsg",resultMsg);
			map.put("result", DPresponse.result);
			map.put("pay",DPresponse.pay);
//			map.put("widget", DPresponse.widget);
			
			if(resultCd.equals("0000")) {
				//결제성공
				String trxId = DPresponse.pay.getString("trxId");
				
				if(smsKey!=null && smsKey.length()>0) {
					SmsDAO smsDAO = new SmsDAO();
					smsDAO.smsPayComplete(trxId,smsKey);
				}
				
//				//분할결제 결과테이블 업데이트.
//				GCDAO gcDAO = new GCDAO();
//				gcDAO.updateResPay(trackId,trxId);
//				
//				SharedMap<String, Object> idInfo = gcDAO.getIdInfo(trackId);
//				FirebaseUtil.sendFCM(idInfo.getString("pushToken"), "알림", payerName+"("+payerTel+")고객이 "+String.format("%,d", Integer.parseInt(amount))+"원 을 결제하였습니다.");
				
			}
		}
		
		
		return map;
	}
	
	private DirectPaymentResponse sendPaymentApi(String sendurl, DirectPaymentRequest request, String payKey) {
		logger.info("========== sendPaymentApi ===========  : ");
		
		DirectPaymentResponse response = null;
	
		 //String urlString = UNIT.API_SERVER_URL +"/api/pay";
		String urlString = BASE_URL + sendurl; 
		logger.info("[ API URL = " +urlString +" ]");
		logger.info("sugi request url : "+ urlString);
		 
	        String line = null;

	        InputStream in = null;
	        BufferedReader reader = null;
	        //HttpURLConnection httpsConn = null;
	        HttpsURLConnection httpsConn = null;
	        try { // Get HTTPS URL connection
	        	logger.info("========== sendPaymentApi ===========  : 1");
	            URL url = new URL(urlString);
	            logger.info("========== sendPaymentApi ===========  : url " +url);
	            httpsConn = (HttpsURLConnection) url.openConnection();
	            logger.info("========== sendPaymentApi ===========  : httpsConn " +httpsConn);
	           
	            
	            // Set Hostname verification 
	            logger.info("========== sendPaymentApi ===========  : 2");
	            httpsConn = (HttpsURLConnection) url.openConnection();
	            httpsConn.setHostnameVerifier(new HostnameVerifier() {
					@Override
					public boolean verify(String hostname, SSLSession session) {
						return true;
					}
	            });
	           
	            logger.info("========== sendPaymentApi ===========  : 3");
	            //SSL setting 
	            SSLContext context = SSLContext.getInstance("TLS"); 
	            context.init(null, null, null); 
	            httpsConn.setSSLSocketFactory(context.getSocketFactory());
	            
	            logger.info("========== sendPaymentApi ===========  : 4");
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
	            
	            logger.info("========== sendPaymentApi ===========  : 5");
	            // Header Setting 
	            httpsConn.setRequestProperty("Authorization", payKey);
	            httpsConn.setRequestProperty("content-type", "application/json");

	            logger.info("========== sendPaymentApi ===========  : 6");
	            String requestString = GsonUtil.toJson(request);
	            logger.info("sugi request : "+ GsonUtil.toPrettyFormat(requestString));
	            //write
	            OutputStreamWriter wr = new OutputStreamWriter(httpsConn.getOutputStream());
                wr.write(requestString);
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
	            
	            
	            logger.info("sugi response : "+ GsonUtil.toPrettyFormat(sb.toString()));
	            response = (DirectPaymentResponse) GsonUtil.fromJson(sb.toString(), DirectPaymentResponse.class);
	          
	            reader.close();
	        } catch (	              UnknownHostException e) {
	            e.printStackTrace();
	        } catch (	                MalformedURLException e) {
	        	 e.printStackTrace();
	        } catch (	                IOException e) {
	        	 e.printStackTrace();
	        } catch (	                Throwable e) {
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
	        
	        
	        return response;
	}
	
	public String getTrackId() {
		return "SMS_" + getFunction("FN_NEXTVAL2", "TRACKID");
	}
	
	public String getFunction(String function, String value) {
		String returnVal = "";
		String query = "SELECT " + function + "(?) as val";

		DBManager db = null;
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rset = null;

		try {
			db = DBFactory.getInstance();
			conn = db.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, value);
			rset = pstmt.executeQuery();

			while (rset.next()) {
				returnVal = rset.getString(1);
			}
			conn.commit();
		} catch (Exception t) {
			logger.debug("sql error : {}, query : {}", t.getMessage(), query);
		} finally {
			db.close(conn, pstmt, rset);
		}
		return returnVal;
	}
	
}

	