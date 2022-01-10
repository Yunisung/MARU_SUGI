package com.pgmate.sugi.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

/**
 * @author Administrator
 *
 */
public class RandomUtil {

	/*
	 * 	Random 한 문자+숫자 조합 추출
	 *  Make Service Or Merchant Id 
	 */
	public static String getRandomString(String frontSt, int length ){
        char[] charaters = {'A','B','C','C','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','0','1','2','3','4','5','6','7','8','9'};
       
        StringBuffer sb = new StringBuffer();
        Random rn = new Random();
        for( int i = 0 ; i < length ; i++ ){
            sb.append( charaters[ rn.nextInt( charaters.length ) ] );
        }
        
        Calendar calendar = Calendar.getInstance();
        java.util.Date date = calendar.getTime();
        String dateRandom = (new SimpleDateFormat("ddHHmmss").format(date));
        
        
        return frontSt+"_"+dateRandom+sb.toString();
    }

}
