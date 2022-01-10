package com.pgmate.lib.dao;

import java.security.Key;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.lib.conf.ConfigLoader;
import com.pgmate.lib.util.cipher.Crypt;
import com.pgmate.lib.util.db.DBConfigBean;
import com.pgmate.lib.util.db.DBFactory;
import com.pgmate.lib.util.db.DBManager;
import com.pgmate.lib.util.lang.ByteUtil;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;
import com.pgmate.sugi.util.CPUtil;

/**
 * @author Administrator
 *
 */
public class DAO  implements java.io.Serializable {

	private static Logger logger 			= LoggerFactory.getLogger(com.pgmate.lib.dao.DAO.class);
	private static DBConfigBean configBean 	= null;

	public static String eq= "eq";
	public static String ne= "ne";
	public static String gt= "gt";
	public static String ge= "ge";
	public static String lt= "lt";
	public static String le= "le";
	public static String lk= "lk";
	public static String in= "in";
	public static String ni= "ni";
	public static String bt= "bt";
	public static String fneq= "fneq";
	public static String fnne= "fnne";
	public static String fngt= "fngt";
	public static String fnge= "fnge";
	public static String fnlt= "fnlt";
	public static String fnle= "fnle";
			
	
	private String error	= "";
	private boolean debug	= false;
	private String columns	= "*";
	private String table	= "";
	
	private String join		= "";
	private String orderBy	= "regDate DESC";
	private String groupBy	= "";
	private long limit = 0;
	public StringBuilder where		= new StringBuilder();
	private StringBuilder sql		= new StringBuilder();
	private SharedMap<String,Object> record= new SharedMap<String,Object>();
	
	private long total		= 0;
	private double totalSum	= 0;
	private String hash		= "";
	private static Key secretKeySpec = null;

	public DAO() {
		this("",CPUtil.CP_DEBUG);		
	}
	
	/**
	 * TABLE 및 DEBUG = false 
	 * @param table
	 */
	public DAO(String table){
		this(table,false);
	}
	
	/**
	 * TABLE, DEBUG 여부 지정
	 * @param table
	 * @param debug
	 */
	
	
	public DAO(String table,boolean debug){
		if(configBean == null){
			configBean = ConfigLoader.getConfig().db;
		}
		if(secretKeySpec == null){
			try{
			secretKeySpec = Crypt.generateKey("DES", ByteUtil.toBytes("696d697373796f7568616e6765656e61", 16));
			}catch(Exception e){}
		}
		this.table = table;
		this.debug = debug;
	}

	public DAO(String table , boolean debug, DBConfigBean db){
		if(configBean == null){
			configBean = db;
		}
		if(secretKeySpec == null){
			try{
			secretKeySpec = Crypt.generateKey("DES", ByteUtil.toBytes("696d697373796f7568616e6765656e61", 16));
			}catch(Exception e){}
		}
		this.table = table;
		this.debug = debug;
	}
	
	
	/**
	 * JDBC Error Message 전달 
	 * @return
	 */
	public String getError() {
		return error;
	}
	
	/**
	 * SQL QUERY + Elapsed Time 확인
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	/**
	 * SELECT A,B,C 등 사용 시 A,B,C 로 지정하여 반환하고자 하는 COLUMN 을 comma 로 구분하여 전달한다.
	 * @param columns 
	 */
	public void setColumns(String columns) {
		this.columns = columns;
	}


	/**
	 * @param TABLE 이름
	 */
	public void setTable(String table) {
		this.table = table;
	}
	
	public String getTable() {
		return table;
	}
		
	/**
	 * @param orderby :  orderBy 구문은 생략 후   "ABC desc,CDB asc" 형식으로 전달.  
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}


	/**
	 * @param groupBy 구문은 생략 후 "ABC, DEF "형식으로 전달 
	 */
	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}


	/**
	 * type : INNER, OUTER ,condition : TabA.Colmun =TabB.Colmun 
	 * @param 
	 */
	public void setJoin(String type,String condition) {
		this.join = type+" JOIN "+table+" ON "+condition;
	}
	
	public void setLimit(long limit) {
		this.limit = limit;
	}


	/**
	 * ABC = 'CDE' AND BCD ='ABC'
	 * @param WEHRE 조건에 대한 값을 초기화 하여 재 지정한다.
	 */
	public void setWhere(String where) {
		this.where = new StringBuilder().append(" ").append(where);
	}
	
	/**
	 * ABC='CDE' 등이며 
	 * @param WEHRE 조건을 append 하여 저장한다. 기본적으로 기 조건과는 AND 로 지정된다.
	 */
	public void addWhere(String where) {
		if(this.where.length() ==0){
			setWhere(where);
		}else{
			this.where.append(" AND ").append(where);
		}
	}
	
	public void addWhere(String column, Object value) {
		//idx_key = table+"_"+value;		
		addWhere(column,value,eq);
	}
		
	
	/**
	 * COLUMN , VALUE, Operator 를 지정하여 조건 절에 APPEND 한다.
	 * Operator 는 eq = , gt > , ge >= , lt < , le <= , lk like 를 지원하며 그외의 연산자는 축약어가 아닌 직접 지정하면 된다. 
	 * @param column
	 * @param value
	 * @param operatorator
	 */
	
	public void addWhere(String column,Object value,String operator){
		if(!column.equals("") && value !=null){
			if(value instanceof java.lang.Long || value instanceof java.math.BigInteger || value instanceof java.lang.Integer || value instanceof java.lang.Double){
				if(!operator.equals(lk)){
					if(operator.equals(in) || operator.equals(ni)){
						addWhere(column +getOperator(operator)+"("+CommonUtil.toString(value)+")");
					}else{
						addWhere(column +getOperator(operator)+CommonUtil.toString(value));
					}
				}
			}else if(value instanceof java.sql.Timestamp){
				if(!operator.equals(lk)){
					addWhere("DATE_FORMAT("+column +",'%y%m%d%H%i%s')"+getOperator(operator)+"'"+CommonUtil.timestampToString((Timestamp)value,"yyMMddHHmmss")+"'");
				}
			}else{
				if(operator.equals(lk)){
					addWhere(column +getOperator(operator)+"'%"+CommonUtil.toString(value)+"%'");		
				}else if(operator.equals(in) || operator.equals(ni)){
					addWhere(column +getOperator(operator)+"("+CommonUtil.toString(value)+")");
				}else if(operator.equals(bt)){
					String[] scope = CommonUtil.adjustArray(CommonUtil.split(CommonUtil.toString(value), ",", true),2);
					addWhere(column +getOperator(operator)+scope[0]+" AND "+scope[1]);
				}else if(operator.startsWith("fn")){
					addWhere(column +getOperator(operator)+CommonUtil.toString(value));
				}else{
					addWhere(column +getOperator(operator)+"'"+CommonUtil.toString(value)+"'");
				}
			}
		}
	
	}
	
	/**
	 * prepared insert,update 사용 시 각 컬럼의 값을 직접 사용한다.
	 * @param column
	 * @param value
	 */
	public void setRecord(String column,Object value){
		record.put(column, value);
	}
	
	/**
	 * 페이징 시 재 검색조건 가져올때.
	 * @return
	 */
	public String getHash(){
		return this.hash;
	}
	
	/**
	 *지정된 TABLE, JOIN , ORDER BY , GROUP BY 으로 SELECT 쿼리를 할 경우 사용한다.
	 * @return
	 */
	public RecordSet search(){		 				
		return  query(searchInit());
	}

	private String searchInit(){
		
		sql.append("SELECT ").append(columns).append(" FROM ").append(this.table).append(this.join);
		if(where.length() > 1){ sql.append(" WHERE ").append(where.toString());}
		if(groupBy.length() > 1){ sql.append(" GROUP BY ").append(groupBy);}
		if(orderBy.length() > 1){ sql.append(" ORDER BY ").append(orderBy);}
		if(limit > 0 ) { sql.append(" LIMIT ").append(limit); }
		return sql.toString();
	}
	
	private String searchTable(){
		return "SELECT "+columns+" FROM "+this.table+this.join;
	}
	

	private String searchCondition(){
		StringBuilder buf = new StringBuilder();
		if(where.length() > 1){ buf.append(" WHERE ").append(where.toString());}
		if(groupBy.length() > 1){ buf.append(" GROUP BY ").append(groupBy);}
		if(orderBy.length() > 1){ buf.append(" ORDER BY ").append(orderBy);}
		return buf.toString();
	}
	
	/**
	 * ROW 수를 조회할 때 사용한다.
	 * @return
	 */
	public long getCount(){
		this.columns = " COUNT(*) AS CNT ";
		RecordSet rset = query(searchInit());
		if(rset == null || !rset.next()) {
			return 0;
		} else {
			return rset.getLong("CNT");
		}
	}

	/**
	 * 지정한 WHERE 절로 ROW 수를 조회할 때 사용한다.
	 * @param where
	 * @return
	 */
	public long getCount(String initWhere){
		this.where = new StringBuilder();
		this.where.append(initWhere);
		return getCount();
	}
	
	
	
	
	/**
	  * /**
	 * 페이징 관련된 값 + 페이지 HASH 를 사용해서 검색 할 경우 
	 * 지정된 TABLE, JOIN , ORDER BY , GROUP BY 으로 SELECT 쿼리를 할 경우 사용한다.
	 * @return
	 * @param current
	 * @param size
	 * @param hash
	 * @return
	 */
	public RecordSet searchList(long current,long size,String hashVal){
		if(current == 0){current = 1;}
		if(size == 0){size = 20;}
		
		String countQuery 	= "";
		String query 		= "";
		
		
		if(hashVal.trim().equals("")){
			countQuery = "SELECT COUNT(*) AS TOTAL FROM "+this.table+this.join;
			
			String condition = searchCondition();
			if(condition.length() > 1){ countQuery += condition; }
			
			if(!groupBy.equals("")){
				countQuery = "SELECT COUNT(*) AS TOTAL FROM ( "+countQuery +") BC";
			}
			// CUBRID : query = "SELECT * FROM (" +searchInit() + ") LIMIT "+(size*(current-1))+","+(size);
			query = searchInit() +" LIMIT "+(size*(current-1))+","+(size);
			//HASH 값에 대한 암호화 및 BASE64 처리
			cryptHash(condition);
		}else{
			this.hash = hashVal;
			//HASH 복호화 하여 사용한다.
			String hashWhere = decryptDES(hashVal);
			if(debug) {logger.debug("query hash : [{}] ",hashWhere);}
			countQuery = "SELECT COUNT(*) AS TOTAL FROM "+this.table+this.join;
			if(hashWhere.length() > 1){ countQuery += hashWhere; }
			if(!groupBy.equals("")){
				countQuery = "SELECT COUNT(*) AS TOTAL FROM ( "+countQuery +") BC";
			}
			// CUBRID query = "SELECT * FROM (" +searchTable()+" "+hashWhere + ") LIMIT "+(size*(current-1))+","+(size);
			query = searchTable() +" "+hashWhere +" LIMIT "+(size*(current-1))+","+(size);
		}
		
		long startsTime = System.currentTimeMillis();
		
		DBManager db	= null;
		Statement stmt 	= null;
		ResultSet rset	= null;
		Connection conn = null;
		RecordSet records = new RecordSet();
		
		try {
			
			db = DBFactory.getInstance();
			conn = db.getConnection();
			stmt = conn.createStatement();
			stmt.executeQuery(countQuery);
			rset = stmt.getResultSet();
			if(rset.next()){
				total 	= rset.getLong("TOTAL");
			}
			//ResultSet,Statement 초기화
			rset.close();
			stmt.close();
			
			stmt = conn.createStatement();
			stmt.executeQuery(query);
			rset = stmt.getResultSet();
			if(rset != null){
				records = new RecordSet(rset);
			}
		}catch(Exception e) {
			error = CommonUtil.getSQLExceptionMessage(e);
			logger.debug("sql error : {}",error);
		}finally{
			if(debug) {
				logger.debug("query count : [{}] ",countQuery);
				logger.debug("query  : [{}] ",query);
				logger.debug("elapsedTime : [{}]msec",(long)(System.currentTimeMillis()-startsTime));
			}
			db.close(conn, stmt, rset);
			init();
		}

		return records;
		
	}
	
	
	
	public RecordSet searchList2(long current,long size,String hashVal,String sumColumn){
		if(current == 0){current = 1;}
		if(size == 0){size = 20;}
		
		String countQuery 	= "";
		String query 		= "";
		
		
		if(hashVal.trim().equals("")){
			countQuery = "SELECT COUNT(*) AS TOTAL,SUM("+sumColumn+") AS TOTAL_AMT FROM "+this.table+this.join;
			String condition = searchCondition();
			if(condition.length() > 1){ countQuery += condition; }
			if(!groupBy.equals("")){
				countQuery = "SELECT COUNT(*) AS TOTAL, TOTAL_AMT FROM ( "+countQuery +") BC";
			}
			//CUBRID : query = "SELECT * FROM (" + searchInit() + ")  LIMIT "+(size*(current-1))+","+(size);
			query = searchInit() + " LIMIT "+(size*(current-1))+","+(size);
			//HASH 값에 대한 암호화 및 BASE64 처리
			cryptHash(condition);
		}else{
			this.hash = hashVal;
			//HASH 복호화 하여 사용한다.
			String hashWhere = decryptDES(hashVal);
			if(debug) {logger.debug("query hash : [{}] ",hashWhere);}
			countQuery = "SELECT COUNT(*) AS TOTAL,SUM("+sumColumn+") AS TOTAL_AMT FROM "+this.table+this.join;
			if(hashWhere.length() > 1){ countQuery += hashWhere; }
			if(!groupBy.equals("")){
				countQuery = "SELECT COUNT(*) AS TOTAL, TOTAL_AMT FROM ( "+countQuery +") BC";
			}
			//CUBRID query = "SELECT * FROM (" +searchTable()+" "+hashWhere + ") LIMIT "+(size*(current-1))+","+(size);
			query = searchTable()+" "+hashWhere + " LIMIT "+(size*(current-1))+","+(size);
		}
		
		long startsTime = System.currentTimeMillis();
		
		DBManager db	= null;
		Statement stmt 	= null;
		ResultSet rset	= null;
		Connection conn = null;
		RecordSet records = new RecordSet();
		
		try {
			
			db = DBFactory.getInstance();
			conn = db.getConnection();
			stmt = conn.createStatement();
			stmt.executeQuery(countQuery);
			rset = stmt.getResultSet();
			if(rset.next()){
				total 	= rset.getLong("TOTAL");
				totalSum = rset.getDouble("TOTAL_AMT");
			}
			//ResultSet,Statement 초기화
			rset.close();
			stmt.close();
			
			stmt = conn.createStatement();
			stmt.executeQuery(query);
			rset = stmt.getResultSet();
			if(rset != null){
				records = new RecordSet(rset);
			}
		}catch(Exception e) {
			error = CommonUtil.getSQLExceptionMessage(e);
			logger.debug("sql error : {}",error);
		}finally{
			if(debug) {
				logger.debug("query count : [{}] ",countQuery);
				logger.debug("query  : [{}] ",query);
				logger.debug("elapsedTime : [{}]msec",(long)(System.currentTimeMillis()-startsTime));
			}
			db.close(conn, stmt, rset);
			init();
		}

		return records;
		
	}
	
	
	

	public boolean insert(){
		
		sql.append("INSERT INTO " + this.table + " (");
		
		int max = record.size();
		int k=0;
		for (Iterator<String> iterator = record.keySet().iterator(); iterator.hasNext();) {
			String key =  (String) iterator.next();
			sql.append(key);
			if(k < (max - 1)) {sql.append(",");}
			k++;
		}
		sql.append(") VALUES (");
		
		for(int i=0; i<max; i++) {
			sql.append("?");
			if(i < (max - 1)) sql.append(",");
		}
		sql.append(")");

		int ret = 0;
		long startsTime = System.currentTimeMillis();
		try{			
			ret=DBFactory.getInstance().preparedExecuteUpdate(sql.toString(),record);
		}catch(Exception e){
			this.error = CommonUtil.getSQLExceptionMessage(e);
		}finally{
			if(debug){ 		
				logger.debug("elapsedTime : [{}]msec",(long)(System.currentTimeMillis()-startsTime));
			}
			init();
		}
	
		return ret > 0 ? true : false;		
	}
	
	
	public long insertAndLastIdx(){
		
		sql.append("INSERT INTO " + this.table + " (");
		
		int max = record.size();
		int k=0;
		for (Iterator<String> iterator = record.keySet().iterator(); iterator.hasNext();) {
			String key =  (String) iterator.next();
			sql.append(key);
			if(k < (max - 1)) {sql.append(",");}
			k++;
		}
		sql.append(") VALUES (");
		
		for(int i=0; i<max; i++) {
			sql.append("?");
			if(i < (max - 1)) sql.append(",");
		}
		sql.append(")");

		long ret = 0;
		long startsTime = System.currentTimeMillis();
		try{			
			ret=DBFactory.getInstance().preparedExecuteUpdateAndLastIdx(sql.toString(), record);
		}catch(Exception e){
			this.error = CommonUtil.getSQLExceptionMessage(e);
		}finally{
			if(debug){ 		
				logger.debug("elapsedTime : [{}]msec",(long)(System.currentTimeMillis()-startsTime));
			}
			init();
		}
	
		return ret ;		
	}
	
	public boolean insert(String query){
		int ret = 0;
		long startsTime = System.currentTimeMillis();
		try{
			ret=DBFactory.getInstance().preparedExecuteUpdate(query );
		}catch(Exception e){
			this.error = CommonUtil.getSQLExceptionMessage(e);
		}finally{
			if(debug){
				logger.debug("elapsedTime : [{}]msec",(long)(System.currentTimeMillis()-startsTime));
			}
			init();
		}
	
		return ret > 0 ? true : false;
	}
	
	public boolean update(){
		
		sql.append("UPDATE " + this.table + " SET ");
		
		int max = record.size();
		int k=0;
		for (Iterator<String> iterator = record.keySet().iterator(); iterator.hasNext();) {
			String key =  (String) iterator.next();
			sql.append(key + "=?");
			if(k < (max - 1)) {sql.append(",");}
			k++;
		}
		if(where.length() > 1){sql.append(" WHERE " + this.where);}

		int ret = 0;
		long startsTime = System.currentTimeMillis();
		try{
			ret=DBFactory.getInstance().preparedExecuteUpdate(sql.toString(),record );
		}catch(Exception e){
			this.error = CommonUtil.getSQLExceptionMessage(e);
		}finally{
			if(debug){
				logger.debug("elapsedTime : [{}]msec",(long)(System.currentTimeMillis()-startsTime));
			}
			init();
		}
	
		return ret > 0 ? true : false;
	}
	
	
	public boolean delete() {
		sql.append("DELETE FROM " + this.table + " WHERE " + this.where.toString());
		int ret = 0;
		long startsTime = System.currentTimeMillis();
		try{
			ret=DBFactory.getInstance().preparedExecuteUpdate(sql.toString(),record );
		}catch(Exception e){
			this.error = CommonUtil.getSQLExceptionMessage(e);
		}finally{
			if(debug) {
				logger.debug("elapsedTime : [{}]msec",(long)(System.currentTimeMillis()-startsTime));
			}
			init();
		}
	
		return ret > 0 ? true : false;
	}
	
	
	public boolean update(String query){
		
		if(query.toUpperCase().indexOf("UPDATE ") > -1 && query.toUpperCase().indexOf("WHERE ") < 0){
			logger.debug("UPDATE ERROR WHERE IS NULL [{}]" ,query.toString() );
			return false;
		}
		
		int ret = 0;
		long startsTime = System.currentTimeMillis();
		try{
			ret=DBFactory.getInstance().preparedExecuteUpdate(query );
		}catch(Exception e){
			this.error = CommonUtil.getSQLExceptionMessage(e);
		}finally{
			if(debug){
				logger.debug("elapsedTime : [{}]msec",(long)(System.currentTimeMillis()-startsTime));
			}
			init();
		}
	
		return ret > 0 ? true : false;
	}
	
	public long updateAndLastIdx(String query,String summary){
		if(query.toUpperCase().indexOf("WHERE ") < 0){
			logger.debug("UPDATE ERROR WHERE IS NULL [{}]" ,sql.toString() );
			return 0;
		}
		
		long lastIdx = 0;
		DBManager db 			= null;
		PreparedStatement pstmt 		= null;
		Connection 	conn		= null;
		ResultSet rset			= null;
		int result = 0;

		try {
			
			db 			= DBFactory.getInstance();
			conn		= db.getConnection();

			pstmt		= conn.prepareStatement(query);
			pstmt.setString(1,summary);
			result  	= pstmt.executeUpdate();
			rset		= pstmt.executeQuery("SELECT LAST_INSERT_ID() ");
			
			while(rset.next()){
				lastIdx = rset.getLong(1);
			}
			conn.commit();
		}catch(Exception t){
			error = CommonUtil.getSQLExceptionMessage(t);
			logger.debug("sql error : {}, query : {}",error,query);
		}finally {
			db.close(conn, pstmt, rset);
		}
		if(debug) {
			logger.debug("query : [{}] ",query);
			logger.debug("generated key : [{}] ",lastIdx);
		}
		return lastIdx;
		
	}
	
	
	/**
	  * 직접 쿼리로 SELECT 를 요청 할 때 사용한다.
	 * @param query
	 * @return
	 */
	public RecordSet query(String query){
		DBManager db = null;
		RecordSet rset= new RecordSet();
		
		long startsTime = System.currentTimeMillis();
		try {
			db = DBFactory.getInstance();
			db.setDebug(debug);
			rset = db.statementExecute(query);
		}catch(Exception e) {
			this.error = db.getError();
		}finally{
			if(debug) {
				logger.debug("elapsedTime : [{}]msec",(long)(System.currentTimeMillis()-startsTime));
			}
			init();
		}
		
		return rset;
	}
	
	
	
	public long getSeqCurrent(String name){
		String query = "SELECT curVal FROM PG_SEQ WHERE name ='"+name+"'";
		RecordSet rset = query(query);
		if(rset.size() ==0) {
			return 0;
		} else {
			rset.next();
			return rset.getLong("curVal");
		}
	}
	
	public long getSeqNext(String name){
		String query = "SELECT FN_NEXTVAL('"+name+"') as CNT";
		RecordSet rset = query(query);
		if(rset.size() == 0) {
			return 0;
		} else {
			rset.next();
			return rset.getLong("CNT");
		}
	}
	
	public String getPassword(String value){
		String query = "SELECT password('"+value+"') pw";
		RecordSet rset = query(query);
		if(rset.size() ==0) {
			return "";
		} else {
			rset.next();
			return rset.getString("pw");
		}
	}
	
	
	public String getAESEnc(String value){
		String query = "SELECT FN_AES_ENC('"+value+"') pw";
		RecordSet rset = query(query);
		if(rset.size() ==0) {
			return "";
		} else {
			rset.next();
			return rset.getString("pw");
		}
	}
	
	public String getAESDec(String value){
		String query = "SELECT FN_AES_DEC('"+value+"') pw";
		RecordSet rset = query(query);
		if(rset.size() ==0) {
			return "";
		} else {
			rset.next();
			return rset.getString("pw");
		}
	}
	
	public String getFunction(String function,String... value){
		String returnVal = "";
		String query = "SELECT "+function+"( ";
		for(String val : value){
			query +="?,";
		}
		query = query.substring(0,query.length()-1) +") as val";
		
		DBManager db 			= null;
		PreparedStatement pstmt = null;
		Connection 	conn		= null;
		ResultSet rset			= null;

		try {
			
			db 			= DBFactory.getInstance();
			conn		= db.getConnection();

			pstmt		= conn.prepareStatement(query);
			for(int i=0;i<value.length;i++){
				pstmt.setString(i+1,value[i]);
			}
			rset		= pstmt.executeQuery();
			
			while(rset.next()){
				returnVal = rset.getString(1);
			}
			conn.commit();
		}catch(Exception t){
			error = CommonUtil.getSQLExceptionMessage(t);
			logger.debug("sql error : {}, query : {}",error,query);
		}finally {
			db.close(conn, pstmt, rset);
		}
		if(debug) {
			logger.debug("query : [{}] ",query);
		}
		return returnVal;
	}
	
	
	/**
	  * PAGING 검색 후 총 조회 결과 회신
	 * @return total 
	 */
	public long getTotal() {
		return total;
	}
	
	/**
	 * PAGING 검색 후 총 조회 결과 회신
	 * @return total 
	 */
	public double getTotalSum() {
		return totalSum;
	}

	
	private static String getOperator(String cond){
		cond = cond.toLowerCase();
		if(cond.equals(eq) || cond.equals(fneq)){
			return " = ";
		}else if(cond.equals(ne) || cond.equals(fnne)){
			return " != ";
		}else if(cond.equals(gt) || cond.equals(fngt)){
			return " > ";
		}else if(cond.equals(ge) || cond.equals(fnge)){
			return " >= ";
		}else if(cond.equals(lt) || cond.equals(fnlt)){
			return " < ";
		}else if(cond.equals(le) || cond.equals(fnle)){
			return " <= ";
		}else if(cond.equals(lk)){
			return " LIKE ";
		}else if(cond.equals(in)){
			return " IN ";
		}else if(cond.equals(ni)){
			return " NOT IN ";
		}else if(cond.equals(bt)){
			return " BETWEEN ";
		}else{
			return cond;
		}
	}
	
	public String getColumns(String table){
		StringBuffer sb = new StringBuffer();
		DBManager db	= null;
		Statement stmt 	= null;
		ResultSet rset	= null;
		Connection conn = null;
		try {
			
			db = DBFactory.getInstance();
			conn = db.getConnection();
			stmt = conn.createStatement();
			stmt.executeQuery("SELECT * FROM "+table + " WHERE 0=1");
			
			rset = stmt.getResultSet();
			ResultSetMetaData metaData = rset.getMetaData();
			int rowCount = metaData.getColumnCount();
			for(int i=0;i<rowCount;i++){
		
				sb.append(metaData.getColumnName(i+1)+",");
			}
			
			
		}catch(Exception e) {
			error = CommonUtil.getSQLExceptionMessage(e);
			logger.debug("sql error : {}",error);
		}finally{
			db.close(conn, stmt, rset);
		}
		if(sb.length() == 0){
			return "";
		}else{
			return sb.toString().substring(0, sb.length()-1);
		}
	}

	
	private void cryptHash(String condition){
		try{
			this.hash =  Crypt.encryptBase64(secretKeySpec, "DES/ECB/PKCS5Padding",null, condition.getBytes());
		}catch(Exception e){}
	}
	
	private String decryptDES(String condition){
		String des = "";
		if(condition.trim().equals("")){
			return "";
		}
		try{
			des = Crypt.decryptBase64(secretKeySpec, "DES/ECB/PKCS5Padding",null, condition);
		}catch(Exception e){}
		return des;
	}
	
	public String toString(RecordSet rset){
		StringBuilder sb = new StringBuilder();
		
		sb.append("\nsize : "+rset.size());
		sb.append("\ncolumn : "+CommonUtil.arrayToString(rset.getColumns()));
		while(rset.next()){
			ConcurrentHashMap<String,Object> row = rset.getRow();
			sb.append("\n row : "+rset.getIdx());
			for (Entry<String, Object> entry : row.entrySet()) {
				sb.append("\n   "+entry.getKey()+":"+entry.getValue());
	        }
		}
		return sb.toString();
	}
	
	
	public boolean dbPing(){
		RecordSet rset = query("SELECT 1+1 AS CNT");
		rset.next();
		if(rset.getInt("CNT") == 2){
			return true;
		}else{
			return false;
		}
		
	}
	

	
	private void init(){
		error	= "";
		debug	= false;
		//columns	= "*";
		//table	= "";
		
		join		= "";
		orderBy	= "regDate DESC";
		groupBy	= "";
		where.setLength(0);
		sql.setLength(0);
		//record.clear();
		
		//total		= 0;
		limit = 0;
		hash		= "";
	}
	
	public void initRecord(){
		record.clear();
		where.setLength(0);
		sql.setLength(0);
		join		= "";
		orderBy	= "regDate DESC";
		groupBy	= "";
		limit = 0;
	}
	
	public static void main(String[] args) {
		DAO dao = new DAO();
		
		
	}
	
}

