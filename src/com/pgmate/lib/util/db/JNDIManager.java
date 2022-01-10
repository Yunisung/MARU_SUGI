package com.pgmate.lib.util.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Administrator
 *
 */
public class JNDIManager extends DBManager {

	private Logger logger = LoggerFactory.getLogger( getClass());
	
	private DataSource pool = null;
		
	
	public JNDIManager(DBConfigBean dbConfigBean) throws Exception {
		
		try {
			Context env = (Context) new InitialContext().lookup("java:comp/env");
			pool = (DataSource) env.lookup(dbConfigBean.getPoolName());
			if (pool == null)
				throw new Exception("Can't initiate WebServer DB Connection Manager (pool is null) & datasource ="+dbConfigBean.getPoolName()); 
		} catch (NamingException e) {
			logger.debug("JNDI DataSource error = JNDI : {}, error : {} ",dbConfigBean.getPoolName(),e.getMessage());
			throw new Exception(e);
		}
	}
	
	
	public Connection getConnection() throws SQLException {
		Connection conn = null;
		try {
			conn = pool.getConnection();
			conn.setAutoCommit(false);
			return conn;
		}catch(SQLException sql){
			throw new SQLException("JNDI DB Connection is not create " , sql);
		}
	}
	
	public String status()throws SQLException{
		return "";
	}
	
	public void shutdown()throws SQLException {
		pool = null;
	}
	

		
	
}
