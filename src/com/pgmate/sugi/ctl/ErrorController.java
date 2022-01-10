package com.pgmate.sugi.ctl;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.sugi.interceptor.SessionExclude;
import com.pgmate.sugi.model.ajax.CPResponse;
import com.pgmate.sugi.model.ajax.Result;
import com.pgmate.sugi.util.CPUtil;

/**
 * @author Administrator
 *
 */
@Controller
public class ErrorController {

	private static Logger logger = LoggerFactory.getLogger( com.pgmate.sugi.ctl.ErrorController.class );
	
	@RequestMapping(path="/error", produces="application/json" ,headers="content-type=application/json")
	@SessionExclude
    public ResponseEntity<CPResponse> handle(HttpServletRequest request,Exception e) {
		CPResponse cpResponse = new CPResponse();
		Result result = new Result();
		result.code	= CommonUtil.parseInt(request.getAttribute("javax.servlet.error.status_code"));
		if(result.code >= 500){
			result.code = CPUtil.RESULT_ERROR;
		}else if(399 < result.code && result.code < 500){
			result.code = CPUtil.RESULT_BAD_REQUEST;
		}
		result.message= "URI="+request.getRequestURI()+",msg="+e.getMessage();
		result.error	= CommonUtil.toString(request.getAttribute("javax.servlet.error.message"));
		cpResponse.result = result;
        
        return new ResponseEntity<CPResponse>(cpResponse, HttpStatus.OK);
    }
	
	@RequestMapping(path="/error")
	@SessionExclude
    public ModelAndView handle2(HttpServletRequest request,Exception e) {
		Result result = new Result();
		result.code	= CommonUtil.parseInt(request.getAttribute("javax.servlet.error.status_code"));
		/*
		if(result.code >= 500){
			result.code = CPUtil.RESULT_ERROR;
		}else if(399 < result.code && result.code < 500){
			result.code = CPUtil.RESULT_BAD_REQUEST;
		}*/
		result.message= "URI="+request.getRequestURI()+",msg="+e.getMessage();
		result.error	= getStackTraceAsString(e);
		logger.debug("error ,code : {}, message : {} , error : {}",result.code,result.message,result.error);
        
        return new ModelAndView("/common/error","result",result);
    }
	
	
	
	
	public String getStackTraceAsString(Exception e) {
	  ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	  PrintWriter writer = new PrintWriter(bytes, true);
	  e.printStackTrace(writer);
	  return bytes.toString();
	}


}
