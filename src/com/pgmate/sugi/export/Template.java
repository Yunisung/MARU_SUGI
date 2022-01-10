package com.pgmate.sugi.export;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.io.FileIO;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;
import com.pgmate.sugi.util.CPUtil;

/**
 * @author Administrator
 *
 */
public class Template {

	private static Logger logger 	= LoggerFactory.getLogger( com.pgmate.sugi.export.Template.class );
	private static SharedMap<String,String> styleMap = new SharedMap<String,String>();
	private String stylesheet		= "";
	private CPDocument doc			= new CPDocument("export","","system");
	
	public Template() {
		this("tpl.css");
	}
	
	public Template(String stylesheet){
		this.stylesheet = stylesheet;
		if(!styleMap.containsKey(stylesheet)){
			try{
				byte[] read = new FileIO().getBytes(CPUtil.getCanonicalTemplatePath()+File.separator+stylesheet);
				styleMap.put(stylesheet, new String(read, "utf-8"));
			}catch(Exception e){
				System.out.println(e.getMessage());
			}	
		}
	}
	
	public void setDocument(CPDocument doc){
		this.doc = doc;
	}
	
	public String export(LinkedHashMap <String,String> thead,RecordSet rset,String contentType)throws Exception{
		int column = 0;
		StringBuilder sb =new StringBuilder();
		if(thead != null){
			sb.append(getFirst(CommonUtil.toString(thead.size()/4)));
		}else{
			sb.append(getFirst("1"));
		}
		
		sb.append("<table border='1'>\n");
		sb.append("<thead>\n");
		sb.append("<tr align='center'>\n");
		
		if(thead != null && thead.size() !=0){
			List<String> keys = new ArrayList<String>();
			for (Entry<String, String> entry : thead.entrySet()) {
				keys.add(entry.getKey());
				sb.append("<td>"+entry.getValue()+"</td>\n");
				column++;
	        }
			
			sb.append("</tr>\n");
			sb.append("</thead>\n");
			sb.append("<tbody>\n");
			
			
			for(SharedMap<String,Object> datas : rset.getRows()){
				sb.append("<tr>\n");
				for(String key : keys){
					Object data = datas.get(key);
					if(data instanceof java.lang.Integer){
						sb.append("<td align='right' class='long'>"+CommonUtil.makeMoneyType(CommonUtil.parseInt(data), ",")+"</td>\n");
					}else if(data instanceof java.lang.Long){
						sb.append("<td align='right' class='long'>"+CommonUtil.makeMoneyType(CommonUtil.parseLong(data), ",")+"</td>\n");
					}else if(data instanceof java.lang.Double){
						sb.append("<td align='right' class='double'>"+CommonUtil.makeMoneyType(CommonUtil.parseDouble(data), ",")+"</td>\n");
					}else if(data instanceof java.sql.Timestamp){
						sb.append("<td>"+CommonUtil.timestampToString((Timestamp)data, "yyyy/MM/dd HH:mm:ss")+"</td>\n");
					}else{
						sb.append("<td>"+CommonUtil.toString(data)+"</td>\n");
					}
				}
				sb.append("</tr>\n");
			}
	
			
		}else{
			int i=0;
			for(ConcurrentHashMap<String,Object> datas : rset.getRows()){
				if(i==0){
					for (Entry<String, Object> entry : datas.entrySet()) {
						sb.append("<td>"+entry.getKey()+"</td>\n");		
						column++;
					}
					sb.append("</tr>\n");
					sb.append("</thead>\n");
					sb.append("<tbody>\n");
				}
				sb.append("<tr>\n");
				for (Entry<String, Object> entry : datas.entrySet()) {
					
					Object data = entry.getValue();
					if(data instanceof java.lang.Integer){
						sb.append("<td align='right' class='long'>"+CommonUtil.makeMoneyType(CommonUtil.parseInt(data), ",")+"</td>\n");
					}else if(data instanceof java.lang.Long){
						sb.append("<td align='right' class='long'>"+CommonUtil.makeMoneyType(CommonUtil.parseLong(data), ",")+"</td>\n");
					}else if(data instanceof java.lang.Double){
						sb.append("<td align='right' class='double'>"+CommonUtil.makeMoneyType(CommonUtil.parseDouble(data), ",")+"</td>\n");
					}else if(data instanceof java.sql.Timestamp){
						sb.append("<td>"+CommonUtil.timestampToString((Timestamp)data, "yyyy/MM/dd HH:mm:ss")+"</td>\n");
					}else{
						sb.append("<td>"+CommonUtil.toString(data)+"</td>\n");
					}
					
				}
				sb.append("</tr>\n");
				i++;
				
			}
		}
		
		sb.append(getLast());
		
		TplExport export = new TplExport();
		String fileLink = "";
		export.setCss(stylesheet);
		export.setAuthor(doc.author);
		
		if(contentType.equals(CPUtil.CP_TYPE_FILE_PDF)){
			if(column > 6){
				export.setRotate(true);
			}
			fileLink = export.textToPdf(sb.toString(), true);
		}else if(contentType.equals(CPUtil.CP_TYPE_FILE_EXL)){
			fileLink = export.textToExl(sb.toString());
		}
		
		
		return fileLink;
	}
	

	
	private String getFirst(String colspan){
		StringBuilder sb = new StringBuilder();
		sb.append("<html>\n");

		sb.append("<head><meta http-equiv=Content-Type content=text/html; charset=UTF-8>\n");
		sb.append("<style>");
		sb.append((String)styleMap.get(stylesheet));
		sb.append("</style>");
		sb.append("</head>\n");
		sb.append("<body>\n");
		sb.append("<table class='tbl-type mb20' border='1' cellspacing='0' summary=''>\n");
		sb.append("<tbody>\n");
		sb.append("<tr>\n");
		sb.append("<td colspan='COLSPAN'>Title</td>\n");
		sb.append("<td colspan='COLSPAN'>"+doc.title+"</td>\n");
		sb.append("<td colspan='COLSPAN'>Date</td>\n");
		sb.append("<td colspan='COLSPAN'>"+CommonUtil.getCurrentDate("yyyy/MM/dd HH:mm:ss")+"</td>\n");
		sb.append("</tr>\n");
		sb.append("<tr>\n");
		sb.append("<td colspan='COLSPAN'>Description</td>\n");
		sb.append("<td  colspan='COLSPAN'>"+doc.desc+"</td>\n");
		sb.append("<td colspan='COLSPAN'>Author</td>\n");
		sb.append("<td  colspan='COLSPAN'>"+doc.author+"</td>\n");
		sb.append("</tr>\n");
		sb.append("</tbody>\n");
		sb.append("</table>\n");

		sb.append("<br/>\n");
		return sb.toString().replaceAll("COLSPAN", colspan);

	}
	
	private String getLast(){
		StringBuilder sb = new StringBuilder();
		sb.append("</tbody></table></body>\n");
		sb.append("</html>\n");
		return sb.toString();
	}
	
	
	

}
