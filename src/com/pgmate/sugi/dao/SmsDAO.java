package com.pgmate.sugi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import com.pgmate.lib.util.db.DBFactory;
import com.pgmate.lib.util.db.DBManager;
import com.pgmate.sugi.util.SQLInjectionUtil;
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
		String query = " SELECT count(*) AS count FROM TB_SMS_ORDER WHERE smsKey=? ORDER BY ins_dt desc";

		RecordSet rset = new RecordSet();
		DBManager db = null;
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet resultSet = null;

		try {
			db = DBFactory.getInstance();
			conn = db.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1,  SQLInjectionUtil.changeValue(smsKey));
			pstmt.executeQuery();
			resultSet = pstmt.getResultSet();

			if(resultSet != null) {
				rset = new RecordSet(resultSet);
			}
		} catch (Exception e) {
			logger.debug("sql error : {}, query : {}", e.getMessage(), query);
		} finally {
			db.close(conn, pstmt, resultSet);
		}

		return rset.getInt("count");
	}


	public boolean insertSMSPay(SharedMap<String, Object> smsPayMap) {
		String query = " INSERT INTO TB_SMS_ORDER (smskey, paykey, name, payerName, payerTel, payerEmail, products, amount) " +
				"VALUES (?,?,?,?,?,?,?,?)";

		RecordSet rset = new RecordSet();
		DBManager db = null;
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet resultSet = null;

		try {
			db = DBFactory.getInstance();
			conn = db.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1,  SQLInjectionUtil.changeValue(smsPayMap.getString("smsKey")));
			pstmt.setString(2,  SQLInjectionUtil.changeValue(smsPayMap.getString("payKey")));
			pstmt.setString(3,  SQLInjectionUtil.changeValue(smsPayMap.getString("name")));
			pstmt.setString(4,  SQLInjectionUtil.changeValue(smsPayMap.getString("payerName")));
			pstmt.setString(5,  SQLInjectionUtil.changeValue(smsPayMap.getString("payerTel")));
			pstmt.setString(6,  SQLInjectionUtil.changeValue(smsPayMap.getString("payerEmail")));
			pstmt.setString(7,  SQLInjectionUtil.changeValue(smsPayMap.getString("products")));
			pstmt.setString(8,  SQLInjectionUtil.changeValue(smsPayMap.getString("amount")));
			pstmt.executeQuery();
			resultSet = pstmt.getResultSet();

			if(resultSet != null) {
				rset = new RecordSet(resultSet);
			}
		} catch (Exception e) {
			logger.debug("sql error : {}, query : {}", e.getMessage(), query);
		} finally {
			db.close(conn, pstmt, resultSet);
		}

		if(rset.size() > 0)
			return true;
		else
			return false;
	}


	public SharedMap<String, Object> getSmsPay(String smsKey) {
		String query = " SELECT *  FROM TB_SMS_ORDER WHERE smsKey=? ORDER BY ins_dt desc";

		RecordSet rset = new RecordSet();
		DBManager db = null;
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet resultSet = null;

		try {
			db = DBFactory.getInstance();
			conn = db.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1,  SQLInjectionUtil.changeValue(smsKey));

			pstmt.executeQuery();
			resultSet = pstmt.getResultSet();

			if(resultSet != null) {
				rset = new RecordSet(resultSet);
			}
		} catch (Exception e) {
			logger.debug("sql error : {}, query : {}", e.getMessage(), query);
		} finally {
			db.close(conn, pstmt, resultSet);
		}

		return rset.getRowFirst();

	}
	
	public SharedMap<String, Object> getMaxInstall(String payKey) {
		String query = " SELECT apiMaxInstall AS count FROM PG_MCHT_TMN WHERE payKey=? ";

		RecordSet rset = new RecordSet();
		DBManager db = null;
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet resultSet = null;

		try {
			db = DBFactory.getInstance();
			conn = db.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1,  SQLInjectionUtil.changeValue(payKey));

			pstmt.executeQuery();
			resultSet = pstmt.getResultSet();

			if(resultSet != null) {
				rset = new RecordSet(resultSet);
			}
		} catch (Exception e) {
			logger.debug("sql error : {}, query : {}", e.getMessage(), query);
		} finally {
			db.close(conn, pstmt, resultSet);
		}


		return rset.getRowFirst();
	}
	
	public SharedMap<String, Object> getMaxInstall(String payKey) {
		this.setTable("PG_MCHT_TMN");
		this.setColumns("apiMaxInstall");
		this.addWhere("payKey", payKey, eq);
		RecordSet rset = search();
		this.initRecord();
		return rset.getRowFirst();
	}

	public SharedMap<String, Object> getSmsBill(String smsKey) {
		String query = " SELECT A.trxId, A.regDay, A. regTime, A.amount, A.bin, A.last4, A.issuer, A.status, A.authCd "
		+ "FROM VW_TRX_PAY A RIGHT OUTER JOIN TB_SMS_ORDER B ON A.trxId = B.TrxId WHERE B.smsKey=? ORDER BY ins_dt desc";

		RecordSet rset = new RecordSet();
		DBManager db = null;
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet resultSet = null;

		try {
			db = DBFactory.getInstance();
			conn = db.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1,  SQLInjectionUtil.changeValue(smsKey));

			pstmt.executeQuery();
			resultSet = pstmt.getResultSet();

			if(resultSet != null) {
				rset = new RecordSet(resultSet);
			}
		} catch (Exception e) {
			logger.debug("sql error : {}, query : {}", e.getMessage(), query);
		} finally {
			db.close(conn, pstmt, resultSet);
		}


		return rset.getRowFirst();
	}

	public boolean smsPayComplete(String trxId, String smsKey) {
		String query = " UPDATE TB_SMS_ORDER SET status='Y', trxId=? WHERE smsKey=? ORDER BY ins_dt desc ";

		RecordSet rset = new RecordSet();
		DBManager db = null;
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet resultSet = null;

		try {
			db = DBFactory.getInstance();
			conn = db.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1,  SQLInjectionUtil.changeValue(trxId));
			pstmt.setString(2,  SQLInjectionUtil.changeValue(smsKey));
			pstmt.executeQuery();
			resultSet = pstmt.getResultSet();

			if(resultSet != null) {
				rset = new RecordSet(resultSet);
			}
		} catch (Exception e) {
			logger.debug("sql error : {}, query : {}", e.getMessage(), query);
		} finally {
			db.close(conn, pstmt, resultSet);
		}

		if(rset.size() > 0)
			return true;
		else
			return false;
	}
	
}