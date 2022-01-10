package com.pgmate.lib.key;

import java.util.Random;

import com.pgmate.lib.util.lang.CommonUtil;

/**
 * @author Administrator
 *
 */
public class GenKey {

	private static int KEY_COUNT = 18;
	private static int KEY_RANGE = 104;
	
	
	

	
	public static String genKeys(Enum<CPKEY> key,String value){
		
		if(key.toString().equals("")){
			return makeSerialNumber(value, randomKeyAccess());
		}else{
			return key.toString()+makeSerialNumber(value, randomKeyAccess());
		}
	}
	
	public static String genInterMsgKeys(String value){
		return CPKEY.INTER_MSG.toString()+CommonUtil.getCurrentDate("yyyyMMddHHmm")+"_"+makeSerialNumber(value, randomKeyAccess());
	}
	
    
    private static Integer[] randomKeyAccess(){
    	Integer[] cnt = new Integer[KEY_COUNT];
    	Random random = new Random();
    	for(int i =0 ; i < KEY_COUNT ; ++i) {
    		cnt[i] = random.nextInt(KEY_RANGE);
    	}
    	return cnt;
	}
    
    private static synchronized String makeSerialNumber (String value, Integer[] keys){
		 
    	StringBuilder sb = new StringBuilder();
    	value +=System.nanoTime();
    	String serial = "";
    	try{
    		serial = SecurityHash(value,"MD2") +  SecurityHash(value,"MD5") +SecurityHash(value,"SHA1");
    	}catch(Exception e){}
    	for(int i = 0 ; i < keys.length ;++i) {
    		sb.append( serial.charAt(keys[i]));
    		if(i == 3 || i == 9 || i == 12 )  sb.append("-");
    	}

    	return sb.toString();
	  }
    
    private static String SecurityHash(String stringInput, String algorithmName )throws Exception {
	  
    	String hexMessageEncode = "";
    	byte[] buffer = stringInput.getBytes();
    	java.security.MessageDigest messageDigest = java.security.MessageDigest.getInstance(algorithmName);
    	messageDigest.update(buffer);
    	byte[] messageDigestBytes = messageDigest.digest();
     
    	for (int index=0; index < messageDigestBytes.length ; index ++){
    		int countEncode = messageDigestBytes[index] & 0xff;
    		if (Integer.toHexString(countEncode).length() == 1) hexMessageEncode = hexMessageEncode + "0";
    		hexMessageEncode = hexMessageEncode + Integer.toHexString(countEncode);
    	}
 
    	return hexMessageEncode;
    }
    
    

    
	public static void main(String[] args) {
		try{
		long time = System.currentTimeMillis();
		System.out.println(GenKey.genKeys(CPKEY.TRX_ID, "MPUNION"));
		System.out.println(GenKey.genKeys(CPKEY.SECRET_KEY, "kollshop"));
		System.out.println(GenKey.genKeys(CPKEY.PUBLIC_KEY, "kollshop"));
		System.out.println(GenKey.genKeys(CPKEY.SECRET_KEY, "pertechhk"));
		System.out.println(GenKey.genKeys(CPKEY.PUBLIC_KEY, "pertechhk"));
		System.out.println(GenKey.genKeys(CPKEY.SECRET_KEY, "gsshkglea"));
		System.out.println(GenKey.genKeys(CPKEY.PUBLIC_KEY, "gsshkglea"));
		
		}catch(Exception e){e.printStackTrace();}


	}

}
