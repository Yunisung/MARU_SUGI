package com.pgmate.sugi.util;

import javax.servlet.http.HttpServletRequest;

import com.pgmate.lib.util.lang.CommonUtil;

public class IPUtil {
  
  public static String getIp(HttpServletRequest request) {
    String ip = request.getHeader("X-FORWARDED-FOR");
		if (ip == null || ip.isEmpty()) ip = CommonUtil.nToB(request.getHeader("X-Real-IP"));
    if (ip == null || ip.isEmpty()) ip = request.getRemoteAddr();
    
    return ip;
  }

 }