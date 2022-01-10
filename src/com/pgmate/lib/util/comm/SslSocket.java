package com.pgmate.lib.util.comm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import com.pgmate.lib.util.lang.CommonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 *
 */
public class SslSocket {

	private Logger logger = LoggerFactory.getLogger( getClass() );
	
	private SSLSocket socket 				= null;
	private InputStream inputStream 	= null;
	private OutputStream outputStream 	= null;
	
	private String host					= "";
	private int port					= 0;
	private int timeout					= 0;
	
	/*
	 * 기본 생성자 TcpSocket을 생성한다.
	 */
	public SslSocket(){
	}
	
	/**
	 * 이미 생성된 Socket 을 이용하여 send, recv 할 때 사용한다.
	 * @param socket
	 */
	public SslSocket(SSLSocket socket){
		this.socket = socket;
	}
	
	/**
	 * Socket 생성시 필요한 필수값 설정 
	 * @param host
	 * @param port
	 * @param timeout
	 */
	public void setSocketProperty(String host,int port,int timeout){
		this.host 		= host;
		this.port 		= port;
		this.timeout 	= timeout;
	}
	
	/**
	 * Socket 생성시 필요한 필수값 설정 
	 * @param host
	 * @param port
	 * @param timeout
	 */
	public void setSocketProperty(String host,String port,String timeout){
		this.host 		= host;
		this.port 		= CommonUtil.parseInt(port);
		this.timeout 	= CommonUtil.parseInt(timeout);
	}
	
	
	/**
	 * connect() 전에 Map 에 trustStore 에 대한 정보를 설정한다.
	 * debug = "javax.net.debug","ssl", ("javax.net.ssl.trustStore",trustStore_pass), "sun.security.ssl.allowUnsafeRenegotiation","true"
	 * @param map
	 */
	public void setTrustStore(Map<String,String> map){
		if(System.getProperty("javax.net.ssl.trustStore") == null){
			if(map.size() !=0){
				for (Entry<String, String> entry : map.entrySet()) {
					System.setProperty(entry.getKey(),entry.getValue());
		        }
			}
		}
		
	}
	
	
	/**
	 * Socket 접속 및 InputStream, OutputStream의 생성 
	 * @throws IOException
	 */
	public void connect() throws IOException {
		try{
			SSLSocketFactory f = (SSLSocketFactory) SSLSocketFactory.getDefault();
			socket = (SSLSocket) f.createSocket(host, port);
			socket.setSoTimeout(timeout);
			outputStream= socket.getOutputStream();
			inputStream = socket.getInputStream();
		}catch(IOException e){
			logger.debug("connect error : {}",CommonUtil.getExceptionMessage(e));
			throw new IOException(e);
		}
	}
	
	
	/**
	 * byte[] 기반의 송신 및 수신 
	 * Stream 및 Socket 은 자동으로 Closing된다.
	 * @param send
	 * @return
	 * @throws IOException
	 */
	public byte[] sendRecv(byte[] send) throws IOException {
		byte[] recv = null;
		try{
			connect();
			send(send);
			recv = recv();
		}catch(IOException e){
			logger.debug("sendRecv error : {}",CommonUtil.getExceptionMessage(e));
			throw new IOException(e);
		}finally{
			ioClose();
			socketClose();
		}
		return recv;
	}
	
	/**
	 * String 기반의 송신 및 수신
	 * Stream 및 Socket 은 자동으로 Closing된다.
	 * @param send
	 * @return
	 * @throws IOException
	 */
	public String sendRecv(String send) throws IOException {
		return new String(sendRecv(send.getBytes()));
	}
	
	
	/**
	 * 보내는 데이터를 charsetName 으로 인코딩하여 byte로 변환하고 , 
	 * 수신된 데이터는 charsetNAme 으로 인코딩하여 String  으로 반환한다.
	 * Stream 및 Socket 은 자동으로 Closing된다.
	 * @param send
	 * @param charsetName
	 * @return
	 * @throws IOException
	 */
	public String sendRecv(String send,String charsetName) throws IOException {
		byte[] recv = sendRecv(send.getBytes(charsetName));
		return new String(recv,charsetName);
	}
	
	

	/**
	 * 특정한 길이열을 지정하지 않고 전체를 수신
	 * 네트워크에 문제가 발생 할 수도 있을 경우 사용하며. 기본값으로 지정되어 있다.
	 * Socket 및 Stream 은 close되지 않는다.
	 * @return
	 * @throws IOException
	 */
	public byte[] recv() throws IOException {
		if(inputStream == null ){
			inputStream = socket.getInputStream();
		}
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
        int bcount = 0;
        byte[] buf = new byte[2048];
        int read_retry_count = 0;
        while(true) {
			int n = inputStream.read(buf);
            if ( n > 0 ) { bcount += n; bout.write(buf,0,n); }
            else if (n == -1) break;
            else  { // n == 0
                if (++read_retry_count >= 5)
                  throw new IOException("inputstream-read-retry-count(5) exceed !");
            }
            if(inputStream.available() == 0){ break; }
        }
        bout.flush();
        byte[] res = bout.toByteArray();
        bout.close();
        return res;
	}
	
	/**
	 * 지정된 문자열만큼 데이터 수신.
	 * Socket 및 Stream 은 close되지 않는다.
	 * @param size
	 * @return
	 * @throws IOException
	 */
	public byte[] recv(int size) throws IOException{
		if(inputStream == null ){
			inputStream = socket.getInputStream();
		}
		byte[] recv = new byte[size];

		boolean run = true;
		int recvedSize = 0;

		while(run){
			byte[] temp = new byte[size-recvedSize];
			int read = inputStream.read(temp);
			CommonUtil.arrayCopy(recv, recvedSize,temp,read);
			recvedSize += read;
			if(recvedSize >= size){
				run = false;
			}
		}
		return recv;
	}
	
	/**
	 * byte 기반의 송신 
	 * Socket 및 Stream 은 close되지 않는다.
	 * @param data
	 * @throws IOException
	 */
	public void send(byte[] data)throws IOException{
		if(outputStream == null){
			outputStream = socket.getOutputStream();
		}
		/*
		int len = data.length;
		if(len > 1024){
			int cnt = data.length/1024;
			if(len % 1024 != 0){
				cnt++;
			}
			for(int i=0;i<cnt;i++){
				if(i+1 == cnt){
					outputStream.write(data,1024*i,len-(1024*i));
					
				}else{
					outputStream.write(data,1024*i,1024*(i+1));
				}
			}
			
			
		}else{
			outputStream.write(data,0,data.length);
		}*/
		outputStream.write(data,0,data.length);
		outputStream.flush();
	}
	
	/**
	 * socket 이 현재 유효한지 여부를 확인할 때 사용한다.
	 * Socket 및 Stream 은 close되지 않는다.
	 * @return
	 */
	public boolean isAlive(){
		if(socket.isOutputShutdown()){
			return false;
		}
		if(socket.isClosed()){
			return false;
		}
		return true;
	}
	
	
	/**
	 * InputStream 과 OutputStream 에 대한 종료 
	 */
	public void ioClose() throws IOException{
		if(inputStream != null){ inputStream.close();}
		if(outputStream != null){ outputStream.close();}
	}

	/**
	 * Socket 사용이 완료되었을 때. 해당 Socket 의 접속을 끊는다.
	 */
	public void socketClose(){
		if(socket != null){
			try{
				if(inputStream != null || outputStream != null){
					ioClose();
				}
				socket.close();
				socket = null;
			}catch(IOException e){
				logger.debug("close error : {}",CommonUtil.getExceptionMessage(e));
			}
		}
	}
	
	
	public String getClientIp()throws IOException{
		return CommonUtil.toString(socket.getInetAddress().toString());
	}
	
	
	
	
	
	
	
	
	
	
	
}
