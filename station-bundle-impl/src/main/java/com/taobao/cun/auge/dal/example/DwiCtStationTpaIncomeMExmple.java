package com.taobao.cun.auge.dal.example;

import java.io.Serializable;

public class DwiCtStationTpaIncomeMExmple implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 连续的月份
	 */
	private String[] bizMonths;
	
	/**
	 * 连续n个月
	 */
	private int lastMonthCount;
	
	/**
	 * 排名范围，例如前20%
	 */
	private long scale;

	public String[] getBizMonths() {
		return bizMonths;
	}

	public void setBizMonths(String[] bizMonths) {
		this.bizMonths = bizMonths;
	}

	public int getLastMonthCount() {
		return lastMonthCount;
	}

	public void setLastMonthCount(int lastMonthCount) {
		this.lastMonthCount = lastMonthCount;
	}

	public long getScale() {
		return scale;
	}

	public void setScale(long scale) {
		this.scale = scale;
	}
	
	
}
