package com.pgmate.sugi.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.lib.util.map.SharedCacheMap;
import com.pgmate.lib.util.map.SharedMap;

public class WebCache {
	private static Logger logger = LoggerFactory.getLogger(com.pgmate.sugi.util.WebCache.class);
	
	public static SharedCacheMap cacheMap			= new SharedCacheMap(5);
	
	public void setSMSKey(String userId, String number){
		if(cacheMap.containsKey(userId)) {
			cacheMap.delete(userId);
		}
		SharedMap<String, Object> reqMap = new SharedMap<>();
		reqMap.put("number", number);
		cacheMap.put(userId, reqMap);
	}

	public String getSMSKey(String userId){
		SharedMap<String, Object> resMap = new SharedMap<>();
		if(cacheMap.containsKey(userId)) {
			resMap = cacheMap.get(userId);
		} else {
			logger.debug("WEB CACHE NOT FOUND KEY = {}", userId);
		}
		return resMap.getString("number");
	}
	
}
