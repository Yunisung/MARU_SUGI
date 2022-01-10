package com.pgmate.sugi.util;


/**
 * @author Administrator
 *
 */
public class ControllerUtil {
	static public String identityMasking(String orgStr){
		//주민번호
		System.out.println(orgStr);
		if(orgStr.matches("^\\d{6}\\-[1-4]\\d{6}$")){
			orgStr = orgStr.substring(0, 8) + "******";
		}
		
		return orgStr;
	}
	
	public static void main(String[] args) {
		//System.out.println(ControllerUtil.identityMasking("8170218-1402919"));
	}
}
