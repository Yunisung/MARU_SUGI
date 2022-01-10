package com.pgmate.lib.util.map;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalCause;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

/**
 * @author Administrator
 *
 */
public abstract class TimeoutCache {

	private LoadingCache<String, SharedMap<String,Object>> cache = null;

	public TimeoutCache(int expireInMinutes) {
		init(expireInMinutes);
	};
	
	
	 
	private void init(int expireInMinutes) {
		RemovalListener<String, SharedMap<String,Object>> removalListener = new RemovalListener<String, SharedMap<String,Object>>() {
			public void onRemoval(RemovalNotification<String, SharedMap<String,Object>> removal) {
				if (removal.getCause() == RemovalCause.EXPIRED) {
					processAfterExpire(removal.getKey(), removal.getValue());
				} else if (removal.getCause() == RemovalCause.REPLACED) {
					
				} else {
				}

			}
		};

		cache = CacheBuilder.newBuilder()
		.maximumSize(10000)
		.expireAfterWrite(expireInMinutes, TimeUnit.MINUTES)
		.removalListener(removalListener).build(new CacheLoader<String, SharedMap<String,Object>>() {
			public  SharedMap<String,Object> load(String key) {
				return getUnchecked(key);
			}
		});

	}
	
	public SharedMap<String,Object> getUnchecked(String key){
		SharedMap<String,Object> val = null;
		try{
			val =cache.getUnchecked(key);
		}catch(Exception e){}
		return val;
	}
	
	public boolean containsKey(String key){
		return cache.asMap().containsKey(key);
	}
	
	public Set<String> keySet(String key){
		return cache.asMap().keySet();
	}
	
	public SharedMap<String,Object> put(String key , SharedMap<String,Object> value){
		if(value != null){
			cache.put(key, value);
		}
		return value;
	}
	
	public void add(String key , SharedMap<String,Object> value){
		if(value != null){
			cache.put(key, value);
		}
	}
	
	public void delete(String key) {
		cache.invalidate(key);
		
	}
	
	public SharedMap<String,Object> get(String key){
		SharedMap<String,Object> val = null;
		try{
			val =cache.get(key);
		}catch(Exception e){}
		return val;
	}
	
	
	public long size(){
		return cache.size();
	}
	
	
	public void cleanUp(){
		cache.cleanUp();
	}
	
	public ConcurrentMap<String, SharedMap<String,Object>> asMap() {
		return cache.asMap();
	}
	
	public LoadingCache<String, SharedMap<String,Object>> getCache() {
		return cache;
	}
	
	public abstract void processAfterExpire(String key, SharedMap<String,Object> value);
	
 


}
