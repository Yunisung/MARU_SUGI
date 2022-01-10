package com.pgmate.sugi.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.lib.util.map.SharedMap;
import com.pgmate.sugi.dao.UserDAO;
import com.pgmate.sugi.model.ajax.CPRequest;
import com.pgmate.sugi.model.ajax.Data;
import com.pgmate.sugi.session.CPSession;

/**
 * @author Administrator
 *
 */
public class SessionUtil {

	private static Logger logger = LoggerFactory.getLogger( com.pgmate.sugi.util.SessionUtil.class );
	
	public static boolean isLive(HttpServletRequest request){
		if(request.getSession().getAttribute(CPUtil.CP_SESSION) == null){
			return false;
		}else{
			return true;
		}
	}
	
	public static void create(HttpServletRequest request,CPSession cpSession){
		HttpSession session = request.getSession(true);
		session.setMaxInactiveInterval(CPUtil.CP_SESSION_TIMEOUT);
		setAttribute(request,CPUtil.CP_SESSION,cpSession);
	}
	

	
	public static void setAttribute(HttpServletRequest request,String name,Object object){
		request.getSession().setAttribute(name, object);   
	}
	
	
	public static void destroy(HttpServletRequest request){
		request.getSession().removeAttribute(CPUtil.CP_SESSION);
		request.getSession().invalidate();
	}
	
	
	public static CPSession get(HttpServletRequest request){
		if(request.getSession().getAttribute(CPUtil.CP_SESSION) == null){
			return null;
		}else{
			return (CPSession)request.getSession().getAttribute(CPUtil.CP_SESSION);
		}
	}
	

	
	
	public static String toString(HttpServletRequest request){
		StringBuilder sb = new StringBuilder();
		sb.append("--- SESSION ---\n");
		sb.append("ID          = "+request.getSession().getId());
		sb.append("CreateTime  = "+request.getSession().getCreationTime());
		sb.append("LastAccess  = "+request.getSession().getLastAccessedTime());
		sb.append("MaxInterval = "+request.getSession().getMaxInactiveInterval());
		Enumeration<String> attr = request.getSession().getAttributeNames();
		int i=0;
		for(String name : Collections.list(attr)){
			sb.append("attr["+i+"] ="+name);
		}
		return sb.toString();
	}
	
	
	public static void initSessionData(CPSession cpSession, SharedMap<String,Object> tmnMap) {
		cpSession.setMchtId(tmnMap.getString("mchtId"));
		cpSession.setMchtName(tmnMap.getString("mchtNick"));
		cpSession.setTmnId(tmnMap.getString("tmnId"));
		cpSession.setSerial(tmnMap.getString("serial"));
		cpSession.setPayKey(tmnMap.getString("payKey"));
		
	
	}
}
