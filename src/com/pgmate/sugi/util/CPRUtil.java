package com.pgmate.sugi.util;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.gson.GsonUtil;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;
import com.pgmate.lib.util.xml.XmlUtil;
import com.pgmate.sugi.export.CPDocument;
import com.pgmate.sugi.export.Template;
import com.pgmate.sugi.export.XlsExport;
import com.pgmate.sugi.model.ajax.CPRequest;
import com.pgmate.sugi.model.ajax.CPResponse;
import com.pgmate.sugi.model.ajax.Files;
import com.pgmate.sugi.model.ajax.Page;
import com.pgmate.sugi.model.ajax.Result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
/**
 * @author Administrator
 *
 */
public class CPRUtil {
	
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.sugi.util.CPRUtil.class );

	private CPResponse cpResponse 	= null;
	private CPDocument doc			= null;
	private LinkedHashMap<String,String> thead = null;
	
	public CPRUtil(CPRequest request) {
		if(CPUtil.CP_DEBUG){
			logger.debug("json request : {}",GsonUtil.toJson(request, true, ""));
		}
		cpResponse 			= new CPResponse();
		cpResponse.type 		= request.type;
		cpResponse.redirect 	= request.redirect;
		if(request.page != null){
			cpResponse.page		= request.page;
		}
		if(request.thead != null){
			cpResponse.thead		= request.thead;
		}
		if (request.reason != null && cpResponse.type.equals("excel") || cpResponse.type.equals("pdf")) {
			doc = new CPDocument(request.reason, "Export DATA", "SYSTEM");
		}

	}
	
	public CPRUtil(){
		this.cpResponse 			= new CPResponse();
	}
	/**
	 * 200,OK 응답 지정
	 * @return
	 */
	public CPRUtil resultOK(){
		return result(CPUtil.RESULT_OK,CPUtil.RESULT_OK_MSG,"");
	}
	
	public CPRUtil resultOK(String message){
		return result(CPUtil.RESULT_OK,message,"");
	}
	
	/**
	 * 600 등록 , 수정 실패 등.
	 * @param message
	 * @return
	 */
	public CPRUtil resultNOK(){
		return result(CPUtil.RESULT_NOK,CPUtil.RESULT_NOK_MSG,"");
	}

	/**
	 * 600 등록 , 수정 실패 등.
	 * @param message
	 * @return
	 */
	public CPRUtil resultNOK(String message){
		return result(CPUtil.RESULT_NOK,message,"");
	}
	/**
	 * 600 등록 , 수정 실패 등.
	 * @param message
	 * @param error
	 * @return
	 */ 
	public CPRUtil resultNOK(String message,String error){
		return result(CPUtil.RESULT_NOK,message,error);
	}

	
	public CPRUtil resultNOT_FOUND(){
		return result(CPUtil.RESULT_NOK,CPUtil.RESULT_NOT_FOUND_MSG,"");
	}
	
	/**
	 * 601 검색 결과가 없을 경우 
	 * @param message
	 * @return
	 */
	public CPRUtil resultNOT_FOUND(String message){
		return result(CPUtil.RESULT_NOT_FOUND,message,"");
	}
	/**
	 * 601 검색 결과가 없을 경우 
	 * @param message
	 * @param error
	 * @return
	 */ 
	public CPRUtil resultNOT_FOUND(String message,String error){
		return result(CPUtil.RESULT_NOT_FOUND,message,error);
	}
	
	
	public CPRUtil resultACCESS_DENIED(){
		return result(CPUtil.RESULT_ACCESS_DENIED,CPUtil.RESULT_ACCESS_DENIED_MSG,"");
	}

	/**
	 * 602 접근 거부 메세지
	 * @param message
	 * @return
	 */
	public CPRUtil resultACCESS_DENIED(String message){
		return result(CPUtil.RESULT_ACCESS_DENIED,message,"");
	}
	/**
	 * 602 접근 거부 메세지
	 * @param message
	 * @param error
	 * @return
	 */ 
	public CPRUtil resultACCESS_DENIED(String message,String error){
		return result(CPUtil.RESULT_ACCESS_DENIED,message,error);
	}
	

	
	public CPRUtil resultSESSION_EXPIRED(){
		return result(CPUtil.RESULT_SESSION_EXPIRED,CPUtil.RESULT_SESSION_EXPIRED_MSG,"");
	}
	
	/**
	 * 603 세션 종료
	 * @param message
	 * @return
	 */
	public CPRUtil resultSESSION_EXPIRED(String message){
		return result(CPUtil.RESULT_SESSION_EXPIRED,message,"");
	}
	/**
	 * 603 세션 종료
	 * @param message
	 * @param error
	 * @return
	 */ 
	public CPRUtil resultSESSION_EXPIRED(String message,String error){
		return result(CPUtil.RESULT_SESSION_EXPIRED,message,error);
	}
	
	public CPRUtil resultBAD_REQUEST(){
		return result(CPUtil.RESULT_BAD_REQUEST,CPUtil.RESULT_BAD_REQUEST_MSG,"");
	}
	
	/**
	 * 604 잘 못된 요청 이 수신된 경우
	 * @param message
	 * @return
	 */
	public CPRUtil resultBAD_REQUEST(String message){
		return result(CPUtil.RESULT_BAD_REQUEST,message,"");
	}
	/**
	 *604 잘 못된 요청 이 수신된 경우
	 * @param message
	 * @param error
	 * @return
	 */ 
	public CPRUtil resultBAD_REQUEST(String message,String error){
		return result(CPUtil.RESULT_BAD_REQUEST,message,error);
	}
	
	/**
	 * 700 시스템 오류
	 * @param message
	 * @return
	 */
	public CPRUtil resultERROR(String message){
		return result(CPUtil.RESULT_ERROR,message,"");
	}
	/**
	 * 700 시스템 오류
	 * @param message
	 * @param error
	 * @return
	 */ 
	public CPRUtil resultERROR(String message,String error){
		return result(CPUtil.RESULT_ERROR,message,error);
	}
	
	
	
	
	/**
	 * 코드,메세지,에러 메세지 직접 회신 
	 * @param code
	 * @param message
	 * @param error
	 * @return
	 */
	public CPRUtil result(int code,String message,String error){
		Result result	= new Result();
		result.code 	= code;
		result.message 	= message;
		result.error 	= error;
		cpResponse.result = result;
		return this;
	}
	
	/**
	 * rediect 변수 값 직접 지정 
	 * @param redirect
	 * @return
	 */
	public CPRUtil redirect(String redirect){
		cpResponse.redirect = redirect;
		return this;
	}
	
	/**
	 * search 형 데이터 set
	 * @param rset
	 * @return
	 */
	public CPRUtil data(RecordSet rset){

		if(cpResponse.result == null){
			if(rset.size() == 0){
				resultNOT_FOUND(CPUtil.RESULT_NOT_FOUND_MSG);
			}else{
				resultOK();
			}
		}
		if(cpResponse.type.equals(CPUtil.CP_TYPE_LIST) || cpResponse.type.equals(CPUtil.CP_TYPE_SEARCH)){
			cpResponse.data = rset.getRows();
			setSummary();
		}
		
		return this;
	}
	
	/**
	 * list 형 테이터 지정 
	 * @param dao
	 * @param rset
	 * @return
	 */
	public CPRUtil dataList(RecordSet rset,DAO dao){
		//EXCEL , PDF
		if(cpResponse.type.equals(CPUtil.CP_TYPE_FILE_EXL) || cpResponse.type.equals(CPUtil.CP_TYPE_FILE_PDF)){
			if(doc == null){
				doc = new CPDocument("AutoExport", "Export DATA", "SYSTEM");
			}
			
			LinkedHashMap <String,String> thead = new LinkedHashMap <String,String>();
			if(cpResponse.thead != null) {
				String[] dhead = CommonUtil.split(cpResponse.thead, ",", true);
				for(String head : dhead){
					if(head.indexOf(":") > -1){
						String[] data = CommonUtil.split(head, ":", true);
						thead.put(data[0], data[1]);
					}else{
						thead.put(head, head);
					}
				}
			}
			export(doc, thead);
		}
		
		setDefaultData(rset,dao);
		//결과 메세지가 없을 경우 값 설정
		if(cpResponse.result == null){
			if(rset.size() == 0){
				resultNOT_FOUND(CPUtil.RESULT_NOT_FOUND_MSG);
			}else{
				resultOK();
			}
		}
		
		if(cpResponse.type.equals(CPUtil.CP_TYPE_LIST) || cpResponse.type.equals(CPUtil.CP_TYPE_SEARCH)){
			cpResponse.data = rset.getRows();
			setSummary();
		}
		
		
		
		
		return this;
	}
	
	/**
	 * 파일 링크시 파일 정보 설정 
	 * @param link
	 * @param auth
	 * @return
	 */
	private void setFile(String link,String auth){
		Files file 		= new Files();
		file.link 		= link;
		file.auth 		= auth;
		cpResponse.file = file;
	}
	
	
	public CPRUtil export(CPDocument doc,LinkedHashMap<String,String> thead){
		this.doc = doc;
		this.thead = thead;
		return this;
	}
	
	/**
	 * 응답 CPResponse 반환
	 * @return
	 */
	public CPResponse cpResponse(){
		if(CPUtil.CP_DEBUG){
			logger.debug("json response : {}",cpResponseJson());
		}
		return cpResponse;
	}
	public ModelAndView setView(HttpServletRequest webRequest,String view,String cpResponseName ){
		if(CPUtil.CP_DEBUG){
			logger.debug("json response : {}",cpResponseJson());
		}
		if(webRequest == null){
			logger.debug("HttpServletRequest is null please set HttpServletRequest");
		}else{
			if(CommonUtil.isNullOrSpace(cpResponseName)){
				webRequest.setAttribute("CPR", cpResponse);
			}else{
				webRequest.setAttribute(cpResponseName, cpResponse);
			}
		}
		
		if(cpResponse.type.equals(CPUtil.CP_TYPE_FILE_EXL) || cpResponse.type.equals(CPUtil.CP_TYPE_FILE_PDF)){
			cpResponse.page = null;
			cpResponse.thead = null;
			return new ModelAndView("/common/jsonResponse","message",GsonUtil.toJson(cpResponse));
		}else{
			
			return new ModelAndView(view);
		}
	}
	
	public String cpResponseJson(){
		boolean pretty = true;
		if(cpResponse.data != null &&  cpResponse.data.size() > 5){
			pretty = false;
		}
		return GsonUtil.toJson(cpResponse,pretty,"");
	}
	
	public String cpResponseXml(){
		return XmlUtil.toXml(cpResponse,true,"utf-8");
	}
	
	private void setDefaultData(RecordSet rset,DAO dao){
		//리스트 일경우만 PAGING 을 제공한다.
		if(cpResponse.type.equals(CPUtil.CP_TYPE_LIST)){
			if(cpResponse.page == null){	
				cpResponse.page = CPUtil.correctPage(new Page());
			}			
		
			cpResponse.page.total 	= dao.getTotal();
			cpResponse.page.hash 	= dao.getHash();
			cpResponse.page.totalPage = cpResponse.page.total/cpResponse.page.size;
			if(cpResponse.page.total%cpResponse.page.size != 0){
				cpResponse.page.totalPage++;
			}
		}else if(cpResponse.type.equals(CPUtil.CP_TYPE_FILE_PDF)){
			if(cpResponse.file == null && rset.size() !=0){
				Template t = new Template();
				if(doc != null){
					t.setDocument(doc);
				}
				String link = "";
				try{
					if(CommonUtil.isNullOrSpace(cpResponse.thead)){
						link = "";
					}else{
						link = t.export(thead, rset,CPUtil.CP_TYPE_FILE_PDF );
					}
				}catch(Exception e){
					link = e.getMessage();
				}
				setFile(link,"authkey");
			}
		}else if(cpResponse.type.equals(CPUtil.CP_TYPE_FILE_EXL)){
			if(cpResponse.file == null && rset.size() !=0){
				XlsExport export = new XlsExport(doc);
				String link = "";
				try{
					if(CommonUtil.isNullOrSpace(cpResponse.thead)){
						link = "";
					}else{
						link = export.makeExcel(thead, rset, true, true);
					}
				}catch(Exception e){
					link = e.getMessage();
				}
				
				setFile(link, "authkey");
			}
		}else{
			
		} 
	}
	
	
	private void setSummary(){
		
		if(cpResponse.data == null){return;}
		if(cpResponse.data.size() > 0){
			cpResponse.sum = new SharedMap<String,Object>();
			for(int i=0 ; i< cpResponse.data.size() ; i++){
				SharedMap<String,Object> dataMap = cpResponse.data.get(i);	
				for(String key : dataMap.keySet()){
					if(i== 0){	//initialize
						cpResponse.sum.put(key,0);
					}
					
						cpResponse.sum.put(key, cpResponse.sum.getDouble(key)+dataMap.getDouble(key));
					
					
				}
			}
			
		}
	}


}
