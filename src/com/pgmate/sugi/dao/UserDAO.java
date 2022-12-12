package com.pgmate.sugi.dao;

import com.pgmate.lib.util.db.DBFactory;
import com.pgmate.lib.util.db.DBManager;
import com.pgmate.sugi.util.SQLInjectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
		String query = " SELECT A.*, B.name AS mchtName, B.nick AS mchtNick FROM PG_MCHT_TMN A LEFT JOIN PG_MCHT B ON A.mchtId = B.mchtId" +
				"WHERE A.tmnId=? AND A.serial=? AND A.webPay='사용' AND A.status='사용' AND B.status='사용'";

		RecordSet rset = new RecordSet();
		DBManager db = null;
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet resultSet = null;

		try {
			db = DBFactory.getInstance();
			conn = db.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, SQLInjectionUtil.changeValue(tmnId));
			pstmt.setString(2, SQLInjectionUtil.changeValue(serial));
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

		return rset;
	}

	public RecordSet getTmn(String tmnId) {
		String query = " SELECT A.*, B.name AS mchtName, B.nick AS mchtNick FROM PG_MCHT_TMN A LEFT JOIN PG_MCHT B ON A.mchtId = B.mchtId" +
				"WHERE A.tmnId=? AND A.webPay='사용' AND A.status='사용' AND B.status='사용'";

		RecordSet rset = new RecordSet();
		DBManager db = null;
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet resultSet = null;

		try {
			db = DBFactory.getInstance();
			conn = db.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, SQLInjectionUtil.changeValue(tmnId));
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

		return rset;
	}
	
	public RecordSet getTmnDtl(String tmnId) {
		String query = " SELECT * FROM PG_MCHT_TMN_DTL WHERE tmnId=?";

		RecordSet rset = new RecordSet();
		DBManager db = null;
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet resultSet = null;

		try {
			db = DBFactory.getInstance();
			conn = db.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1,  SQLInjectionUtil.changeValue(tmnId));
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

		return rset;
	}
	
	public RecordSet getSettleAccnt(String tmnId) {
		String query = " SELECT B.* FROM PG_MCHT_TMN A LEFT JOIN PG_MCHT_TAX B ON A.taxId = B.taxId WHERE A.tmnId=?";

		RecordSet rset = new RecordSet();
		DBManager db = null;
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet resultSet = null;

		try {
			db = DBFactory.getInstance();
			conn = db.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1,  SQLInjectionUtil.changeValue(tmnId));
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

		return rset;
	}
	
}