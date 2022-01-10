package com.pgmate.lib.util.map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Administrator
 *
 */
public class SharedCacheMap extends TimeoutCache {
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.lib.util.map.SharedCacheMap.class );
	
	public SharedCacheMap(int expireInMinutes) {
		super(expireInMinutes);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void processAfterExpire(String key, SharedMap<String, Object> value) {
		logger.info("cache timeout : key : {} ",key);
	}

}
