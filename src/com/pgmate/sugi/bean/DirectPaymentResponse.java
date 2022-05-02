package com.pgmate.sugi.bean;

import java.util.ArrayList;
import java.util.List;

import com.pgmate.lib.util.map.SharedMap;

public class DirectPaymentResponse {
	
	public SharedMap<String,Object> result = new SharedMap<String,Object>();
	public SharedMap<String,Object> pay = new SharedMap<String,Object>();
	public List<SharedMap<String,Object>> products = new ArrayList<SharedMap<String,Object>>();
	
	public DirectPaymentResponse() {
		// TODO Auto-generated constructor stub
	}
}
