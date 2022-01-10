package com.pgmate.lib.conf;

import com.pgmate.lib.tomcat.conf.TomcatConfigBean;
import com.pgmate.lib.util.daemon.DaemonConfigBean;
import com.pgmate.lib.util.db.DBConfigBean;
import com.pgmate.lib.vertx.conf.VertXConfigBean;

/**
 * @author Administrator
 *
 */
public class Config {

	public DBConfigBean db					= new DBConfigBean();
	public DaemonConfigBean daemon			= new DaemonConfigBean();
	public VertXConfigBean vertx			= new VertXConfigBean();
	public TomcatConfigBean tomcat			= new TomcatConfigBean();
	
	
	public Config() {
		// TODO Auto-generated constructor stub
	}
	
	



}
