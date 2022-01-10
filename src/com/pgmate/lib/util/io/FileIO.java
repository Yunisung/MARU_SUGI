package com.pgmate.lib.util.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.pgmate.lib.util.lang.CommonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Administrator
 *
 */
public class FileIO {

	private static Logger logger = LoggerFactory.getLogger( com.pgmate.lib.util.io.FileIO.class);
	
	
	public FileIO(){
	}
	
	/**
	 * 파일을 StringBuilder 로 읽어오기. suffix 는 마지막에 \r\n 등을 지정할 수 있다
	 * @param fileName
	 * @param suffix
	 * @return
	 * @throws IOException
	 */
	public static StringBuilder readLine(String fileName,String suffix)throws IOException{
		StringBuilder sb = new StringBuilder();
		List<String> list = readLineList(fileName);
		for(String line:list){
			sb.append(line+suffix);
		}
		return sb;
	}
	
	/**
	 * 파일을 List 형태로 읽어 오기 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static List<String> readLineList(String fileName)throws IOException{
		List<String> list = new ArrayList<String>();
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(fileName));
			String str;
			while ((str = in.readLine()) != null) {
				if(str != null){
					if(!str.trim().equals("")){
						list.add(str);
					}
				}
			}
		}catch (IOException e) {
			logger.debug("error : {}",CommonUtil.getExceptionMessage(e));
		}finally{
			close(in);
		}
		return list;
	}
	
	/**
	 * 파일을 StringBuffer 로 읽어오기 suffix 는 마지막에 \r\n 등을 지정할 수 있으며 charsetName을 지정할 수 있다.
	 * @param fileName
	 * @param suffix
	 * @return
	 * @throws IOException
	 */
	public static StringBuilder readLine(String fileName,String suffix,String charsetName)throws IOException{
		StringBuilder sb = new StringBuilder();
		List<String> list = readLineList(fileName,charsetName);
		for(String line:list){
			sb.append(line+suffix);
		}
		return sb;
	}
	public static List<String> readLineList(String fileName,String charsetName)throws IOException{
		List<String> list = new ArrayList<String>();
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), charsetName));
			String str;
			while ((str = in.readLine()) != null) {
				if(str != null){
					if(!str.trim().equals("")){
						list.add(str);
					}
				}
			}
		}catch (IOException e) {
			logger.debug("error : {}",CommonUtil.getExceptionMessage(e));
		}finally{
			close(in);
		}
		return list;
	}
	
	/**
	 * 파일을 ByteArrayOutpuStream 으로 읽어 온다.
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static byte[] getBytes(String fileName)throws IOException{
		File file = new File(fileName);
		if ( file.length() > Integer.MAX_VALUE ) {	throw new IOException("exceed integer max value");
		}
		
		ByteArrayOutputStream ous = null;
		InputStream ios = null;
		
		try {
			byte[] buffer = new byte[4096];
			ous = new ByteArrayOutputStream();
			ios = new FileInputStream(file);
			int read = 0;
		    while ( (read = ios.read(buffer)) != -1 ) {
		    	ous.write(buffer, 0, read);
		    }
		    
		}catch(IOException e){
			logger.debug("error : {}",CommonUtil.getExceptionMessage(e));
			throw new IOException(e);
		}finally {
			close(ous);
		    close(ios);
		}
		return ous.toByteArray();
	}
	
	
	/**
	 * byte[] 의 binary 파일 write
	 * @param fileName
	 * @param data
	 * @return
	 * @throws IOException
	 */
	public static boolean writeBinary(String fileName,byte[] data) throws IOException{
		boolean isWrite = false;
		FileOutputStream out = null;
		
		try{
			out = new FileOutputStream(new File(fileName));
			
			out.write(data);
			out.flush();
			isWrite = true;
		}catch(IOException e){
			logger.debug("error : {}",CommonUtil.getExceptionMessage(e));
			throw new IOException(e);
		}finally {
			close(out);
		}
		return isWrite;	
	}
	
	/**
	 * String data 에 대한 파일 쓰기 charset은 기본 File.encoding 을 따른다.
	 * @param fileName
	 * @param data
	 * @return
	 * @throws IOException
	 */
	public static boolean write(String fileName,String data) throws IOException{
		boolean isWrite = false;
		BufferedWriter writer = null;
		try{
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)));
			writer.write(data);
			isWrite = true;
		}catch(IOException e){
			logger.debug("error : {}",CommonUtil.getExceptionMessage(e));
			throw new IOException(e);
		}finally {
			close(writer);
		}
		return isWrite;	
	}
	
	/**
	 * String data 에 대한 파일 쓰기 charset을 지정가능하다.
	 * @param fileName
	 * @param data
	 * @param charsetName
	 * @return
	 * @throws IOException
	 */
	public static boolean write(String fileName,String data,String charsetName) throws IOException{
		boolean isWrite = false;
		BufferedWriter writer = null;
		try{
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName),charsetName));
			writer.write(data);
			isWrite = true;
		}catch(IOException e){
			logger.debug("error : {}",CommonUtil.getExceptionMessage(e));
			throw new IOException(e);
		}finally {
			close(writer);
		}
		return isWrite;	
	}
	
	public static boolean printWrite(String fileName,String data,boolean append) throws IOException{
		boolean isWrite = false;
		PrintWriter writer = null;
		try{
			writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName,append)));
			writer.println(data);
			isWrite = true;
		}catch(IOException e){
			logger.debug("error : {}",CommonUtil.getExceptionMessage(e));
			throw new IOException(e);
		}finally {
			close(writer);
		}
		return isWrite;	
	}
	
	

	private static void close(OutputStream output){
		try{
			output.close();
		}catch(IOException e){
			logger.debug("OutputStream close error : {}"+e.getMessage());
		}
	}
	
	private static void close(InputStream input){
		try{
			input.close();
		}catch(IOException e){
			logger.debug("InputStream close error : {}"+e.getMessage());
		}
	}
	
	private static void close(Reader reader){
		try{
			reader.close();
		}catch(IOException e){
			logger.debug("OutputStream close error : {}"+e.getMessage());
		}
	}
	
	public static void close(Writer writer){
		try{
			writer.close();
		}catch(IOException e){
			logger.debug("OutputStream close error : {}"+e.getMessage());
		}
	}
	
}
