package com.pgmate.sugi.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.sugi.util.CPUtil;

public class CodeDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.sugi.dao.CodeDAO.class );
	private static final String TABLE = "PG_CODE";
	private static final String COLUMNS = "`idx`, `alias`, `code`, `codeName`";
	
	public CodeDAO() {
		super(TABLE,CPUtil.CP_DEBUG);
		super.setColumns(CodeDAO.COLUMNS);
	}
	
	public RecordSet getById(String idx){
		addWhere("lower(idx)",idx.toLowerCase(),eq);
		return search();
	}
	
	public RecordSet getBank(){
		addWhere("alias", "BANK", eq);
		setOrderBy("");
		return search();
	}
	public String getBankName(String code){
		this.setColumns("codeName");
		addWhere("alias", "BANK", eq);
		addWhere("code", code, eq);
		setOrderBy("");
		RecordSet rset = search();
		return rset.getRow(0).getString("codeName");
	}
}
