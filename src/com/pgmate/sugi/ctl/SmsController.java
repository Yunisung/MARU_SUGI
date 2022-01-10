package com.pgmate.sugi.ctl;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.pgmate.lib.util.gson.GsonUtil;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;
import com.pgmate.sugi.bean.Request;
import com.pgmate.sugi.dao.SmsDAO;

/**
 * @author Administrator
 *
 */
@Controller
public class SmsController {
	
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.sugi.ctl.SmsController.class );
	private static final String BASE_URL = "https://svcsugi.mtouch.com";
	
	@RequestMapping(value="/sms/{smsKey}/pay")
	public ModelAndView smsPay(HttpServletRequest request ,@PathVariable("smsKey") String smsKey) {
		SmsDAO smsDAO = new SmsDAO();
		
		SharedMap<String, Object> smsPay = smsDAO.getSmsPay(smsKey);
		request.setAttribute("baseUrl", "https://svcsugi.mtouch.com");
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
}

	