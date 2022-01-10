package com.pgmate.lib.util.db;

/**
 * @author Administrator
 *
 */
public class DBFactory {

	private static String ENTITY_NAME = "DEFAULT_DB";
	
	public static DBManager getInstance()throws Exception{
		return getInstance(ENTITY_NAME);		
	}
	
	public static DBManager getInstance(String name)throws Exception{
		return DBManager.getManager(name);
	}
}
