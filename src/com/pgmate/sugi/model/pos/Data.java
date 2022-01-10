package com.pgmate.sugi.model.pos;

import java.util.List;

/**
 * @author Administrator
 *
 */
public class Data {

	public String receiptNo		= "";	//영수증 번호 
	public String payType		= "";	//카드 현금 복합 구분 ( 'CARD','CASH','CC')
	public long amount			= 0;	//총 금액
	public String issuer		= "";	//카드 발급사 
	public String authNo	= "";		//승인번호
	public String trnTime		= "";	//카드 응답 시간 또는 거래 시간 
	
	public List<Order> order	= null;
	
	public Data() {
		// TODO Auto-generated constructor stub
	}

}
