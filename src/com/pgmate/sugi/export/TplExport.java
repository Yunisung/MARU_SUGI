package com.pgmate.sugi.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.pgmate.lib.util.io.FileIO;
import com.pgmate.lib.util.io.FileUtil;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.sugi.util.CPUtil;

/**
 * @author Administrator
 *
 */
public class TplExport {
	
	private static Logger logger 	= LoggerFactory.getLogger( com.pgmate.sugi.export.TplExport.class );
	
	private String filePath			= "webexport";
	private String css				= "tpl.css";
	private String waterMarkFile	= "tpl.png";
	private String hangul			= "malgun.ttf";
	private String outputFileName	= "";
	private String author			= "system";
	private String url				= "";
	private boolean rotate			= false;
	
	
	
	public TplExport(){
		url = CPUtil.CP_UPLOAD_DIR+"/"+filePath+"/"+CommonUtil.getCurrentDate("yyyyMMdd")+"/";
		filePath = CPUtil.getCanonicalWebPath()+File.separator+CPUtil.CP_UPLOAD_DIR+File.separator+filePath+File.separator;
		CPUtil.setTemplateDirectory(filePath);
		filePath = filePath+File.separator+CommonUtil.getCurrentDate("yyyyMMdd")+File.separator;
		
	}
	
	public void setCss(String css){
		this.css = css;
	}
	
	public void setRotate(boolean rotate){
		this.rotate = rotate;
	}
	
	
	public void setAuthor(String author){
		this.author = author;
	}
	
	
	public String textToPdf(String text,boolean waterMark) throws Exception {
		outputFileName = defaultFileName("pdf");
		
		try {	 
			
			
			//Document 생성
			
			Document document = null;
			if(rotate){
				document = new Document(PageSize.A4.rotate(), 30, 30, 30, 30); // 용지 및 여백 설정
			}else{
				document =new Document(PageSize.A4, 30, 30, 30, 30); // 용지 및 여백 설정
			}
			
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath+outputFileName)); // 바로 다운로드.
			//PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());  // Stream 에 쓸때. 
			writer.setInitialLeading(12.5f);
			// Document 오픈
			document.open();
			XMLWorkerHelper helper = XMLWorkerHelper.getInstance();
			      
			// CSS
			CSSResolver cssResolver = new StyleAttrCSSResolver();
			CssFile cssFile =XMLWorkerHelper.getCSS(new FileInputStream(CPUtil.getCanonicalTemplatePath()+File.separator+css)); 
					
			cssResolver.addCss(cssFile);
			      
			 // HTML, 폰트 설정
			 XMLWorkerFontProvider fontProvider = new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);
			 fontProvider.register(CPUtil.getCanonicalTemplatePath()+File.separator+hangul, "MalgunGothic"); // MalgunGothic은 alias,
			 
			 CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);
			  
			 HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
			 htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
			  
			 // Pipelines
			 PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
			 HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
			 CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);
			  
			 XMLWorker worker = new XMLWorker(css, true);
			 XMLParser xmlParser = new XMLParser(worker, Charset.forName("UTF-8"));
			  
			 // 폰트 설정에서 별칭으로 줬던 "MalgunGothic"을 html 안에 폰트로 지정한다.
			 StringReader strReader = new StringReader(text);
			 xmlParser.parse(strReader);
			 
			 
			 //PdfPageEvent event = new PdfPageEvent(); // Pdf 속성 설정.
			 writer.setBoxSize("boxName", new Rectangle(36, 54, 559, 788));
			 //writer.setPageEvent(event);
			 
			 document.close();
			 writer.close();
			 
			 //stamp찍기		    
			 if(waterMark) {
				 BaseFont bf = BaseFont.createFont(BaseFont.COURIER_BOLD, BaseFont.WINANSI, BaseFont.EMBEDDED);
				 BaseFont bf2 = BaseFont.createFont(BaseFont.COURIER, BaseFont.WINANSI, BaseFont.EMBEDDED);
				 //Image image = Image.getInstance(CPUtil.getCanonicalTemplatePath()+File.separator+waterMarkFile);
				 //image.setAbsolutePosition(170, 440);
				 
				 String oldFile = filePath+outputFileName;
				 PdfReader reader = new PdfReader(oldFile);
				 int n = reader.getNumberOfPages();
				 
				 String waterMarkPDF = defaultFileName("pdf");
				 PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(filePath+waterMarkPDF));
				 
				 
				 
				 int i=0;
				 PdfContentByte under =null;
				 PdfContentByte over  =null;;
				 
				 while(i<n){
					 
					if(rotate){
						under = stamp.getUnderContent(i+1);
				        under.beginText();
				        under.setFontAndSize(bf, 32);
				        under.setCMYKColorFill(64, 11, 0, 0);
				        under.showTextAligned(Element.ALIGN_CENTER, "", 397.5f, 281, 45);
				        
				        under.setFontAndSize(bf, 10);
				        under.setCMYKColorFill(64, 11, 0, 0);
				        under.showTextAligned(Element.ALIGN_CENTER, CommonUtil.getCurrentDate("yyyy/MM/dd HH:mm:ss"),420.5f, 270, 45);
				        under.endText(); 
						
				        over = stamp.getOverContent(i+1);
					    over.beginText();
					    over.setFontAndSize(bf2, 10);
					    over.showTextAligned(Element.ALIGN_CENTER,"-"+ (i+1) +"-",10,10,0);
					    over.showTextAligned(Element.ALIGN_CENTER,author,550,10,0);
					    over.endText();
					}else{
						under = stamp.getUnderContent(i+1);
				        under.beginText();
				        under.setFontAndSize(bf, 32);
				        under.setCMYKColorFill(64, 11, 0, 0);
				        under.showTextAligned(Element.ALIGN_CENTER, "", 297.5f, 421, 45);
				        
				        under.setFontAndSize(bf, 10);
				        under.setCMYKColorFill(64, 11, 0, 0);
				        under.showTextAligned(Element.ALIGN_CENTER, "",320.5f, 410, 45);
				        under.endText(); 
						
				        over = stamp.getOverContent(i+1);
					    over.beginText();
					    over.setFontAndSize(bf2, 10);
					    over.showTextAligned(Element.ALIGN_CENTER,"-"+ (i+1) +"-",10,10,0);
					    over.showTextAligned(Element.ALIGN_CENTER,author,550,10,0);
					    over.endText();
					}
				    i++; 
				 }
				 
				 stamp.close();
				 reader.close();
				 new FileUtil().deleteFile(oldFile);
				 outputFileName = waterMarkPDF;
			 }		
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		
		
		return url+outputFileName;
	}
	
	public String readFile(String fileName) throws Exception {
		
		byte[] read = new FileIO().getBytes(CPUtil.getCanonicalTemplatePath()+File.separator+fileName);
		
		return new String(read, "utf-8");
	}
	
	public String textToExl(String text) throws Exception {
		outputFileName = defaultFileName("xls");
		try {
			  Workbook wb = new HSSFWorkbook();
			  FileOutputStream fileOut = new FileOutputStream(filePath+outputFileName);
			  
              fileOut.write(text.getBytes());
              wb.write(fileOut);
			  fileOut.close();
			    
		  }catch (Exception e) {
			  e.printStackTrace();
		  }
		return url+outputFileName;
	}
	
	public String textToExl(String text, HSSFWorkbook wb) throws Exception {
		outputFileName = text + ".xls";
		
		try {
			FileOutputStream fileOut = new FileOutputStream(filePath+outputFileName);
			wb.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return url+outputFileName;
	}
	
	
	private String defaultFileName(String contentType){
		String str = CommonUtil.toString(System.currentTimeMillis());
		String MD5 = "";
		try{
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte byteData[] = md.digest();
			StringBuffer sb = new StringBuffer();
			for(int i = 0 ; i < byteData.length ; i++){
				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
			}
			MD5 = sb.toString();
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace();
			MD5 = null;
		}
		outputFileName = MD5+"."+contentType;
		return outputFileName;
	}

	
	
	
	
	
	
	
	

}
