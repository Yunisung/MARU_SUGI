package com.pgmate.sugi.model.pos;

/**
 * @author Administrator
 *
 */
public class POSDATA {

	public Head head		= null;		//공통부
	public Data merchant	= null;		//사업자 결제 부분
	public Data anypay		= null;		//애니페이 결제 부분 
	
	
	public POSDATA() {
		// TODO Auto-generated constructor stub
	}

}
