package com.pgmate.sugi.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;

/**
 * @author Administrator
 *
 */
public class UserDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.sugi.dao.UserDAO.class );
	private static final String TABLE = "PG_MCHT_TMN";
	private static final String COLUMNS = "*";

	public UserDAO() {
	}
	

	public RecordSet getTmnLogin(String tmnId, String serial) {
		this.setTable("PG_MCHT_TMN A LEFT JOIN PG_MCHT B ON  A.mchtId = B.mchtId");
		this.setColumns("A.*, B.name AS mchtName, B.nick AS mchtNick");
		this.addWhere("A.tmnId",tmnId,eq);
		this.addWhere("A.serial",serial,eq);
		this.addWhere("A.webPay","사용",eq);
		this.addWhere("A.status","사용",eq);
		this.addWhere("B.status","사용",eq);
		RecordSet rset = search();
		this.initRecord();
		return rset;
	}

	public RecordSet getTmn(String tmnId) {
		this.setDebug(true);
		this.setTable("PG_MCHT_TMN A LEFT JOIN PG_MCHT B ON  A.mchtId = B.mchtId");
		this.setColumns("A.*, B.name AS mchtName, B.nick AS mchtNick");
		this.addWhere("A.tmnId",tmnId,eq);
		this.addWhere("A.webPay","사용",eq);
		this.addWhere("A.status","사용",eq);
		this.addWhere("B.status","사용",eq);
		RecordSet rset = search();
		this.initRecord();
		return rset;
	}
	
	public RecordSet getTmnDtl(String tmnId) {
		this.setTable("PG_MCHT_TMN_DTL");
		this.setColumns("*");
		this.addWhere("tmnId",tmnId,eq);
		RecordSet rset = search();
		this.initRecord();
		return rset;
	}
	
	public RecordSet getSettleAccnt(String tmnId) {
		this.setTable("PG_MCHT_TMN A LEFT JOIN PG_MCHT_TAX B ON A.taxId = B.taxId");
		this.setColumns("B.*");
		this.addWhere("A.tmnId",tmnId,eq);
		RecordSet rset = search();
		this.initRecord();
		return rset;
	}
	
}