package com.pgmate.lib.util.lang;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Administrator
 *
 */
public class CommonUtil {

	private static Logger logger = LoggerFactory.getLogger( com.pgmate.lib.util.lang.CommonUtil.class );
	/**
	 * Object 를 String 형으로 변환할때 사용되면 Timestamp 는 yyyyMMddHHmmss 포맷으로 반환한다.
	 * @param obj
	 * @return
	 */
	public static String toString(Object obj){
		if(obj == null){
			return "";
		}else{
			if(obj instanceof String){
				return (String)obj;
			}else if(obj instanceof Integer){
				Integer i = (Integer)obj;
				return i.toString();
			}else if(obj instanceof Long){
				Long l = (Long)obj;
				return l.toString();
			}else if(obj instanceof Double){
				Double d = (Double)obj;
				return d.toString();
			}else if(obj instanceof Byte){
				Byte b = (Byte)obj;
				return b.toString();
			}else if(obj instanceof byte[]){
				byte[] b = (byte[])obj;
				return new String(b);
			}else if(obj instanceof Character){
				Character c = (Character)obj;
				return String.valueOf(c);
			}else if(obj instanceof char[]){
				char[] c = (char[])obj;
				return String.valueOf(c);
			}else if(obj instanceof BigInteger){
				BigInteger b =  (BigInteger)obj;
				return b.toString();
			}else if(obj instanceof BigDecimal){
				BigDecimal b =  (BigDecimal)obj;
				return b.toString();
			}else if(obj instanceof Boolean){
				Boolean b =  (Boolean)obj;
				return b.toString();
			}else if(obj instanceof Timestamp){
				return timestampToString((Timestamp)obj,"yyyy/MM/dd HH:mm:ss.S");
			}else if(obj instanceof String[]){
				String[] s = (String[])obj;
				StringBuilder ret = new StringBuilder();
				for(int i=0;i<s.length;i++){
					ret.append(CommonUtil.nToB(s[i]));
					if(i+1 != s.length){
						ret.append(",");
					}
				}
				return ret.toString();
			}else{
				return "";
			}
			
			
		}
	}
	
	
	/**
	 * byte[] 를 지정한 character set 으로 인코딩 후 String Object 로 반환한다.
	 * @param b
	 * @param charsetName
	 * @return
	 */
	public static String toString(byte[] b,String charsetName){
		String str = "";
		try{
			str= new String(b,charsetName);
		}catch(Exception e){
			str= toString(b);
		}
		return str;
	}
	
	/**
	 * byte[] 의 특정 시작 index 부터 종료 index 까지를 String Object 로 반환한다.
	 * @param b
	 * @param start
	 * @param end
	 * @return
	 */
	public static String toString(byte[] b , int start , int end){
		int len = b.length;
		if(len < start || len==0 )
			return "";
		else if(len < start+end)
			return new String(b,start,len);
		else
			return new String(b,start,end);
	}
	
	/**
	 * byte[] 의 특정 시작 index 부터 마지막 index 까지를 String Object 로 반환한다.
	 * @param b
	 * @param start
	 * @return
	 */
	public static String toString(byte[] b , int start){
		return toString(b,start,b.length-start);
	}
	
	
	/**
	 * byte[] 의 특정 시작 index 부터 종료 index 까지를  지정한 character set 으로 인코딩 후 String Object 로 반환한다.
	 * @param b
	 * @param start
	 * @param end
	 * @param charSet
	 * @return
	 */
	public static String encodeString(byte[] b , int start , int end,String charSet){
		byte[] nb = getBytes(b, start, end);
		if(nb == null){
			return "";
		}else{
			String str = "";
			try{
				str = new String(nb,charSet);
			}catch(Exception e){
				e.printStackTrace();
			}
			return str;
		}
	}
	
	
	
	/**
	 * String object 가 null 이면 true 를 null 이 아니면 false 를 반환한다.
	 * @param str
	 * @return
	 */
	public static boolean isNull(String str) {
		if (str == null)
			return true;
		else
			return false;
	}
	
	/**
	 * String object 가 null 이거나 "" 이면 true 를 null 이 아니면 false 를 반환한다.
	 * @param str
	 * @return
	 */
	public static boolean isNullOrSpace(String str){
		if(isNull(str)){
			return true;
		}
		if(str.trim().length() == 0){
			return true;
		}
		return false;
	}
	
	public static boolean isEmpty(String str){
		return isNullOrSpace(str);
	}
	
	/**
	 * String object 가 null 이면 "" 로 반환하여 NullPointException  을 제거한다.
	 * @param str
	 * @return
	 */
	public static String nToB(String str){
		if(isNull(str)){
			return "";
		}else{
			return str;
		}
	}
	
	/**
	 * String object 가 null 이면 replaceString 으로 반환한다.
	 * @param str
	 * @param replaceStr
	 * @return
	 */
	public static String nToB(String str,String replaceStr){
		if(isNull(str)){
			return replaceStr;
		}else{
			return str;
		}
	}
	
    public static String replace(String source, String target, String wantWord) {
	    if (source == null || target == null || wantWord == null ||
	        target.length() == 0)return source;

	    StringBuffer result = new StringBuffer(source.length());

	    int idx1 = 0, idx2 = 0;
	    while ( (idx2 = source.indexOf(target, idx1)) >= 0) {
	      result.append(source.substring(idx1, idx2));
	      result.append(wantWord);
	      idx1 = idx2 + target.length();
	    }
	    
	    
	    result.append(source.substring(idx1));
	    return result.toString();
	}		
	
	/**
		대상문자열(str)의 임의의 위치(index)에 지정문자열(insert)를 추가한 문자열을 반환한다.
	
		@param str 대상문자열
		@param index 지정문자열을 추가할 위치로서 대상문자열의 첫문자 위치를 0으로 시작한 상대 위치. index가 0 보다 작은 값일 경우는 대상문자열의 끝자리를 0으로 시작한 상대적 위치. 맨앞과 맨뒤는 문자열 + 연산으로 수행가능함으로 제공하지 않는다.
		@param insert 추가할 문자열
		@return 추가완료된 문자열
	*/
	public static String insert(String str, int index, String insert) {
		StringBuilder sb = new StringBuilder(str);
		sb.insert(index, insert);
		return sb.toString();
	}
	
	
	/**
	 * 문자열에서 delim 을 기준으로 array로 변경함.
	 * space 는 delim 이 연속된 것을 array 로 사용할지 여부 결정
	 * split 의 limit 가 -1 이면 마지막에 delim 이 올경우 1개의 Array 를 추가하여 반환한다.
	 * @param str
	 * @param delim
	 * @param space
	 * @return
	 */
	public static String[] split(String str, String delim, boolean space) {
		if(space){
			return str.split("["+delim+"]",-1);
		}else{
			return str.split("[-]+",-1);
		}
	}
	
	/**
	 * 지정된 Array 의 갯수만큼만 반환하는 경우 array 가 적을 경우  추가 space 를 반환하며.
	 * Array 의 갯수가 초과하면 지정된 Array 만 반환한다.
	 * @param str
	 * @param delim
	 * @param space
	 * @param array
	 * @return
	 */
	public static String[] split(String str,String delim,boolean space,int arrayLen){
		return adjustArray(split(str,delim,space),arrayLen);
	}
	

	/**
	 * String[] 에 null이 있을 경우 null 을 ""로 초기화함.
	 * @param oldArray
	 * @return
	 */
	public static String[] adjustArray(String[] oldArray){
		
		String[] newArray = new String[oldArray.length];
		for(int i = 0 ; i < oldArray.length ; i++){
			if(isNullOrSpace(oldArray[i])){
				newArray[i] = "";
			}else{
				newArray[i] = oldArray[i];
			}
		}
		return newArray;
	}
	
	/**
	 * String[] 에 null이 있을 경우 이를 null 에러가 발생하지 않도록 만들어 주며 지정한 array 갯수만큼 생성하여 반환한다.
	 * @param oldArray
	 * @return
	 */
	public static String[] adjustArray(String[] oldArray,int array){
		List<String> list = new ArrayList<String>(Arrays.asList(oldArray));
		if(list.size() >= array){
			list = list.subList(0, array);
		}else{
			int len = array-list.size();
			for(int i=0;i<len;i++){
				list.add("");
			}
		}
		
		return list.toArray(new String[array]);
	}
	
	/**
	 * String 에 지정한 size 만큼 왼쪽에 0 을 추가하여 반환한다.
	 * @param str
	 * @param size
	 * @return
	 */
	public static String zerofill(String str,int size){
		str = nToB(str);
		if(str.length() > size){
			return str.substring(0,size);
		}
		try {
			NumberFormat nf = NumberFormat.getInstance();
			return zerofill(nf.parse(str), size);
		} catch (Exception e) {
			return zerofill(0,size);
		}
		
	}
	
	/**
	 * Number 에 지정한 size 만큼 왼쪽에 0 을 추가하여 반환한다.
	 * @param num
	 * @param size
	 * @return
	 */
	public static String zerofill(Number num, int size) {
		String str = set("0",size);
		if(num instanceof Double){
			return zerofillDouble(toString(num.doubleValue()),size);
		}else{
			DecimalFormat df = new DecimalFormat(str);
			return df.format(num);
		}
	}
	
	/**
	 * Double 형으로 소수 이하가 포함되어 있는 경우 왼쪽에 0을 추가하여 반환한다.
	 * @param str
	 * @param size
	 * @return
	 */
	public static String zerofillDouble(String str,int size){
		int len = size - str.length();
		if(len > 0){
			return set("0",len)+str;
		}else{
			return str.substring(0,size);
		}
	}
	
	/***
	 * String 을 size 만큼 복사하여 추가하여 반환한다.
	 * @param str
	 * @param size
	 * @return
	 */
	public static String set(String str,int size){
		StringBuilder sb = new StringBuilder();
		for(int i=0 ; i < size ; i++){
			sb.append(str);
		}
		return sb.toString();
	}
	
	
	/**
	 * " " 즉 공백을 지정한 size 만큼 추가하여 반환한다.
	 * @param size
	 * @return
	 */
	public static String setFiller(int size){
		return set(" ",size);
	}
	
	/**
	 * Object 의 크기를 byte 단위로 계산후 size 만큼 확장하되 부족하면 오른쪽에 " " 를 추가한다.
	 * @param obj
	 * @param size
	 * @return
	 */
	public static String byteFiller(Object obj,int size){
		String str = toString(obj);
		return str+set(" ",size-str.getBytes().length);
				
	}
	
	/**
	 * Object 의 크기를 byte 단위로 계산후 size 만큼 확장하되 부족하면 왼쪽에 " " 를 추가한다.
	 * @param obj
	 * @param size
	 * @return
	 */
	public static String byteFillerLeft(Object obj,int size){
		String str = toString(obj);
		return set(" ",size-str.getBytes().length)+str;
	}
	
	/**
	 * Object 의 크기를 byte 단위로 계산후 size 만큼 잘라낸 후 반환한다.
	 * @param obj
	 * @param size
	 * @return
	 */
	public static String byteTrim(Object obj,int size){
		byte[] src = toString(obj).getBytes();
		if(src.length <= size){
			return new String(src);
		}else{
			byte[] dest = new byte[size];
			System.arraycopy(src,0, dest,0,size);
			return new String(dest);
		}
	}
	
	/**
	 * byte[] 의 시작 index 와 종료 index 만큼을 카피하여 반환한다.
	 * @param b
	 * @param start
	 * @param end
	 * @return
	 */
	public static byte[] getBytes(byte[] b,int start,int end){
		byte[] dest = null;
		int len = b.length;
		
		if(len == 0 || len < start || end-start <0){
			return null;
		}
		if(len < end){
			dest = new byte[len];
			System.arraycopy(b, 0, dest, 0, len);
			return dest;
		}else{
			dest = new byte[end];
			System.arraycopy(b, 0, dest, 0, end);
			return dest;
		}
	}
	
	/**
	 * byte[] a 에 byte[] b 를 추가하여 반환한다. 
	 * @param a
	 * @param b
	 * @return
	 */
	public static byte[] byteAppender(byte[] a,byte[] b){
		int len = a.length +b.length;
		byte[] dest = new byte[len];
		
		System.arraycopy(a,0, dest,0,a.length);
		System.arraycopy(b,0, dest,a.length,b.length);
		return dest;
	}
	
	/**
	 * System.arrayCopy 의 확장형
	 * @param destBuffer
	 * @param destPos
	 * @param srcBuffer
	 * @param srcLen
	 */
	public static void arrayCopy(Object destBuffer,int destPos,Object srcBuffer,int srcLen){
		System.arraycopy(srcBuffer,0,destBuffer,destPos,srcLen);
	}
	
	/**
	 * String 의 charset 을 charSet1 -> charSet2 로 변환하여 반환한다.
	 * @param str
	 * @param charSet1
	 * @param charSet2
	 * @return
	 */
	public static String encode(String str,String charSet1,String charSet2){
		try {
			str = new String(str.getBytes(charSet1), charSet2);
		} catch (UnsupportedEncodingException unsupportedencodingexception) {} catch (NullPointerException nullpointerexception) {}
		return str;
	}
	
	/**
	 * str 문자열의 index 열부터 replace char 로 변경한다.
	 * setStrToHide(1111112222222,"*",7) -> 111111******* 
	 * @param str
	 * @param replace
	 * @param cnt
	 * @return
	 */
	public static String setStrToHide(String str,String replace,int index){
		
		byte[] b = str.getBytes();
		int len  = b.length;
		for(int i=index-1;i<len;i++){
			b[i] = (byte)replace.charAt(0);
		}
		return toString(b);
	}
	
	
	public static String getRandomKeyUUID(){
		return UUID.randomUUID().toString();
	}
	

	/**
	 * Exception 발생 시 Message 를 가독성 있도록 반환한다.
	 * @param e
	 * @return
	 */
	public static String getExceptionMessage(Exception e){
		StringBuilder sb = new StringBuilder();
		if(e instanceof SQLException){
			SQLException sql = (SQLException)e;
			sb.append("SQLState: " + sql.getSQLState ());
			sb.append(",Message:  " + sql.getMessage ());
			sb.append(",ErrorCode:   " + sql.getErrorCode ());
			sb.append(sql.getNextException ());  //Adds an SQLException object to the end of the chain.
		}
		sb.append("Message ="+e.getMessage()+"\n");
		StackTraceElement[] trace = e.getStackTrace();
		for(int i=0;i<trace.length;i++){
			sb.append(trace[i].toString()+"\n");
		}

		return sb.toString();
	}
	
	public static String getSQLExceptionMessage(Exception e){
		StringBuilder sb = new StringBuilder();
		if(e instanceof SQLException){
			SQLException sql = (SQLException)e;
			
			String msg = sql.getMessage();
			int stx = msg.indexOf("CAS INFO");
			
			if(stx > 0){
				msg = msg.substring(0, stx-1) ;
			}
			sb.append("ErrorCode:   " + sql.getErrorCode ());
			sb.append(",Message:  " + msg+"\n");
			StackTraceElement[] trace = e.getStackTrace();
			int depth = 5;
			if(trace.length < 5){
				depth = trace.length;
			}
			for(int i=0;i<trace.length;i++){
				sb.append(trace[i].toString()+"\n");
			}
		}else{
			sb.append("Message ="+e.getMessage());
		}
		return sb.toString();
	}
	
	
	
	
	
	/**
	 * Query String & 와 = 으로 이루어진 String 을 charset 으로 decoding 하여 HashMap 으로 변환 후 반환한다.
	 * @param str
	 * @param charset
	 * @return
	 */
	public static Map<String,String> parseQueryString(String str,String charset){
		Map<String,String> map = new HashMap<String,String>();
		String[] array = str.split("[&]");
		for(String s:array){
			String[] newArray = split(s,"[=]",true,2);
			map.put(newArray[0],decode(newArray[1],charset));
		}
		return map;
	}
	
	
	 /**
	  * Map<String,String> 을 QueryString 으로 변환한다.
	  * @param map
	  * @param charset
	  * @return
	  */
	public static String toQueryString(Map<String,String> map,String charset){
		StringBuilder sb = new StringBuilder();
		if(map.size() !=0){
			int i=0;
			for (Entry<String, String> entry : map.entrySet()) {
				sb.append(entry.getKey()).append("=").append(encode(entry.getValue(),charset));
				i++;
				if(map.size() != i){
					sb.append("&");
				}
	        }
		}
		return sb.toString();
	}

	/**
	 * String 을 특정 character set 으로 encoding 하여 반환한다.
	 * charset 을 지정하지 않을 경우 utf-8 로 변환한다.
	 * @param str
	 * @param charset
	 * @return
	 */
	public static String encode(String str,String charset){
		str = nToB(str);
		charset = nToB(charset,"utf-8");
		try{
			str = URLEncoder.encode(str, charset);
		}catch(Exception e){
			logger.info(getExceptionMessage(e));
		}
		return str;
		
	}
	
	/**
	 * String 을 특정 character set 으로 decoding 하여 반환한다.
	 * charset 을 지정하지 않을 경우 euc-kr 로 변환한다.
	 * @param str
	 * @param charset
	 * @return
	 */
	public static String decode(String str,String charset){
		str = nToB(str);
		charset = nToB(charset,"utf-8");
		
		try{
			str = URLDecoder.decode(str, charset);
		}catch(Exception e){
			logger.info(getExceptionMessage(e));
		}
		return str;
		
	}
	


	
	

	/**
	* 날짜를 받아 Timestamp 로 변환한다
	* @param  dateString : yyyyMMdd, timeString : HHmmss
	* @return 배열값[2] : 성공 OK, 실패 :FAIL
	*/
	public static Timestamp stringToTimestamp(String dateStr) {
		String format = "yyyyMMddHHmmss";
		
		if(dateStr.length() >= 21  ){
			format = "yyyy-MM-dd HH:mm:ss.SSS";
		}else if(dateStr.length() == 19  ){
			format = "yyyy-MM-dd HH:mm:ss";
		}else if(dateStr.length() == 17  ){
			format = "yyyy-MM-dd HHmmss";
		}else if(dateStr.length() == 14  ){
			format = "yyyyMMddHHmmss";
		}else if(dateStr.length() == 12  ){
			format = "yyyyMMddHHmm";
		}else if(dateStr.length() == 8){
			format = "yyyyMMdd";
		}else if(dateStr.length() == 10){
			format = "yyyy-MM-dd";
		}else if(dateStr.length() == 6){
			format = "yyyyMM";
		}else if(dateStr.length() == 4){
			format = "yyMM";
		}
		return new Timestamp(getDate(format,dateStr).getTime());
	
	}
	
	
	/**
	* 현재 시간의 Timestamp를 반환한다.
	* @param
	* @return Timestamp Object
	*/
	public static Timestamp getCurrentTimestamp() {
		Date currDt = new Date(System.currentTimeMillis());
		return new Timestamp(currDt.getTime());
	}
	
	/**
	* Timestamp를 받아 format 형태의 스트링으로 변환한다
	* @param  Timestamp
	* @return String date format
	*/
	public static String timestampToString(Timestamp ts) {
		return timestampToString(ts,"yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	* Timestamp를 받아 format 형태의 스트링으로 변환한다
	* @param  Timestamp
	* @return String date format
	*/
	public static String timestampToString(Timestamp ts,String format) {
		if (ts == null) {
			ts = new Timestamp(new java.util.Date().getTime());
		}
		return getDateString(format, ts.getTime(),null);
	}
	
	/**
	날짜 데이터가 오늘보다 과거인지 미래인지 체크한다.
	@param yyyyMMdd
	@return 과거면 false, 같거나 미래면 true
	*/
	public static boolean isBefore(String str) {
		GregorianCalendar currentDate = new GregorianCalendar();
		GregorianCalendar fromDate = getGregorianCalendar(str);
	
		if (fromDate.before(currentDate)) {
			return false; //현재보다 과거이다.
		} else {
			return  true; //현재보다 미래이다.
		}
	}
	
	
	
	/*****************************************************************************************
	 * 숫자 관련 Utility 
	 *****************************************************************************************/
	
	/**
	 * Object 를 int format 으로 변환하여 반환한다.
	 * @param obj
	 * @return
	 */
	public static int parseInt(Object obj){
		if(obj == null){
			return 0;
		}else{
			try{
				if(obj instanceof String){
					String str = (String)obj;
					return Integer.parseInt(str);
				}else if(obj instanceof Integer){
					Integer i = (Integer)obj;
					return i.intValue();
				}else if(obj instanceof Long){
					Long l = (Long)obj;
					return l.intValue();
				}else if(obj instanceof Double){
					Double d = (Double)obj;
					return d.intValue();
				}else if(obj instanceof Byte){
					Byte b = (Byte)obj;
					return b.intValue();
				}else if(obj instanceof byte[]){
					byte[] b = (byte[])obj;
					String str = new String(b);
					return Integer.parseInt(str);
				}else{
					return 0;
				}
			}catch(Exception e){
				
			}
			return 0;
		}
	}
	
	/**
	 * Object 를 long format 으로 변환하여 반환한다.
	 * @param obj
	 * @return
	 */
	public static long parseLong(Object obj){
		if(obj == null){
			return 0;
		}else{
			try{
				if(obj instanceof String){
					String str = (String)obj;
					return Long.parseLong(str);
				}else if(obj instanceof Integer){
					Integer i = (Integer)obj;
					return i.longValue();
				}else if(obj instanceof Long){
					Long l = (Long)obj;
					return l.longValue();
				}else if(obj instanceof Double){
					Double d = (Double)obj;
					return d.longValue();
				}else if(obj instanceof Byte){
					Byte b = (Byte)obj;
					return b.longValue();
				}else if(obj instanceof byte[]){
					byte[] b = (byte[])obj;
					String str = new String(b);
					return Long.parseLong(str);
				}else{
					return 0;
				}
			}catch(Exception e){
				
			}
			return 0;
		}
	}
	
	/**
	 * Object 를 double format 으로 변환하여 반환한다.
	 * @param obj
	 * @return
	 */
	public static double parseDouble(Object obj){
		if(obj == null){
			return 0;
		}else{
			try{
				if(obj instanceof String){
					String str = (String)obj;
					return Double.parseDouble(str);
				}else if(obj instanceof Integer){
					Integer i = (Integer)obj;
					return i.doubleValue();
				}else if(obj instanceof Long){
					Long l = (Long)obj;
					return l.doubleValue();
				}else if(obj instanceof Double){
					Double d = (Double)obj;
					return d;
				}else if(obj instanceof Byte){
					Byte b = (Byte)obj;
					return b.doubleValue();
				}else if(obj instanceof byte[]){
					byte[] b = (byte[])obj;
					String str = new String(b);
					return Double.parseDouble(str);
				}else{
					return 0;
				}
			}catch(Exception e){
				
			}
			return 0;
		}
	}
	
	
	public static double adjustAmount(double amount){
		if(amount == 0){
			return 0;
		}else if (amount < 0){
			return CommonUtil.adjustDoubleHalfDown(2,amount+0.005);
		}else{
			return CommonUtil.adjustDoubleHalfDown(2,amount-0.005);
		}
	}
	
	public static double adjustAmountUp(double amount){
		if(amount == 0){
			return 0;
		}else if (amount < 0){
			return CommonUtil.adjustDoubleHalfUp(2,amount);
		}else{
			return CommonUtil.adjustDoubleHalfUp(2,amount);
		}
	}
	
	/**
	 * double 형 금액을 문자로 변환하여 반환한다. 단 소수점 이하는 반올림한다.
	 * @param amount
	 * @return
	 */
	public static String convertAmount(double amount){
		amount = adjustDoubleHalfUp(0,amount);
		Double d = new Double(amount);
		String amt = toString(d.longValue());
		return amt;

	}

    /**
     * double 형 금액에서 특정 소숫점 이하(scale)는 반올림하여 반환한다. 
     * @param scale
     * @param d
     * @return
     */
	public static double adjustDoubleHalfUp(int scale,double d){
		return adjustDouble(scale,d,BigDecimal.ROUND_HALF_UP);
	}

	/**
     * double 형 금액에서 특정 소숫점 이하(scale)는 반내림하여 반환한다. 
     * @param scale
     * @param d
     * @return
     */
	public static double adjustDoubleHalfDown(int scale,double d){
		return adjustDouble(scale,d,BigDecimal.ROUND_HALF_DOWN);
	}
	
	/**
	 * double 형 금액에서 특정 소숫점 이하(scale)는 roundingMode 에 따라 반올림,반내림 하여 반환한다. 
	 * @param scale
	 * @param d
	 * @param roundingMode
	 * @return
	 */
	public static double adjustDouble(int scale,double d,int roundingMode){
		try{
			BigDecimal bd = new BigDecimal(d);
			d = bd.setScale(scale,roundingMode).doubleValue();
		}catch(Exception e){
		}
		return d;
	}
	
	/**
	통화형식처럼 숫자 3자리미다 ,(콤마)를 찍는다. String형 인자를 받는다.

	@param money String형 통화형식
	@return 3자리마다 ,(콤마)가 찍힌 형식
	*/
	public static String moneyFormat(String s) {
		NumberFormat numberformat = NumberFormat.getNumberInstance();
		String s1;
		try {
			Number number = numberformat.parse(s);
			s1 = numberformat.format(number);
		} catch (ParseException parseexception) {
			s1 = "0";
		}
		return s1;
	}
	
	public static String makeMoneyType(int intMoney, String delimeter) {
		return (makeMoneyType(CommonUtil.toString(intMoney), delimeter));
	}
	
	public static String makeMoneyType(long lngMoney, String delimeter) {
		return (makeMoneyType(CommonUtil.toString(lngMoney), delimeter));
	}
	
	public static String makeMoneyType(double dbleMoney, String delimeter) {
		return (makeMoneyType(CommonUtil.toString(dbleMoney), delimeter));
	}
	
	public static String makeMoneyType(String strMoney, String delimeter) {
		if (strMoney == null || strMoney.equals("") || delimeter == null || delimeter.equals(""))
			return "";
	
	
		DecimalFormat df = new DecimalFormat();
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
	
		dfs.setGroupingSeparator(delimeter.charAt(0));
		df.setGroupingSize(3);
		df.setDecimalFormatSymbols(dfs);
	
		return (df.format(Double.parseDouble(strMoney))).toString();
	}
		
	
	/*****************************************************************************************
	 * 날짜 관련 Utility 
	 *****************************************************************************************/
	
	/** 
	 * 지정된 format 으로 구성된 String 을 Date 포맷으로 변환하여 반환한다.
	 * @param format
	 * @param dateStr
	 * @return
	 */
	public static Date getDate(String format,String dateStr){
		SimpleDateFormat sdf = new java.text.SimpleDateFormat(format);
		Date date = null;
		try{
			date = sdf.parse(dateStr);
		}catch(Exception e){e.getMessage();}
		return date;
	}
	
	/**
	 * 특정 Locale 의 Date 를 특정 format 의 String 으로 변환하여 반환한다.
	 * @param format
	 * @param date
	 * @param locale
	 * @return
	 */
	public static String getDateString(String format,Date date,Locale locale){
		if(locale == null){
			locale = new Locale("KOREAN","KOREA");
		}
		SimpleDateFormat sdf = new java.text.SimpleDateFormat(format,locale);
		return sdf.format(date);
	}
	
	/**
	 * 특정 Locale 의 long time 을 특정 format 의 String 으로 변환하여 반환한다.
	 * @param format
	 * @param date
	 * @param locale
	 * @return
	 */
	public static String getDateString(String format,long time,Locale locale){
		return getDateString(format, new Date(time), locale);
	}
	
	/**
	 * @param 날짜 포맷을 입력한다. yyyyMMddHHmmss 등
	 * @return 포맷에 정의된 날짜의 String형
	 */
	public static String getCurrentDate(String format){
		return getDateString(format, new Date(), null);
	}
	
	/**
	 * @return yyyy-MM-dd HH:mm:ss 형식의 현재 시간
	 */
	public static String getCurrentDate(){
		return getCurrentDate("yyyy-MM-dd HH:mm:ss");
	}
	
	
	public static String getUtcDate(String format){
		SimpleDateFormat f = new SimpleDateFormat(format); 
		f.setTimeZone(TimeZone.getTimeZone("UTC")); 
		return f.format(new Date()); 

	}
	
	
	public static String getUtcAfterDate(String format,int minute) {
		String currentDate = getUtcDate(format);
		  try{
			  SimpleDateFormat df = new SimpleDateFormat(format);
			  df.setTimeZone(TimeZone.getTimeZone("UTC")); 
			  Date d = df.parse(currentDate); 	
			  Calendar cal = Calendar.getInstance();
			  cal.setTime(d);
			  cal.add(Calendar.MINUTE, minute);
			  currentDate = df.format(cal.getTime());
		  }catch(Exception e){
			  logger.info("error : {}",e.getMessage());
		  }
		  return currentDate;
	  }
	
	/**
	 * begin 시작 시간과 end 기간과의 날짜 계산
	 * @param 시작 Timestamp
	 * @param 종료 Timestamp 
	 * @return 
	 */
	public static long diffOfDay(Timestamp begin, Timestamp end){
	    return (end.getTime() - begin.getTime()) / (24 * 60 * 60 * 1000);
	}
	
	/**
	 * begin 시작 시간과 end 기간과의 날짜 계산
	 * @param begin
	 * @param end
	 * @param format : 시작 , 종료 간의 날짜 포맷 yyyy-MM-dd HH:mm:ss
	 * @return
	 * @throws Exception   
	 */
	public static long diffOfDay(String begin, String end,String format){
		long term = 0;
		try{
		    Date beginDate = getDate(format, begin);
		    Date endDate = getDate(format,end);
		    term = (endDate.getTime() - beginDate.getTime()) / (24* 60 * 60 * 1000);
		}catch(Exception e){}
		return term;
	}
 
	/**
	 * 입력된 년월의 마지막 일수를 구하는 공식
	 * @param yyyyMM
	 * @return
	 */
	public static int getLastDayOfMonth(String yyyyMM){
		Calendar cal = Calendar.getInstance();
		cal.set(parseInt(yyyyMM.substring(0,4)),parseInt(yyyyMM.substring(4,6))-1,1);
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}


	/**
	 * yyyyMMdd 로 포맷화된 String 을  GregorianCalendar  로 변환하여 반환한다.
	 * @param yyyyMMdd
	 * @return
	 */
	public static GregorianCalendar getGregorianCalendar(String yyyyMMdd){
		int yyyy 	= parseInt(yyyyMMdd.substring(0,4));
		int mm 		= parseInt(yyyyMMdd.substring(4,6));
		int dd 		= parseInt(yyyyMMdd.substring(6));

		GregorianCalendar calendar = new GregorianCalendar(yyyy,mm-1,dd,0,0,0);
		return calendar;
	}
	
	/**
	 * 오늘의 요일을 반환한다. 
	 * @return
	 */
	public static int getDayOfWeek(){
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * Calendar.YEAR,Calendar.MONTH,Calendar.DATE , Amount , yyyyMMdd
	 * skfWk 
	 * @param field
	 * @param amount
	 * @param date
	 * @return
	 */
	public static String getOpDate(int field,int amount,String date){
		GregorianCalendar gCal = getGregorianCalendar(date);

		if(field == Calendar.YEAR){
			 gCal.add(GregorianCalendar.YEAR, amount);
		}else if(field == Calendar.MONTH){
			 gCal.add(GregorianCalendar.MONTH, amount);
		}else if(field == Calendar.DATE){
			 gCal.add(GregorianCalendar.DATE, amount);
		}else{

		}
		return getDateString("yyyyMMdd",gCal.getTime(), null);
	}
	
	public static String getAfterDate(String format,int minute) throws Exception{
		String currentDate = getCurrentDate(format);
		  try{
			  SimpleDateFormat df = new SimpleDateFormat(format);
			  Date d = df.parse(currentDate); 	
			  Calendar cal = Calendar.getInstance();
			  cal.setTime(d);
			  cal.add(Calendar.MINUTE, minute);
			  currentDate = df.format(cal.getTime());
		  }catch(Exception e){
			  throw new Exception(e);
		  }
		  return currentDate;
	  }
	
	
	public static String getAfterDate(String format,String currentDate,int minute) {
		  try{
			  SimpleDateFormat df = new SimpleDateFormat(format);
			  Date d = df.parse(currentDate); 	
			  Calendar cal = Calendar.getInstance();
			  cal.setTime(d);
			  cal.add(Calendar.MINUTE, minute);
			  currentDate = df.format(cal.getTime());
		  }catch(Exception e){
			  logger.info("error : {}",e.getMessage());
		  }
		  return currentDate;
	  }
	
	
	public static String leftTrim(String str){
        int i = 0;
        while (i < str.length() && Character.isWhitespace(str.charAt(i))) {
            str.charAt(i);
            i++;
        }
        return str.substring(i);
	}

	public static synchronized  Class classloader(String className)  { 
		try {
	        Class result =  ClassLoader.getSystemClassLoader().loadClass(className);
	         
	        if (result==null) {
	            throw new Exception("couldn't find constructor  "+className);
	        } 
	         
            return result;
	      
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static String arrayToString(Object[] obj){
		if(obj == null){return "";}
		StringBuilder sb =new StringBuilder();
		for(int i=0;i<obj.length;i++){
			sb.append(CommonUtil.toString(obj[i]));
			if(i !=0){
				sb.append(",");
			}
		}
		return sb.toString();
	}
	
	
	public static String cut(String str,int i){
		byte[] buf = str.getBytes();
		if(buf.length >= i){
			return new String(buf,0,i);
		}else{
			return str;
		}
	}
	
	
	public static String URLEncode(String str){
		try{
			str = URLEncoder.encode(str,"utf-8");
		}catch(Exception e){
		}
		return str;
	}
	
	
	public static String getAmountFormat(String amount){
		return CommonUtil.nToB(amount).replaceFirst("\\.0*$|(\\.\\d*?)0+$", "$1").replaceAll("^0+","");
	}
	
	
	
	
	
	
	
	
}
