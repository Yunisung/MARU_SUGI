package com.pgmate.lib.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;

/**
 * @author Administrator
 *
 */
public class RecordSet extends DataSet {

	public boolean labelLower = false;
	
	public RecordSet(){
	}
	
	public RecordSet(ResultSet rs) throws SQLException {
		if(rs == null) return;

		ResultSetMetaData meta = rs.getMetaData();
		int	max = meta.getColumnCount();
		
		this.columns = new String[max];
		for(int i = 0; i < max; i++) {
			if(labelLower == true) {
				columns[i] = meta.getColumnLabel(i+1).toLowerCase();
			} else {
				columns[i] = meta.getColumnLabel(i+1);
			}
		}
		
		int j = 0;
		while(rs.next()) {
			this.addRow();
			for(int i = 1; i <= max; i++) {
				try {
					if(meta.getColumnType(i) == java.sql.Types.CLOB) {
						this.put(columns[i-1], rs.getString(i));
					}else if(meta.getColumnType(i) == java.sql.Types.TIMESTAMP) {
						this.put(columns[i-1], rs.getTimestamp(i));
					}else {
						this.put(columns[i-1], rs.getObject(i));
					}
				} catch(Exception e) {
					this.put(columns[i-1], "");
				}
			}
			this.put("_idx", ++j);
		}

		this.first();
	}

	public RecordSet(Map<String , String>  rs) throws SQLException {
		if(rs == null) return; 
		int j = 0;
		this.addRow();		
		for (String rediskey : rs.keySet()){		
	      this.put(rediskey, rs.get(rediskey));
	    }		
		this.prev();
//		this.first();
	}

}
