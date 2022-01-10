package com.pgmate.sugi.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.gson.GsonUtil;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.sugi.model.ajax.Data;
import com.pgmate.sugi.util.CPUtil;

/**
 * @author Administrator
 *
 */
public class CPDAO extends DAO{

	private static Logger logger = LoggerFactory.getLogger( com.pgmate.sugi.dao.CPDAO.class );
	
	public CPDAO() {
		this(CPUtil.CP_DEBUG);
	}
	public CPDAO(boolean debug) {
		this.setDebug(debug);
	}
	
	public boolean insert(String table,List<Data> datas){
		this.setTable(table);
		CPUtil.setDAO(this, datas);
		return super.insert();
	}
	
	
	
	public boolean update(String table,List<Data> datas){
		this.setTable(table);
		CPUtil.setDAO(this, datas);
		return super.update();
	}
	
	
	public boolean insert(String table, String regId, List<Data> datas){
		this.setTable(table);
		this.setRecord("regId", regId);
		this.setRecord("regDay", CommonUtil.getCurrentDate("yyyyMMdd"));
		CPUtil.setDAO(this, datas);
		return super.insert();
	}	
	
	public boolean update(String table, String regId, List<Data> datas){		
		
		this.setTable(table);
		this.setRecord("regId", regId);
		this.setRecord("regDay", CommonUtil.getCurrentDate("yyyyMMdd"));
		CPUtil.setDAO(this, datas);
		return super.update();
	}
	
	public boolean updateAndBack(String table, String regId, List<Data> datas){
		//summary 추출 및 제거 
		String summary 	= "";
		for(Data data : datas){
			if(data.name.equals("summary")){
				summary = CommonUtil.toString(data.val);
				datas.remove(data);
				break;
			}
		}
		
		//TABLE의 컬럼 정보 추출 
		String columns =this.getColumns(table);
		String q = "INSERT INTO "+table.replace("PG_", "HT_")+" ("+columns+",summary) SELECT "+columns+",? FROM "+table +" WHERE ";
		
		
		this.setTable(table);
		this.setRecord("regId", regId);
		this.setRecord("regDay", CommonUtil.getCurrentDate("yyyyMMdd"));
		this.setRecord("regDate", CommonUtil.getCurrentTimestamp());
		CPUtil.setDAO(this, datas);
		
		q+=super.where.toString();
		//기존 정보 BACKUP TABLE 로 이동
		long lastIdx = -1;
		if(super.where.length() > 3){
			lastIdx = super.updateAndLastIdx(q,summary);
		}
		
		//업데이트 실행 
		boolean updated = super.update();
		
		//업데이터 실패하면 해당 레코드 삭제 
		if(!updated && lastIdx  !=-1){
			q = "DELETE FROM "+table.replace("PG_", "HT_") +" WHERE idx ="+lastIdx;
			super.update(q);
		}
		
		return updated;
	}
	
	public boolean delete(String table,List<Data> datas){
		this.setTable(table);
		CPUtil.setDAO(this, datas);
		return super.delete();
	}
	
	
	public RecordSet getHistory(String table,String column,Object key){
		addWhere(column,key,eq);
		setOrderBy("idx desc");
		return super.search();
	}
	
	


}
