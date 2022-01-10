package com.pgmate.sugi.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.sugi.session.CPSession;
import com.pgmate.sugi.util.CPUtil;
import com.pgmate.sugi.util.SessionUtil;

/**
 * @author Administrator
 *
 */
public class SessionInterceptor extends HandlerInterceptorAdapter{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.sugi.interceptor.SessionInterceptor.class );
	

	@Override
    public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
		if(CPUtil.CP_DEBUG){
			logger.info("==========    START    =========");
			logger.info("URI : {} , {}",request.getRequestURI(),request.getMethod());
			request.setAttribute("ServletStartTime", System.currentTimeMillis());
		}
		logger.info("{},{},{}",request.getRequestURI(),request.getMethod(),CommonUtil.nToB(request.getHeader("X-Real-IP")));
		
		
		SessionExclude exclude = null;
		
		if (handler instanceof HandlerMethod){
			exclude = ((HandlerMethod) handler).getMethodAnnotation(SessionExclude.class);
		}else{
			//logger.info("HANDLER : {}",handler.getClass());
			//org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler
		}
		
		if(!"/check".equals(request.getRequestURI())) {
			if(exclude == null){
				String contentType = CommonUtil.nToB(request.getContentType()).toLowerCase();
				if(!SessionUtil.isLive(request)){
					logger.debug("SESSION IS NULL : {}",contentType);
					if(CPUtil.CP_DEV_SESSION){
						logger.debug("DEVELOP SESSION ");
						response.sendRedirect("/dev");
						return false;
					}
					
					//AJAX 요청의 경우 
					if(CommonUtil.nToB(request.getHeader("X-Requested-With")).equals("XMLHttpRequest")){
						response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"사용자 세션이 종료되었습니다.");
						logger.debug("AJAX SESSION EXPIRED : ");
						return false;
					}
					if(contentType.endsWith("html")){
						response.sendRedirect("/login/form");
					}else{
						response.sendRedirect("/login/expired");
					}
					
					return false;
				}else{
					/**
					CPSession cpSession = SessionUtil.get(request);
					if(cpSession.getGrade().equals("가맹점") || cpSession.getGrade().equals("영업사원")){
						
					}else{
						DAO dao = new DAO();
						dao.setDebug(CPUtil.CP_DEBUG);
						logger.info("{},{},{}",request.getRequestURI(),SessionUtil.getUserId(request),SessionUtil.getParentId(request));
						dao.update("INSERT INTO PG_USER_TODO (id,uri,todo,regDay) VALUES ('"+SessionUtil.getUserId(request)+"','"+request.getRequestURI()+"','접속',DATE_FORMAT(now(),'%Y%m%d'))");
					}
					**/
				}
			}
		}
		
        return true;
    }
 
    @Override
    public void postHandle(HttpServletRequest request,HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    	if(CPUtil.CP_DEBUG){
    		logger.info("==========    END SF  =========");
    	}
    }
 
    @Override
    public void afterCompletion(HttpServletRequest request,HttpServletResponse response, Object handler, Exception ex) throws Exception {
    	if(CPUtil.CP_DEBUG){
	    	long startTime = CommonUtil.parseLong(request.getAttribute("ServletStartTime"));
	    	logger.info("ElapsedTime : {}msec",(long)(System.currentTimeMillis()-startTime));
	    	if(ex != null){
	    		logger.debug("EXCEPTION STATUS : {}",response.getStatus());
	    	}
	    	logger.info("==========    END      =========");
    	}
    }
	

}
