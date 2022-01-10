package com.pgmate.sugi.ctl;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;
import com.pgmate.sugi.dao.CPDAO;
import com.pgmate.sugi.dao.UserDAO;
import com.pgmate.sugi.interceptor.SessionExclude;
import com.pgmate.sugi.session.CPSession;
import com.pgmate.sugi.util.SessionUtil;

/**
 * @author Administrator
 *
 */
@Controller
public class InitController {

	private static Logger logger = LoggerFactory.getLogger(com.pgmate.sugi.ctl.InitController.class);

	@RequestMapping(value = { "/init" })
	@SessionExclude
	public String init(HttpServletRequest request) {
		
		if (!SessionUtil.isLive(request)) {
			Cookie loginCookie = WebUtils.getCookie(request, "MARUCookie");
			if(loginCookie != null) {
				String tmnId = new CPDAO().getAESDec(loginCookie.getValue());
				
				UserDAO userDAO = new UserDAO();
				SharedMap<String,Object> tmnMap =  userDAO.getTmn(tmnId).getRowFirst();
				if(!tmnMap.isNullOrSpace(("tmnId"))) {
					//USERSESSION
					CPSession cpSession = new CPSession();
					SessionUtil.initSessionData(cpSession, tmnMap);
					//Session  생성
					SessionUtil.create(request, cpSession);
					
					return "redirect:/mcht/order";
				}else {
					return "/sso/websso";
				}
				
			}else {
				return "/sso/websso";
			}
		} else {
			return "redirect:/mcht/order";
		}
	
	}
}
