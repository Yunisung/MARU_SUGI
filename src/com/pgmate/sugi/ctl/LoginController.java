package com.pgmate.sugi.ctl;

import java.util.HashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.util.WebUtils;

import com.pgmate.lib.util.gson.GsonUtil;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;
import com.pgmate.sugi.dao.UserDAO;
import com.pgmate.sugi.interceptor.SessionExclude;
import com.pgmate.sugi.session.CPSession;
import com.pgmate.sugi.util.SessionUtil;

/**
 * @author Administrator
 *
 */
@Controller
public class LoginController {
	
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.sugi.ctl.LoginController.class );
	
	@RequestMapping(value = {"/login/form"})
	@SessionExclude
    public ModelAndView loginPage() {
        return new ModelAndView("/sso/websso");
    }
	
	
	@RequestMapping(value = "/login/in", method = RequestMethod.POST)
	@SessionExclude
	public @ResponseBody Object in(HttpServletRequest request,HttpServletResponse response,@RequestParam(value="tmnId") String tmnId,
					@RequestParam(value="serial") String serial, @RequestParam(value="autoLogin", required=false) String autoLogin){
		UserDAO userDAO = new UserDAO();
		
		
		SharedMap<String,Object> tmnMap =  userDAO.getTmnLogin(tmnId, serial).getRowFirst();
		
		HashMap<String, String> map = new HashMap<String, String>();
		
		if(CommonUtil.isNullOrSpace(tmnMap.getString("tmnId"))) {
			map.put("resultCd", "N");
		}else {
			map.put("resultCd", "Y");
			//USERSESSION
			CPSession cpSession = new CPSession();
			SessionUtil.initSessionData(cpSession, tmnMap);
			//Session  생성
			SessionUtil.create(request, cpSession);
			
			if("Y".equals(autoLogin)){
				Cookie cookie = new Cookie("MARUCookie", userDAO.getAESEnc(tmnId));
				cookie.setPath("/");
				cookie.setMaxAge(60*60*24*30);
				response.addCookie(cookie);
			}


		}
		return map;
		
	}
	
	@RequestMapping(value = "/login/out", method = RequestMethod.GET)
	@SessionExclude
	public String out(HttpServletRequest request,HttpServletResponse response) {
		SessionUtil.destroy(request);
		Cookie cookie = WebUtils.getCookie(request, "MARUCookie");
		if (cookie != null){
			cookie.setPath("/");
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}

		request.setAttribute("message", "접속 세션이 종료되었습니다. 다시 로그인하여 주시기 바랍니다.");
		// request.setAttribute("redirectURL", request.getScheme()+"://"+request.getServerName());
		return "/common/redirectParent";
	}
	
	@RequestMapping(value = {"/login/expired"},produces=MediaType.APPLICATION_JSON_VALUE)
	@SessionExclude
    public String expired(HttpServletRequest request) {
		SessionUtil.destroy(request);
		return "/common/redirectParent";
    }
	
	@RequestMapping(value = {"/sessionAlive"}, method = RequestMethod.GET)
	@SessionExclude
    public @ResponseBody String sessionAlive(HttpServletRequest request) {
		if(SessionUtil.isLive(request)){
			return "MSG||ALIVE";
		}else{
			return GsonUtil.toJson("MSG||사용자 세션이 종료되었습니다.");
		}
	}
	
	@RequestMapping(value = {"/ajaxSession"}, method = RequestMethod.GET)
	@SessionExclude
    public ResponseEntity<String> ajaxSession(HttpServletRequest request) {
		if(SessionUtil.isLive(request)){
			return new ResponseEntity<String>("OK",HttpStatus.OK);
		}else{
			return new ResponseEntity<String>("접속자 세션이 종료되었습니다.",HttpStatus.UNAUTHORIZED);
		}
		
	}
}

	