package com.pgmate.sugi.model.chart;

import java.util.List;

/**
 * @author Administrator
 *
 */
public class StatisticChartBean {

	private List<String> categories = null;
	private List<BarChartBean> series = null;
	
	public StatisticChartBean(){
		
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public List<BarChartBean> getSeries() {
		return series;
	}

	public void setSeries(List<BarChartBean> series) {
		this.series = series;
	}

	
	
	
	
}


