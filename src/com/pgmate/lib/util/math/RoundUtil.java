package com.pgmate.lib.util.math;

import java.math.BigDecimal;

import com.pgmate.lib.util.lang.CommonUtil;

/**
 * @author Administrator
 *
 */
public class RoundUtil {

	/**
	 * 
	 */
	public RoundUtil() {
		// TODO Auto-generated constructor stub
	}
	
	/** 숫자에 대한 반올림 ROUND_HALF_UP
	 * 1235.678756,3 -> 1235.679
	**/
	public static double roundHalfUp(double value,int scale){
		BigDecimal b = new BigDecimal(CommonUtil.toString(value));		
		return CommonUtil.parseDouble(b.setScale(scale,BigDecimal.ROUND_HALF_UP).toString());
	}
	
	/** 숫자에 대한 반내림 ROUND_HALF_DOWN
	 * 1235.678756,3 -> 1235.679
	**/
	public static double roundHalfDown(double value,int scale){
		BigDecimal b = new BigDecimal(CommonUtil.toString(value));		
		return CommonUtil.parseDouble(b.setScale(scale,BigDecimal.ROUND_HALF_DOWN).toString());
	}
	
	/** 숫자 내림 ROUND_DOWN
	 * 1235.678856,3 -> 1235.678
	 * **/
	public static double roundDown(double value,int scale){
		BigDecimal b = new BigDecimal(CommonUtil.toString(value));		
		return CommonUtil.parseDouble(b.setScale(scale,BigDecimal.ROUND_DOWN).toString());
	}
	
	
	/** 특정 자리수에서 무조건 반올림. ROUND_UP
	 * 1235.678256,3 -> 1235.679
	 * **/
	public static double roundUp(double value,int scale){
		BigDecimal b = new BigDecimal(CommonUtil.toString(value));		
		return CommonUtil.parseDouble(b.setScale(scale,BigDecimal.ROUND_UP).toString());
	}
	
	
	
	
	
	
	
	
	
	
	
	

}

