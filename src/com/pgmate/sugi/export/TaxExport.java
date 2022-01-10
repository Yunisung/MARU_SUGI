package com.pgmate.sugi.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.lib.dao.DAO;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;
import com.pgmate.sugi.util.CPUtil;
import com.pgmate.sugi.util.SessionUtil;

public class TaxExport {

	private static Logger logger 	= LoggerFactory.getLogger( com.pgmate.sugi.export.TaxExport.class );
	private POIFSFileSystem fs = null;
	private String url = "";
	private String filePath = "tax";
	private String fileName = "";
	private final String template = "tax_template.xls";
	
	public TaxExport() {
		url = CPUtil.CP_UPLOAD_DIR+"/"+filePath+"/";
		filePath = CPUtil.getCanonicalWebPath()+File.separator+CPUtil.CP_UPLOAD_DIR+File.separator+filePath+File.separator;
		CPUtil.setTemplateDirectory(filePath);
	}
	
	public String makeTaxExcel(SharedMap<String,Object> senderMap, List<SharedMap<String,Object>> targetList, String userId) {
		try {
			fs = new POIFSFileSystem(new FileInputStream(CPUtil.getCanonicalWebPath() + File.separator + CPUtil.CP_TEMPLATE_DIR + File.separator + template));
			logger.debug("Template Path: {}", CPUtil.getCanonicalWebPath() + File.separator + CPUtil.CP_TEMPLATE_DIR + File.separator + template);
			HSSFWorkbook wb = new HSSFWorkbook(fs, true);
			HSSFSheet sheet = wb.getSheetAt(0);
			int rowCount = 6;
			if(senderMap.getString("distType").equalsIgnoreCase("true")) {
				fileName = CommonUtil.getCurrentDate("yyyyMMddhhmmss") +"_tax(선정산).xls";
			} else {
				fileName = CommonUtil.getCurrentDate("yyyyMMddhhmmss") +"_tax.xls";
			}
			
			for(SharedMap<String,Object> eachMap : targetList) {
				String writeDate = TaxExport.maxDateOfMonth(eachMap.getString("endDay"));
				
				Row row = sheet.getRow(rowCount++);
				row.getCell(0).setCellValue("01");															// 계산서 종류 (일반:01)
				row.getCell(1).setCellValue(writeDate); 													// 작성일자
				row.getCell(2).setCellValue(senderMap.getString("identity"));								// 공급자 등록번호
				row.getCell(4).setCellValue(senderMap.getString("compName"));								// 공급자 상호
				row.getCell(5).setCellValue(senderMap.getString("ceoName"));								// 공급자 성명
				row.getCell(6).setCellValue(senderMap.getString("addr1") + "," + senderMap.getString("addr2"));	// 공급자 주소
				row.getCell(7).setCellValue(senderMap.getString("bizCategory"));							// 업태
				row.getCell(8).setCellValue(senderMap.getString("bizType"));								// 종목(업종)
				row.getCell(9).setCellValue(senderMap.getString("email"));									// 이메일
				
				row.getCell(10).setCellValue(eachMap.getString("identity"));								// 공급받는자 등록번호
				row.getCell(12).setCellValue(eachMap.getString("compName"));								// 공급받는자 상호
				row.getCell(13).setCellValue(eachMap.getString("ceoName"));									// 공급받는자 성명
				row.getCell(14).setCellValue(eachMap.getString("addr1") + "," + eachMap.getString("addr2"));	// 공급받는자 주소
				row.getCell(15).setCellValue(eachMap.getString("bizCategory"));								// 공급받는자 업태
				row.getCell(16).setCellValue(eachMap.getString("bizType"));									// 공급받는자 종목(업종)
				row.getCell(17).setCellValue(eachMap.getString("email"));									// 공급받는자 이메일
				row.getCell(19).setCellValue(eachMap.getString("stlFee"));									// 공급가액
				row.getCell(20).setCellValue(eachMap.getString("stlFeeVat"));								// 새액
				row.getCell(22).setCellValue(writeDate.substring(6,8));										// 일자
				row.getCell(23).setCellValue("전자상거래수수료[카드결제]");									// 품목1
				row.getCell(27).setCellValue(eachMap.getString("stlFee"));									// 공급가액1
				row.getCell(28).setCellValue(eachMap.getString("stlFeeVat"));								// 새액1
				row.getCell(58).setCellValue("02");															// 영수(01), 청구(02)
				
				if(!insertHistory(fileName, row, eachMap.getString("mchtId"), eachMap.getString("taxId"), userId)) {
					logger.debug("FAIL INSERT HT_TAX = {}", eachMap.getString("compName"));
				}
			}
			
			FileOutputStream fileOut = new FileOutputStream(filePath + File.separator + fileName);
			logger.debug("DEST FILE PATH : {}", filePath + File.separator + fileName);
			wb.write(fileOut);
			fileOut.close();
			wb.close();
		} catch (FileNotFoundException e) {
			logger.debug("FileNotFoundException: {}", e.getMessage());
		} catch (IOException e) {
			logger.debug("IOException: {}", e.getMessage());
		}
		return url + fileName;
	}
	
	public static String maxDateOfMonth(String orgDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(CommonUtil.getDate("yyyyMMdd", orgDate));
		String endDay = String.valueOf(cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		return orgDate.substring(0, 6) + endDay;
	}
	
	
	private boolean insertHistory(String fileName, Row row, String mchtId, String taxId, String userId) {
		DAO dao = new DAO();
		dao.setTable("HT_TAX");
		
		dao.setRecord("fileName", fileName);
		dao.setRecord("mchtId", mchtId);
		dao.setRecord("taxId", taxId);
		dao.setRecord("taxType", row.getCell(0).getStringCellValue());									// 계산서 종류 (일반:01)
		dao.setRecord("writeDate", row.getCell(1).getStringCellValue());	 								// 작성일자
		dao.setRecord("senderIdentity", row.getCell(2).getStringCellValue());									// 공급자 등록번호
		dao.setRecord("senderCompName", row.getCell(4).getStringCellValue());									// 공급자 상호
		dao.setRecord("senderCeoName", row.getCell(5).getStringCellValue());									// 공급자 성명
		dao.setRecord("senderAddr1", row.getCell(6).getStringCellValue());									// 공급자 주소
		dao.setRecord("senderBizCategory", row.getCell(7).getStringCellValue());									// 업태
		dao.setRecord("senderBizType", row.getCell(8).getStringCellValue());									// 종목(업종)
		dao.setRecord("senderEmail", row.getCell(9).getStringCellValue());									// 이메일
		dao.setRecord("identity", row.getCell(10).getStringCellValue());									// 공급받는자 등록번호
		dao.setRecord("compName", row.getCell(12).getStringCellValue());									// 공급받는자 상호
		dao.setRecord("ceoName", row.getCell(13).getStringCellValue());									// 공급받는자 성명
		dao.setRecord("addr1", row.getCell(14).getStringCellValue());									// 공급받는자 주소
		dao.setRecord("bizCategory", row.getCell(15).getStringCellValue());									// 공급받는자 업태
		dao.setRecord("bizType", row.getCell(16).getStringCellValue());									// 공급받는자 종목(업종)
		dao.setRecord("email", row.getCell(17).getStringCellValue());									// 공급받는자 이메일
		dao.setRecord("subAmt", row.getCell(19).getStringCellValue());									// 공급가액
		dao.setRecord("subVat", row.getCell(20).getStringCellValue());									// 새액
		dao.setRecord("sendDate", row.getCell(22).getStringCellValue());				 					// 일자
		dao.setRecord("item1", row.getCell(23).getStringCellValue());									// 품목1
		dao.setRecord("subAmt1", row.getCell(27).getStringCellValue());									// 공급가액1
		dao.setRecord("subVat1", row.getCell(28).getStringCellValue());									// 새액1
		dao.setRecord("taxType1", row.getCell(58).getStringCellValue());               // 영수(01), 청구(02)
		dao.setRecord("regId", userId);
		dao.setRecord("regDay", CommonUtil.getCurrentDate("yyyyMMdd"));
		return dao.insert();
	}
	
}
