package com.pgmate.sugi.ctl;

import java.util.HashMap;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.pgmate.lib.util.gson.GsonUtil;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;
import com.pgmate.sugi.dao.SmsDAO;
import com.pgmate.sugi.dao.UserDAO;
import com.pgmate.sugi.interceptor.SessionExclude;
import com.pgmate.sugi.session.CPSession;
import com.pgmate.sugi.util.SessionUtil;

/**
 * @author Administrator
 *
 */
@Controller
public class MchtController {
	
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.sugi.ctl.MchtController.class );
	//private static final String BASE_URL = "https://devsugi.bkwinners.kr";
	private static final String BASE_URL = "http://127.0.0.1";
	
	@RequestMapping(value="/mcht/order")
	public ModelAndView order(HttpServletRequest request) {
		logger.debug("---------------- MchtController order ------------------");

		CPSession cpSession = SessionUtil.get(request);
		UserDAO userDAO = new UserDAO();
		String name = "";
		SharedMap<String, Object> sharedMap = userDAO.getTmnDtl(cpSession.getTmnId()).getRowFirst();
		
		if(!CommonUtil.isNullOrSpace(sharedMap.getString("name"))) {
			name = sharedMap.getString("name");
		}else {
			name = cpSession.getMchtName();
		}
		request.setAttribute("TMNNAME", name);
		request.setAttribute("baseUrl", BASE_URL);
		
		SharedMap<String, Object> accntMap = userDAO.getSettleAccnt(cpSession.getTmnId()).getRowFirst();
		request.setAttribute("ACCNTMAP", accntMap);
		return new ModelAndView("/mcht/order");
	}

		
	@RequestMapping(value="/mcht/smsPay")
	@SessionExclude
	public @ResponseBody Object smsPay(HttpServletRequest request){
		
		SharedMap<String, Object> smsPayMap = new SharedMap<String, Object>(); 
		
		SmsDAO smsDAO = new SmsDAO(); 
		
		smsPayMap.put("payKey", CommonUtil.nToB(request.getParameter("payKey")));
		
		smsPayMap.put("name", CommonUtil.nToB(request.getParameter("mchtName")));
		smsPayMap.put("amount", CommonUtil.nToB(request.getParameter("amount")));
		smsPayMap.put("products", CommonUtil.nToB(request.getParameter("products")));
		smsPayMap.put("mchtId", CommonUtil.nToB(request.getParameter("mchtId")));
		smsPayMap.put("payerName", CommonUtil.nToB(request.getParameter("payerName")));
		smsPayMap.put("payerEmail", CommonUtil.nToB(request.getParameter("payerEmail")));
		smsPayMap.put("payerTel", smsDAO.getAESEnc(CommonUtil.nToB(request.getParameter("payerTel"))));
		
		String smsKey = "";
		int count = 0;
		do {
			smsKey = makePayUri(smsPayMap.getString("payKey"));
			count = smsDAO.checkSmsKey(smsKey);
		}while(count > 0);
		
		smsPayMap.put("smsKey", smsKey);
		
		HashMap<String, String> map = new HashMap<String, String>();
		if(smsDAO.insertSMSPay(smsPayMap)) {
			map.put("result", "Y");
			map.put("smsKey", smsKey);
		}else {
			map.put("result", "N");
		}
		
		
		return map;
	}
	
	public String makePayUri(String payKey){
		
		// 현재시간을 이용한 임의의 문자 생성
		Random rnd = new Random();
		rnd.setSeed(System.currentTimeMillis());
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<=14;i++){
			if(rnd.nextBoolean()){
		        sb.append((char)((int)(rnd.nextInt(26))+97));
		    }else{
		        sb.append((rnd.nextInt(10)));
		    }
		}
		return sb.toString();
	}
}

	