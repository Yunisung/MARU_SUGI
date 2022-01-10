package com.pgmate.sugi.util;

import com.pgmate.lib.util.cipher.Base64;
import com.pgmate.lib.util.cipher.SeedKisa;
import com.pgmate.lib.util.lang.ByteUtil;
import com.pgmate.lib.util.lang.CommonUtil;

/**
 * @author Administrator
 *
 */
public class CryptUtil {

	public static byte[] CRYPT_KEY = ByteUtil.toBytes("696d697373796f7568616e6765656e61", 16);
	
	
	public static String encrypt(String data){
		if(CommonUtil.isNullOrSpace(data)){
			return "";
		}
		return Base64.encodeToString(SeedKisa.encrypt(data, CRYPT_KEY));
	}
	
	public static String decrypt(String data){
		if(CommonUtil.isNullOrSpace(data)){
			return "";
		}
		return CommonUtil.toString(SeedKisa.decrypt(Base64.decode(data), CRYPT_KEY));
	}


}
