package com.pgmate.lib.vertx.main;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.lib.util.lang.CommonUtil;

import io.vertx.ext.web.RoutingContext;

/**
 * @author Administrator
 *
 */
public class VertXShared {

	private static Logger logger = LoggerFactory.getLogger( com.pgmate.lib.vertx.main.VertXShared.class );
	
	private static long ROUTING_CONTEXTLIVE_TIME = 5*60*1000;
	private static ConcurrentHashMap<String,Long> sharedRotingTimer = new ConcurrentHashMap<String,Long>();
	private static ConcurrentHashMap<String,RoutingContext> sharedRoutingContext = new ConcurrentHashMap<String,RoutingContext>();

	/**
	 * VertXShared Memory 에 현재의 RoutingContext 를 넣는다
	 * @param message id
	 * @param RoutingContext rc
	 */
	public static void addRoutingContext(String id,RoutingContext rc){
		VertXShared.sharedRotingTimer.put(id,new Long(System.currentTimeMillis()));
		VertXShared.sharedRoutingContext.put(id,rc);
	}
	
	/**
	 * VertXShared Memory 에서 RoutingContext 를 가져온다.
	 * @param id
	 * @return
	 */
	public static RoutingContext getRoutingContext(String id){
		return VertXShared.sharedRoutingContext.get(id);
	}
	
	/**
	 * VertXShared Memory 에서 RoutingContext 를 삭제한다.
	 * @param id
	 */
	public static void removeRoutingContext(String id){
		try{
			if(VertXShared.sharedRotingTimer.containsKey(id)){
				VertXShared.sharedRotingTimer.remove(id);
			}
			if(VertXShared.sharedRoutingContext.containsKey(id)){
				VertXMessage.set408(VertXShared.sharedRoutingContext.get(id));
				VertXShared.sharedRoutingContext.remove(id);
			}
		}catch(Exception e){}
	}
	
	
	public static boolean containsKey(String id){
		return VertXShared.sharedRoutingContext.containsKey(id);
	}
	
	
	/**
	 * 지정된 시간 5분 이상 남아있는 RoutingContext 를 삭제한다.
	 */
	public static void removeRoutingZombie(){
		long time = System.currentTimeMillis();
		
		int totalRoutingContext = sharedRoutingContext.size();
		
		for(ConcurrentHashMap.Entry<String,Long> entry : sharedRotingTimer.entrySet()){
			long createTime = CommonUtil.parseLong(entry.getValue());
			if(time - createTime > ROUTING_CONTEXTLIVE_TIME){
				removeRoutingContext(entry.getKey());
				logger.debug("totalRoutingContext : {}",totalRoutingContext--);
				logger.debug("remove id : {} , live_time : {}",entry.getKey(),(time - createTime));
			}
		}
	}
	
	
}
