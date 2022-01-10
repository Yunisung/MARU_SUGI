package com.pgmate.sugi.util;

import java.io.BufferedReader;
import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.io.FileUtil;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;
import com.pgmate.sugi.model.ajax.Data;
import com.pgmate.sugi.model.ajax.Page;

/**
 * @author Administrator
 *
 */
public class CPUtil {
	private static Logger logger = LoggerFactory.getLogger(com.pgmate.sugi.util.CPUtil.class);

	public static final String CP_TYPE_INSERT 		= "insert";		//RECORD INSERT
	public static final String CP_TYPE_UPDATE		= "update";		//RECORD UPDATE
	public static final String CP_TYPE_SEARCH		= "search";		//단일 RECORD  요청
	public static final String CP_TYPE_LIST			= "list";		//LIST RECORD 요청
	public static final String CP_TYPE_UPLOAD		= "upload";		//UPLOAD 결과
	public static final String CP_TYPE_DONWLOAD		= "down";		//UPLOAD 결과
	public static final String CP_TYPE_FILE_PDF 	= "pdf";		//PDF File 요청
	public static final String CP_TYPE_FILE_EXL		= "excel";		//Excel File 요청
	public static final String CP_TYPE_FILE_TXT		= "txt";		//Text File 요청
	public static final String CP_TYPE_FILE_CSV		= "csv";		//CSV File 요청
	public static final String CP_TYPE_FILE_IMG		= "image";		//IMAGE File 요청
	public static final String CP_TYPE_FILE_HTML	= "html";		//HTML File 요청
	public static final String CP_TYPE_ACCESS		= "access";		//PAGE 접근 권한 요청 
	
	
	
	public static final int RESULT_OK				= 200;			//성공 결과 
	public static final int RESULT_NOK				= 600;			//실패 등.	
	public static final int RESULT_NOT_FOUND		= 601;			//결과 검색 실패 
	public static final int RESULT_ACCESS_DENIED	= 602;			//엑세스 거부
	public static final int RESULT_SESSION_EXPIRED	= 603;			//세션 종료
	public static final int RESULT_BAD_REQUEST		= 604;			//요청 값에 오류가 있을 경우 
	public static final int RESULT_ERROR			= 700;			//시스템 오류
	
	
	public static final String RESULT_OK_MSG			= "OK";			
	public static final String RESULT_NOK_MSG			= "NOK";
	public static final String RESULT_NOT_FOUND_MSG		= "데이터가 없습니다.";
	public static final String RESULT_ACCESS_DENIED_MSG	= "access denied";
	public static final String RESULT_SESSION_EXPIRED_MSG= "session expired";
	public static final String RESULT_BAD_REQUEST_MSG	=  "bad request";		 
	public static final String RESULT_ERROR_MSG			= "system error";
	
	
	public static final String RESULT_DATA_INSERTED	 	= "등록 성공하였습니다.";
	public static final String RESULT_DATA_UPDATED	 	= "업데이트 성공하였습니다.";
	public static final String RESULT_DATA_DELETED	 	= "data deleted";
	public static final String RESULT_DATA_INFAIL		= "등록 실패하였습니다.";
	public static final String RESULT_DATA_UPFAIL	 	= "업데이트 실패하였습니다.";
	public static final String RESULT_DATA_DELFAIL	 	= "delete failure";
	
	public static final String CP_SESSION			= "CP_SESSION";	//Session Attribute NAME
	public static final int CP_SESSION_TIMEOUT		= 30*60;		//30 minutes
	
	public static boolean CP_DEBUG					= false;
	public static boolean CP_DEV_SESSION			= false;
	
	public static final String CP_UPLOAD_DIR		= "/upload";
	public static final String CP_TEMPLATE_DIR		= "template";
	
	public static String getUploadDir() {
		String path = "";
		switch (System.getProperty("os.name")) {
			case "Linux":
				path = "/home/data/app";
				break;
		    case "Windows":
		    	path = "D://tmp//upload";
		    	break;
		}
		return path;
	}
	
	public static Page correctPage(Page page){
		if(page == null){
			page = new Page();
		}
		if(page.current == 0){
			page.current = 1;
		}
		if(page.size == 0){
			page.current = 10;
		}
		
		return page;
		
		
	}
	
	
	public static void setDAO(DAO dao,List<Data> datas){
		if(datas == null){
			return;
		}
		StringBuilder orderBy = new StringBuilder(); 
		for(Data data:datas){
			if(data.key){
				if(!CommonUtil.toString(data.val).equals("")) {
					String str = CommonUtil.toString(data.val);
					String convaerted = SQLInjectionUtil.changeValue(str);
					if(!str.equalsIgnoreCase(convaerted)) {
						data.val = convaerted;
						logger.warn("==== SQL INJECTION C HECK : {} => {}", str, convaerted);
					}
				}
				dao.addWhere(data.name,data.val,data.oper);
			}else{
				dao.setRecord(data.name, data.val);
			}
			if(!data.order.equals("")){
				if(orderBy.length() !=0){
					orderBy.append(",");
				}
				orderBy.append(data.name);
				orderBy.append(" ");
				orderBy.append(data.order);
			}
		}
		if(orderBy.toString().trim().length() > 1){
			dao.setOrderBy(orderBy.toString());
		}
	}
	
	
	
	public static void setRedisDAO(DAO dao,List<Data> datas){
		if(datas == null){
			return;
		}
		
		for(Data data:datas){
			if(data.key){
			}else{
				dao.setRecord(data.name, CommonUtil.toString(data.val));
			}	
		}
		
	}
	
	
	public static String postToString(HttpServletRequest request){
		StringBuilder buf = new StringBuilder();
		String line = null;
			try {
				BufferedReader reader = request.getReader();
				while ((line = reader.readLine()) != null)
					buf.append(line);
			} catch (Exception e) { /*report an error*/ }

		return buf.toString();
	}
	
	
	public static void setUploadDirectory(){
		FileUtil fileUtil = new FileUtil();
		String path = CPUtil.getUploadDir()+CommonUtil.getCurrentDate("yyyyMMdd");
		if(!fileUtil.existDirectory(CPUtil.getUploadDir())){
			fileUtil.createDirectory(CPUtil.getUploadDir());
		}
		if(!fileUtil.existDirectory(path)){
			fileUtil.createDirectory(path);
		}
	}
	
	public static void setTemplateDirectory(String directory){
		FileUtil fileUtil = new FileUtil();
		String path = directory+CommonUtil.getCurrentDate("yyyyMMdd");
		if(!fileUtil.existDirectory(directory)){
			fileUtil.createDirectory(directory);
		}
		if(!fileUtil.existDirectory(path)){
			fileUtil.createDirectory(path);
		}
	}
	
	
	public static void merge(RecordSet rset, RecordSet add){
		for(SharedMap<String,Object> data:add.getRows()){
			rset.addRow(data);
		}
	}
	
	public static RecordSet mergeValue(RecordSet rset, RecordSet add){
		RecordSet newRecord = new RecordSet();
		SharedMap<String,Object> data = (SharedMap<String,Object>)rset.getRows().get(0);
		if(add.size() > 0){
			SharedMap<String,Object> data2 = (SharedMap<String,Object>)add.getRows().get(0);
			data.putAll(data2);
		}
		newRecord.addRow(data);
		return newRecord;
	}
	
	
	
	
	public static String getCanonicalPath(){
		String path = "";
		try{
			path = new File("../").getCanonicalPath();
		}catch(Exception e){}
		return path;
	}
	
	public static String getCanonicalWebPath(){
		String path = "";
		try{
			path = new File("../web").getCanonicalPath();
		}catch(Exception e){}
		return path;
	}
	
	public static String getCanonicalTemplatePath(){
		return getCanonicalPath()+File.separator+"web"+File.separator+"assets"+File.separator+"tpl";
	}
	
	
}
