package com.pgmate.lib.conf;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.lib.util.cipher.Base64;
import com.pgmate.lib.util.cipher.SeedKisa;
import com.pgmate.lib.util.gson.GsonUtil;
import com.pgmate.lib.util.io.FileIO;
import com.pgmate.lib.util.lang.ByteUtil;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.prop.PropertyUtil;

/**
 * @author Administrator
 *
 */
public class ConfigLoader {

	private static Logger logger 		= LoggerFactory.getLogger(com.pgmate.lib.conf.ConfigLoader.class);
	private static String SERVICE_JSON 	= PropertyUtil.getCyrexConf()+File.separator+"service.json";
	private static byte[] SEED_KEY		= ByteUtil.toBytes("696d697373796f7568616e6765656e61", 16);
	private static Config config 		= null;
	public static boolean CRYPT			= false;
	
	public ConfigLoader() {
		// TODO Auto-generated constructor stub
	}
	
	public static Config getConfig(){
		if(config == null){
			load(SERVICE_JSON);
		}
		return config;
	}
	
	public static Config getConfig(String configFile){
		if(config == null){
			load(configFile);
		}
		return config;
	}
	
	private static void load(String configFile){
		
		try{
			String json = CommonUtil.toString(FileIO.getBytes(configFile));
			if(CRYPT){
				json = new String(SeedKisa.decrypt(Base64.decode(json), SEED_KEY));
			}
			
			ConfigLoader.config = (Config)GsonUtil.fromJson(json, new Config());
	
		}catch(Exception e){
			logger.debug(CommonUtil.getExceptionMessage(e));
		}
		
	}
	
	
	public static String write(Config config){
		String json = "";
		try{
			json = GsonUtil.toJson(config, true, "");
			if(CRYPT){
				json = Base64.encodeToString(SeedKisa.encrypt(json, SEED_KEY));
			}
		}catch(Exception e){
			logger.debug(CommonUtil.getExceptionMessage(e));
		}
		return json;
	}
	
	

}
