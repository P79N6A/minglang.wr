package com.taobao.cun.auge.dal.example;

import java.io.Serializable;

public class DwiCtStationTpaIncomeMExtExmple implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 连续的月份
	 */
	private String[] bizMonths;
	
	/**
	 * 连续n个月
	 */
	private Integer lastMonthCount;
	
	/**
	 * 排名范围，例如前20%,scale= 0.2
	 */
	private Double scale;
	
	public String[] getBizMonths() {
		return bizMonths;
	}

	public void setBizMonths(String[] bizMonths) {
		this.bizMonths = bizMonths;
	}

	public Integer getLastMonthCount() {
		return lastMonthCount;
	}

	public void setLastMonthCount(Integer lastMonthCount) {
		this.lastMonthCount = lastMonthCount;
	}

	public Double getScale() {
		return scale;
	}

	public void setScale(Double scale) {
		this.scale = scale;
	}
}
