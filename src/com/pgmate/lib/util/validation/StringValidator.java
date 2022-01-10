package com.pgmate.lib.util.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pgmate.lib.util.lang.CommonUtil;

/**
 * @author Administrator
 *
 */
public class StringValidator {

	private static final String EMAIL_PATTERN 	= 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	private static final String IPADDRESS_PATTERN 	= 
			"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
			"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
			"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
			"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

	
	public static boolean matchEmail(String email){
		
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();

	}
	
	public static boolean matchIpAddress(String ipAddress){
		Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
		Matcher matcher = pattern.matcher(ipAddress);
		return matcher.matches();
	}
	
	public static boolean isDigitNumber(String num){
		return num.matches("^\\d+$");
	}
	
	public static boolean isNumber(String num){
		return num.matches("^[-+]?\\d+(\\.\\d+)?$");
	}
	
	public static boolean isNumberWith2Decimals(String num){
		return num.matches("^\\d+\\.\\d{2}$");
	}
	
	public static boolean matchString(String src,String[] dest){
		if(CommonUtil.isNullOrSpace(src)){
			return false;
		}
		boolean isCollect = false;
		for(String d:dest){
			if(src.equals(d)){
				isCollect =  true;
				break;
			}
		}
		return isCollect;
	}
	
	
	public static boolean isInclude(String src,String[] dest){
		if(CommonUtil.isNullOrSpace(src)){
			return false;
		}
		boolean isInclude = false;
		for(String d:dest){
			if(src.indexOf(d) > 01){
				isInclude =  true;
				break;
			}
		}
		return isInclude;
	}
	
	public static boolean isInclude(Object obj,String[] dest){
		String src = CommonUtil.toString(obj);
		if(CommonUtil.isNullOrSpace(src)){
			return false;
		}
		boolean isInclude = false;
		for(String d:dest){
			if(src.indexOf(d) > 01){
				isInclude =  true;
				break;
			}
		}
		return isInclude;
	}
	
	public static boolean startsWith(Object src,String[] dest){
		return startsWith(CommonUtil.toString(src),dest);
	}
	
	public static boolean startsWith(String src,String[] dest){
		if(CommonUtil.isNullOrSpace(src)){
			return false;
		}
		boolean isCollect = false;
		for(String d:dest){
			if(src.startsWith(d)){
				isCollect =  true;
				break;
			}
		}
		return isCollect;
	}
	
	public static void main(String[] args){
		System.out.println(StringValidator.matchEmail(""));
		System.out.println(StringValidator.isDigitNumber("123213.222"));
		System.out.println(StringValidator.isDigitNumber("123,213.22"));
	}
	
	
	
	

}
