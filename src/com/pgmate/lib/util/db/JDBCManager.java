
package com.pgmate.lib.util.db;


import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.lib.util.lang.BeanUtil;
import com.pgmate.lib.util.lang.CommonUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


public class JDBCManager extends DBManager {

	private static Logger logger = LoggerFactory.getLogger( com.pgmate.lib.util.db.JDBCManager.class);
	private static HikariDataSource 	ds	= null;
	private Connection conn				= null;
	private static String poolAlias     = "TrustNetPool";
	private static int reapConnInterval = 150;
	private static int maxConn 			= 10;
	private static int idleTimeout 		= 50;
	private static int checkoutTimeout 	= 50;
	private static int maxCheckout 		= 15;


	
	public JDBCManager(DBConfigBean dbConfigBean)throws Exception{
		
		try{
			poolAlias 	= dbConfigBean.getPoolName();
			maxConn    	= dbConfigBean.getMaxConnection();
			try{
					
				HikariConfig config = new HikariConfig();
				config.setJdbcUrl(dbConfigBean.getJdbcUrl());
				config.setUsername(dbConfigBean.getUserName());
				config.setPassword(dbConfigBean.getPassWord());
				config.setDriverClassName(dbConfigBean.getDriver());
				config.setAutoCommit(true);
				config.setPoolName(poolAlias);
				config.setMinimumIdle(dbConfigBean.getMinConnection());
				config.setMaximumPoolSize(maxConn);
				config.setIdleTimeout(30000);
				config.setLeakDetectionThreshold(60*1000);
				config.addDataSourceProperty("cachePrepStmts", "true");
				config.addDataSourceProperty("prepStmtCacheSize", "250");
				config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
				//config.addHealthCheckProperty("connectivityCheckTimeoutMs", "1000");
				
				
				ds = new HikariDataSource(config);
				
			}catch(Exception e){
				logger.debug("JDBCPOOL CONNECTION ERROR = {} "+CommonUtil.getExceptionMessage(e));
				throw new Exception("JDBCPOOL CONNECTION ERROR = "+e.getMessage());
			}

		
		}catch(Exception ex){
			logger.debug("JDBCPOOL error = DBConfigBean : {}, error : {} ",BeanUtil.toString(dbConfigBean),CommonUtil.getExceptionMessage(ex));
			throw new Exception("JDBCPOOL error = DBConfigBean  , JDBCPOOL CONNECTION ERROR = "+ex.getMessage());
		}
	}
	
	
	
	
	public Connection getConnection() throws SQLException{
		try {
			conn = ds.getConnection();
		}catch(SQLException sql){
			logger.debug("JDBCPOOL CONNECTION ERROR ="+sql.getMessage());
			logger.debug("PoolInformation ="+status());
			logger.debug("JDBCPool DB Pool Connection is not create ");
			throw sql;
		}
		return conn;
	} 
	
	

	
	public String status() throws SQLException 
	{
		return "not supported";		
	}
	
	
	public void shutdown()throws SQLException {
		ds.close();
	}
	
	
	
	
}
