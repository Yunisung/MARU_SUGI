package com.pgmate.sugi.ctl;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.gson.GsonUtil;

@Controller
public class CheckController {
	private static Logger logger = LoggerFactory.getLogger(com.pgmate.sugi.ctl.CheckController.class );
	
	@RequestMapping(value = "/check", method = RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String check(HttpServletRequest request) {
		logger.info("===================CheckController" );
		String check = "FAIL";
		
		DAO dao = new DAO();
		
		dao.setTable("DUAL");
		dao.setColumns("now() as time");
		dao.setOrderBy("time");
		RecordSet rset = dao.search();
		while(rset.next()) {
			check = "OK";
		}
		
		logger.info("===================CheckController RES : {}", check);
		return check;
    }
}
