
package com.pgmate.lib.util.db;


import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.lib.util.lang.BeanUtil;
import com.pgmate.lib.util.lang.CommonUtil;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;


public class BoneCPManager extends DBManager {

	private Logger logger = LoggerFactory.getLogger( getClass());
	private BoneCP pool						= null;
	


	
	public BoneCPManager(DBConfigBean dbConfigBean)throws Exception{
		
		try{
			
			BoneCPConfig config = new BoneCPConfig();
			
			Class.forName(dbConfigBean.getDriver()).newInstance();
			config.setJdbcUrl(dbConfigBean.getJdbcUrl());
			config.setUsername(dbConfigBean.getUserName());
			config.setPassword(dbConfigBean.getPassWord());
			config.setMinConnectionsPerPartition(dbConfigBean.getMinConnection());
			config.setMaxConnectionsPerPartition(dbConfigBean.getMaxConnection());
			config.setPartitionCount(dbConfigBean.getPartitionCount());
			//config.setLazyInit(true);
			//config.setIdleConnectionTestPeriodInMinutes(60);
			//config.setIdleMaxAgeInMinutes(60);
			//config.setAcquireIncrement(5);
			//config.setStatementsCacheSize(0);
			pool = new BoneCP(config);
		}catch(Exception e){
			logger.debug("BoneCp error = DBConfigBean : {}, error : {} ",BeanUtil.toString(dbConfigBean),CommonUtil.getExceptionMessage(e));
		}
	}
	
	
	
	
	public Connection getConnection() throws SQLException{
		Connection conn = null;
		
 		try {
 			conn = pool.getConnection();
			conn.setAutoCommit(false);
		}catch(SQLException sql){
			throw  sql;
		}
 		return conn;
 		
	} 
	
	
	public String status()throws SQLException{
		return BeanUtil.toString(pool.getStatistics());
	}
	
	
	public void shutdown()throws SQLException {
		pool.shutdown();
	}
	
	
	
	
}
