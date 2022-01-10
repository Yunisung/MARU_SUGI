package com.pgmate.sugi.model.pos;

/**
 * @author Administrator
 *
 */
public class Order {

	public long idx			= 0;	//순서 번호
	public String barcode	= "";	//바코드 번호
	public long qty			= 0;	//수량 
	public long price		= 0;	//객 단가	
	public long totPrice	= 0;	//수량* 객 단가 : 총 금액
	
	
	public Order() {
		// TODO Auto-generated constructor stub
	}

}
