package com.pgmate.sugi.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.map.SharedMap;
import com.pgmate.sugi.model.ajax.Data;
import com.pgmate.sugi.util.CPUtil;

/**
 * @author Administrator
 *
 */
public class SmsDAO extends DAO{
	private static Logger logger = LoggerFactory.getLogger( com.pgmate.sugi.dao.SmsDAO.class );
	private static final String TABLE = "TB_SMS_ORDER";
	private static final String COLUMNS = "*";

	public SmsDAO() {
	}
	

	public int checkSmsKey(String smsKey) {
		this.setTable("TB_SMS_ORDER");
		this.setColumns("count(*) AS count");
		this.addWhere("smsKey", smsKey, eq);
		this.setOrderBy("ins_dt desc");
		RecordSet rset = this.search();
		this.initRecord();
		return rset.getInt("count");
	}


	public boolean insertSMSPay(SharedMap<String, Object> smsPayMap) {
		super.setTable("TB_SMS_ORDER");
		
		super.setRecord("smsKey", smsPayMap.getString("smsKey"));
		super.setRecord("payKey", smsPayMap.getString("payKey"));
		super.setRecord("name", smsPayMap.getString("name"));
		super.setRecord("payerName", smsPayMap.getString("payerName"));
		super.setRecord("payerTel", smsPayMap.getString("payerTel"));
		super.setRecord("payerEmail", smsPayMap.getString("payerEmail"));
		super.setRecord("products", smsPayMap.getString("products"));
		super.setRecord("amount", smsPayMap.getString("amount"));
		boolean result =  super.insert();
		super.initRecord();
		return result;
	}


	public SharedMap<String, Object> getSmsPay(String smsKey) {
		this.setTable("TB_SMS_ORDER");
		this.setColumns("*");
		this.addWhere("smsKey",smsKey,eq);
		this.setOrderBy("ins_dt desc");
		RecordSet rset = search();
		this.initRecord();
		return rset.getRowFirst();
	}

	public SharedMap<String, Object> getSmsBill(String smsKey) {
		this.setTable("VW_TRX_PAY A RIGHT OUTER JOIN TB_SMS_ORDER B ON A.trxId = B.trxId");
		this.setColumns("A.trxId, A.regDay, A. regTime, A.amount, A.bin, A.last4, A.issuer, A.status, A.authCd");
		this.addWhere("B.smsKey",smsKey,eq);
		this.setOrderBy("ins_dt desc");
		RecordSet rset = search();
		this.initRecord();
		return rset.getRowFirst();
	}

	public boolean smsPayComplete(String trxId, String smsKey) {
		this.setTable("TB_SMS_ORDER");
		this.setRecord("status"	, "Y");
		this.setRecord("trxId"	, trxId);
		this.addWhere("smsKey", smsKey);
		this.setOrderBy("ins_dt desc");
		boolean result = this.update();
		this.initRecord();
		return result;
	}
	
}