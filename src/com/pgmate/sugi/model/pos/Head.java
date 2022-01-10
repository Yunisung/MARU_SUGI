package com.pgmate.sugi.model.pos;

/**
 * @author Administrator
 *
 */
public class Head {

	public String merchantId	= "";	//사업자 번호
	public String tmnId			= "";	//POS 또는 터미널 ID
	public String time			= "";	//전송 시간
	public String trnType		= "";	//승인/취소 구분 ('AUTH','REFUND')
	public String trnId			= "";	//거래번호
	public String comments		= "";

	
	public Head() {
		// TODO Auto-generated constructor stub
	}

}
