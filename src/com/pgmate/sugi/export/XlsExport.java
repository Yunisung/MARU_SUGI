package com.pgmate.sugi.export;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;
import com.pgmate.sugi.util.CPUtil;

/**
 * @author Administrator
 *
 */
public class XlsExport {

	private static Logger logger = LoggerFactory.getLogger(com.pgmate.sugi.export.XlsExport.class);
	private CPDocument doc = null;
	private String filePath = "webexport";
	private String outputFileName = "";
	private String author = "system";
	private String url = "";
	private boolean rotate = false;
	ArrayList<String> numberArray = new ArrayList<String>() {{
		add("netAmount");
		add("netFee");
		add("netFeeVat");
		add("ptnFee");
		add("ptnFeeVat");
		add("grossAmount");
		add("vanFee");
		add("vanFeeVat");
		add("bankFee");
		add("benefit");
		add("benefitVat");
		add("chargeCardSum");
		add("chargeCardTaxSum");
		add("chargeCardCount");
		add("chargeCardNetFee");
		add("chargeCardNetFeeVat");
		add("chargeCardPtnFee");
		add("chargeCardVanFee");
		add("chargeCardBenefit");
		add("chargeCardNetSum");
		add("chargeBankSum");
		add("chargeBankTaxSum");
		add("chargeBankCount");
		add("chargeBankNetFee");
		add("chargeBankNetFeeVat");
		add("chargeBankPtnFee");
		add("chargeBankVanFee");
		add("chargeBankBenefit");
		add("chargeBankNetSum");
		add("withdrawSum");
		add("withdrawTaxSum");
		add("withdrawCount");
		add("withdrawNetFee");
		add("withdrawNetFeeVat");
		add("withdrawPtnFee");
		add("withdrawVanFee");
		add("withdrawBenefit");
		add("withdrawNetSum");
		add("chargeAmt");
		add("chargePtnFee");
		add("chargeCnt");
		add("withdrawAmt");
		add("withdrawPtnFee");
		add("withdrawCnt");
		add("stlAmt");
		add("balance");
		add("deposit");
		add("withdraw");
		add("tax");
		
	}};
	
	ArrayList<String> doubleArray = new ArrayList<String>() {{
		add("stlRate");
		add("stlDistRate");
		add("stlAgencyRate");
		add("stlSalesRate");
		add("stlVanRate");
	}};
	
	public XlsExport(CPDocument doc) {
		url = CPUtil.CP_UPLOAD_DIR + "/" + filePath + "/" + CommonUtil.getCurrentDate("yyyyMMdd") + "/";
		filePath = CPUtil.getCanonicalWebPath() + File.separator + CPUtil.CP_UPLOAD_DIR + File.separator + filePath + File.separator;
		CPUtil.setTemplateDirectory(filePath);
		filePath = filePath + File.separator + CommonUtil.getCurrentDate("yyyyMMdd") + File.separator;

		if (doc != null) {
			this.doc = doc;
		} else {
			doc = new CPDocument("AutoExport", "Export DATA", "SYSTEM");
		}
	}

	public String makeExcel(LinkedHashMap<String, String> thead, RecordSet rset, boolean showHeader, boolean headerStyle) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFCellStyle titleCellStyle = workbook.createCellStyle();
		HSSFCellStyle contentCellStyle = workbook.createCellStyle();
		HSSFSheet sheet = workbook.createSheet("Sheet1");
		HSSFRow row;
		HSSFCell cell;

		int rowCnt = 0;
		int columnCnt = 0;
		// 헤더 생성
		if (showHeader) {
/*			row = sheet.createRow(rowCnt++);
			row.setHeight((short)500);
			for (int i = 0; i < thead.size(); i++) {
				cell = row.createCell(columnCnt++);
				cell.setCellValue("");
}
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, thead.size()-1));*/

			row = sheet.createRow(rowCnt++);
			columnCnt = 0;

			if (headerStyle) {
				row.setHeight((short) 500);
				titleCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				titleCellStyle.setBottomBorderColor(HSSFColor.GREY_50_PERCENT.index);
				titleCellStyle.setBorderLeft(HSSFCellStyle.BORDER_NONE);
				titleCellStyle.setBorderRight(HSSFCellStyle.BORDER_NONE);
				titleCellStyle.setBorderTop(HSSFCellStyle.BORDER_NONE);
				titleCellStyle.setAlignment(HSSFCellStyle.VERTICAL_CENTER);
				Font headerFont = workbook.createFont();
				headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
				titleCellStyle.setFont(headerFont);
				titleCellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
				headerFont.setColor(IndexedColors.WHITE.getIndex());
				titleCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			}

			for (Entry<String, String> entry : thead.entrySet()) {
				cell = row.createCell(columnCnt++);
				if (headerStyle) {
					cell.setCellStyle(titleCellStyle);
				}
				cell.setCellValue(entry.getValue());
			}

			if (headerStyle) {
				contentCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				contentCellStyle.setBorderLeft(HSSFCellStyle.BORDER_NONE);
				contentCellStyle.setBorderRight(HSSFCellStyle.BORDER_NONE);
				contentCellStyle.setBorderTop(HSSFCellStyle.BORDER_NONE);
				contentCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
				contentCellStyle.setAlignment(HSSFCellStyle.VERTICAL_CENTER);
			}
		}

		// 바디 생성
		for (SharedMap<String, Object> datas : rset.getRows()) {
			row = sheet.createRow(rowCnt++);
			row.setHeight((short) 400);
			columnCnt = 0;

			for (String key : thead.keySet()) {
				cell = row.createCell(columnCnt++);
				cell.setCellStyle(contentCellStyle);
				Object data = datas.get(key);
				if(numberArray.indexOf(key) > -1) {
					cell.setCellValue(CommonUtil.parseLong(data));
				} else if (doubleArray.indexOf(key) > -1) {
					cell.setCellValue(CommonUtil.parseDouble(data));
				} else if (data instanceof java.lang.Integer) {
					cell.setCellValue(CommonUtil.parseInt(data));
				} else if (data instanceof java.lang.Long) {
					cell.setCellValue(CommonUtil.parseLong(data));
				} else if (data instanceof java.lang.Double) {
					cell.setCellValue(CommonUtil.parseDouble(data));
				} else if (data instanceof java.sql.Timestamp) {
					cell.setCellValue(CommonUtil.timestampToString((Timestamp) data, "yyyy/MM/dd HH:mm:ss"));
				} else {
					cell.setCellValue(CommonUtil.toString(data));
				}
			}
		}

		for (int i = 0; i < columnCnt; i++) {
			sheet.autoSizeColumn((short) i);
			sheet.setColumnWidth(i, (sheet.getColumnWidth(i)) + 512); // 윗줄만으로는 컬럼의 width 가 부족하여 더 늘려야 함.
		}

		TplExport export = new TplExport();
		String fileLink = "";
		try {
			fileLink = export.textToExl(doc.title, workbook);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return fileLink;
	}

	public String makeXlsFile(String text, HSSFWorkbook wb) throws Exception {
		outputFileName = text + ".xls";

		try {
			FileOutputStream fileOut = new FileOutputStream(filePath + outputFileName);
			wb.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return url + outputFileName;
	}

}
