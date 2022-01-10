package com.pgmate.sugi.ctl;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.gson.GsonUtil;
import com.pgmate.sugi.util.CPUtil;
import com.pgmate.sugi.util.SessionUtil;

@Controller
public class CommonController {
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.sugi.ctl.CommonController.class );
	
	@RequestMapping(value = {"/common/changeIdentity"})
    public ModelAndView changeIdentity(HttpServletRequest request) {
		return new ModelAndView("/common/changeIdentity");
    }
	
	@RequestMapping(value = {"/common/debug"}, method = RequestMethod.GET)
    public @ResponseBody String debug(HttpServletRequest request) {
		if(CPUtil.CP_DEBUG){
			CPUtil.CP_DEBUG = false;
			SessionUtil.setAttribute(request, "CP_DEBUG",false);
			logger.debug("DEBUG : {}", CPUtil.CP_DEBUG);
			return "DEBUG 중지처리되었습니다.";
		}else{
			CPUtil.CP_DEBUG = true;
			SessionUtil.setAttribute(request, "CP_DEBUG",true);
			return "DEBUG 가 시작되었습니다.";
		}
	}
	
	@RequestMapping(value = "/common/typeahead/{key}/{keyword}", method = RequestMethod.GET)
    public @ResponseBody String typeahead(HttpServletRequest request, @PathVariable String key, @PathVariable String keyword) {
		ArrayList<String> resultArray = new ArrayList<>();
		DAO dao = new DAO();
		
		if(key.equalsIgnoreCase("ptnName")) {
			dao.setTable("WL_PTN");
			dao.setColumns("name as resKey");
			dao.addWhere("name", keyword, DAO.lk);
		} else if(key.equalsIgnoreCase("mchtName")) {
			dao.setTable("PG_MCHT");
			dao.setColumns("name as resKey");
			dao.addWhere("name", keyword, DAO.lk);
		}else if(key.equalsIgnoreCase("mchtNameId")) {
			dao.setTable("PG_MCHT");
			dao.setColumns("concat(name,' ||',mchtId) as resKey");
			dao.addWhere("name", keyword, DAO.lk);
		} else {
			return "";
		}
		RecordSet rset = dao.search();
		while(rset.next()) {
			resultArray.add(rset.getString("resKey"));
		}
		
        return GsonUtil.toJson(resultArray);
    }
	
	@RequestMapping(value = "/common/mchtList/{agencyId}", method = RequestMethod.GET)
    public @ResponseBody String getMchtList(HttpServletRequest request, @PathVariable String agencyId) {
		
		DAO dao = new DAO();
		dao.setTable("PG_MCHT");
		dao.setColumns("name,mchtId");
		dao.addWhere("agencyId", agencyId, DAO.eq);
		dao.addWhere("status", "사용", DAO.eq);
		dao.setLimit(999999);
		dao.setOrderBy("name asc");
		
        return GsonUtil.toJson(dao.search().getRows());
    }
	
	@RequestMapping(value = "/common/mchtList/byvan/{van}", method = RequestMethod.GET)
    public @ResponseBody String getMchtByVanList(HttpServletRequest request, @PathVariable String van) {
		DAO dao = new DAO();
		dao.setTable("PG_MCHT_TMN A, PG_MCHT B");
		dao.setColumns("B.mchtId,B.name");
		dao.setWhere("A.mchtId = B.mchtId");
		dao.addWhere("A.status", "사용", DAO.eq);
		dao.addWhere("B.status", "사용", DAO.eq);
		if(!van.equals("DEFAULT")) {
			dao.addWhere("A.van", van, DAO.eq);
		}
		dao.setLimit(999999);
		dao.setGroupBy("B.mchtId");
		dao.setOrderBy("B.name asc");
		
        return GsonUtil.toJson(dao.search().getRows());
    }
	
	@RequestMapping(value = "/common/tmnList/{mchtId}", method = RequestMethod.GET)
    public @ResponseBody String getMchtTmnList(HttpServletRequest request, @PathVariable String mchtId) {
		
		DAO dao = new DAO();
		dao.setTable("PG_MCHT_TMN");
		dao.setColumns("tmnId,van,description");
		dao.addWhere("mchtId", mchtId, DAO.eq);
		dao.addWhere("status", "사용", DAO.eq);
		dao.setLimit(999999);
		dao.setOrderBy("tmnId asc");
		return GsonUtil.toJson(dao.search().getRows());
    }
	
	
	
	
	
}
