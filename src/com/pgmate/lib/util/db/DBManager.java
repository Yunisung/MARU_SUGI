package com.pgmate.lib.util.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.lib.conf.ConfigLoader;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.lang.BeanUtil;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

/**
 * @author Administrator
 *
 */
public abstract class DBManager {

	private static Logger logger = LoggerFactory.getLogger( com.pgmate.lib.util.db.DBManager.class);
	private static DBConfigBean dbConfigBean 		= null;
	private static DBManager dbManager 				= null;
	private boolean debug							= false;
	private String error						= "";
	private int defaultTimeout						= 30000;
	
	
	public static DBManager getManager(String dbName) throws Exception {
		dbConfigBean = ConfigLoader.getConfig().db;
		if(dbManager == null) {
			getInstance();
		}
		return dbManager;
	}
	
	private static void getInstance() throws Exception {
		synchronized(DBManager.class) {
			try{
				if(dbConfigBean.getDbType().equals("WEB")){
					dbManager = new JNDIManager(dbConfigBean);
				}else{
					dbManager = new JDBCManager(dbConfigBean);
				}
			}catch(Throwable ex){
				logger.debug("DB INSTANCE LOAD ERROR DEFAULT_DB CONFIG: {}, {} ",ex.getMessage(),BeanUtil.toString(dbConfigBean),null);
				throw new Exception("#### Can't initiate DB Connection Manager"+ex.getMessage(),ex);
			}
			
		}
	}
	
	public void close(Connection conn){
		if(conn != null){
			try{
				conn.close();
			}catch(SQLException e){}
		}
	}
	
	public void close(ResultSet rSet){
		if(rSet != null){
			try{
				rSet.close();
			}catch(SQLException e){}
		}
	}
	
	public void close(Statement stmt){
		if(stmt != null){
			try{
				stmt.close();
			}catch(SQLException e){}
		}
	}
		
	public void close(PreparedStatement pstmt){
		if(pstmt != null){
			try{
				pstmt.close();
			}catch(SQLException e){}
		}
	}
	
	public void close(CallableStatement cstmt){
		if(cstmt != null){
			try{
				cstmt.close();
			}catch(SQLException e){}
		}
	}
	
	public void close(Connection conn,PreparedStatement pstmt,ResultSet rset){
		close(rset);
		close(pstmt);
		close(conn);
	}
	
	public void close(Connection conn,Statement stmt,ResultSet rset){
		close(rset);
		close(stmt);
		close(conn);
	}
	
	public void close(Connection conn,CallableStatement stmt,ResultSet rset){
		close(rset);
		close(stmt);
		close(conn);
	}
	
	public void setDebug(boolean debug){
		this.debug = debug;
	}
	
	public String getError(){
		return this.error;
	}
	
	public int preparedExecuteUpdate(String query) throws SQLException{
		PreparedStatement pstmt = null;
		Connection 	conn			= null;
		int result = 0;

		try {
			if(debug) logger.debug("query : [{}] ",query);
			conn		= getConnection();
			pstmt		= conn.prepareStatement(query);
			result  	= pstmt.executeUpdate();
			conn.commit();
		}catch(SQLException t){
			error = CommonUtil.getSQLExceptionMessage(t);
			logger.debug("sql error : {}",error);
			throw t;
		}finally {
			close(pstmt);
			close(conn);
		}
		return result;
	}
	
	
	public int preparedExecuteUpdate(String query,SharedMap<String,Object> record) throws SQLException{
		PreparedStatement pstmt = null;
		Connection 	conn			= null;
		int result = 0;
		DBUtil dbUtil = new DBUtil();
		try {
			if(debug){logger.debug("query : [{}] ",query);}
			conn		= getConnection();
			pstmt		= conn.prepareStatement(query);
			dbUtil.setValues(pstmt, record);
			result  	= pstmt.executeUpdate();
			conn.commit();
		}catch(SQLException t){
			error = CommonUtil.getSQLExceptionMessage(t);
			logger.debug("sql error : {}",error);
			throw t;
		}finally {
			close(pstmt);
			close(conn);
		}
		return result;
	}
	
	public long preparedExecuteUpdateAndLastIdx(String query,SharedMap<String,Object> record) throws SQLException{
		PreparedStatement pstmt = null;
		Connection 	conn			= null;
		ResultSet rset			= null;
		long result = 0;
		DBUtil dbUtil = new DBUtil();
		try {
			if(debug){logger.debug("query : [{}] ",query);}
			conn		= getConnection();
			pstmt		= conn.prepareStatement(query);
			dbUtil.setValues(pstmt, record);
			result  	= pstmt.executeUpdate();
			rset		= pstmt.executeQuery("SELECT LAST_INSERT_ID() ");
			
			while(rset.next()){
				result = rset.getLong(1);
			}
			conn.commit();
		}catch(SQLException t){
			error = CommonUtil.getSQLExceptionMessage(t);
			logger.debug("sql error : {}",error);
			throw t;
		}finally {
			close(rset);
			close(pstmt);
			close(conn);
		}
		return result;
	}
	

	public int statementExecuteUpdate(String query)throws Exception{
		Statement stmt 	= null;
		Connection conn = null;
		int result = 0;

		try {
			if(debug){logger.debug("query : [{}] ",query);}
			conn		= getConnection();
			stmt		= conn.createStatement();
			result  	= stmt.executeUpdate(query);
			conn.commit();
		}catch(SQLException t){
			error = CommonUtil.getSQLExceptionMessage(t);
			logger.debug("sql error : {}",error);
			throw t;
		}finally {
			close(stmt);
			close(conn);
		}
		return result;
	}
	
	
	
	
	
	public void setAutoCommit(Connection conn,boolean autoCommit) throws SQLException{
		conn.setAutoCommit(autoCommit);
	}

	
	public RecordSet statementExecute(String query)throws Exception{
		return statementExecute(query,defaultTimeout);
	}
	
	
	public RecordSet statementExecute(String query,int timeout) throws Exception {
		Statement stmt 	= null;
		ResultSet rset	= null;
		Connection conn = null;
		RecordSet records = null;
		try {
			if(debug) logger.debug("query : [{}] ",query);
			
			conn = DBFactory.getInstance().getConnection();
			stmt = conn.createStatement();
			stmt.setQueryTimeout(timeout);
			stmt.executeQuery(query);
			rset = stmt.getResultSet();
			
			if(rset != null){
				records = new RecordSet(rset);
			}
			
		}catch(SQLException e) {
			error = CommonUtil.getSQLExceptionMessage(e);
			logger.debug("sql error : {}",error);
			throw e;
		}finally{
			close(conn, stmt, rset);
		}
		
	//	if(records == null) records = new RecordSet(null);
		if(records == null) records = null;
		return records;
	}
	
	public RecordSet preparedStatementExecute(String query)throws Exception{
		return preparedStatementExecute(query,defaultTimeout);
	}
	
	
	public RecordSet preparedStatementExecute(String query,int timeout) throws Exception {
		PreparedStatement pstmt 	= null;
		ResultSet rset	= null;
		Connection conn = null;
		RecordSet records = null;
		try {
			if(debug) logger.debug("query : [{}] ",query);
			
			conn = DBFactory.getInstance().getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setQueryTimeout(timeout);
			pstmt.executeQuery();
			rset = pstmt.getResultSet();
			
			if(rset != null){
				records = new RecordSet(rset);
			}
			
		}catch(SQLException e) {
			error = CommonUtil.getSQLExceptionMessage(e);
			logger.debug("sql error : {}",error);
			throw e;
		}finally{
			close(conn, pstmt, rset);
		}
		
		if(records == null) records = null;  // records = new RecordSet(null);
		return records;
	}
	
	

	
	
	public abstract Connection getConnection() throws SQLException;
	public abstract void shutdown() throws SQLException;
	public abstract String status() throws SQLException;
	
}
