package com.pgmate.sugi.model.chart;

/**
 * @author Administrator
 *
 */
public class BarChartBean {

	private String name = "";
	private double[] data	= null;
	
	public BarChartBean(){
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double[] getData() {
		return data;
	}

	public void setData(double[] data) {
		this.data = data;
	}
	
	
}
