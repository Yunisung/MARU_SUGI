package com.pgmate.lib.util.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.pgmate.lib.util.lang.CommonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Administrator
 *
 */
public class GzipUtil {

	private Logger logger = LoggerFactory.getLogger( getClass() );
	private static final int BUFFER_SIZE = 1024 * 10;
	
	
	public GzipUtil(){
		
	}
	
	public boolean compress(String destFile,String zipFile){
		boolean isCompressed = false;

		BufferedInputStream bis 	= null;
		BufferedOutputStream bos 	= null;

		try{
			bis = new BufferedInputStream(new FileInputStream(destFile));
	        bos = new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(zipFile)));

	        byte[] buffer = new byte[BUFFER_SIZE];
            int cnt = 0;
            while ((cnt = bis.read(buffer, 0, BUFFER_SIZE)) != -1) {
                bos.write(buffer, 0, cnt);
            }
            bos.flush();
	        bos.close();
	        bis.close();
	        isCompressed = true;
	        
	        logger.debug("zip : {} -> {}",destFile ,zipFile,this);
		}catch(Exception e){
			logger.debug(CommonUtil.getExceptionMessage(e));
		}
		return isCompressed;
	}

	public boolean decompress(String zipFile,String unzipFile){
		boolean isCompressed = false;

		BufferedInputStream bis 	= null;
		BufferedOutputStream bos 	= null;

		try{
			bis = new BufferedInputStream(new GZIPInputStream(new FileInputStream(zipFile)));
	        bos = new BufferedOutputStream(new FileOutputStream(unzipFile));

	        byte[] buffer = new byte[BUFFER_SIZE];
            int cnt = 0;
            while ((cnt = bis.read(buffer, 0, BUFFER_SIZE)) != -1) {
                bos.write(buffer, 0, cnt);
            }
            bos.flush();
	        bos.close();
	        bis.close();
	        isCompressed = true;
	        logger.debug("unzip : {} -> {}",zipFile ,unzipFile,this);
		}catch(Exception e){
			logger.debug(CommonUtil.getExceptionMessage(e));
		}
		return isCompressed;
	}
}
