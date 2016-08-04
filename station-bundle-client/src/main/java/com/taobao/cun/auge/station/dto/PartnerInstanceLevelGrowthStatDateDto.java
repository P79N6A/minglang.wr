package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

public class PartnerInstanceLevelGrowthStatDateDto implements Serializable{
	
	private static final long serialVersionUID = 8206206311712077629L;

	/**
	 * 统计日期
	 */
	private String statDate;
	
	/**
	 * 统计开始日期
	 */
	private String statStartDate;
	/**
	 * 统计截止日期
	 * 
	 */
	private String statEndDate;
	
	public String getStatDate() {
		return statDate;
	}
	public void setStatDate(String statDate) {
		this.statDate = statDate;
	}
	public String getStatStartDate() {
		return statStartDate;
	}
	public void setStatStartDate(String statStartDate) {
		this.statStartDate = statStartDate;
	}
	public String getStatEndDate() {
		return statEndDate;
	}
	public void setStatEndDate(String statEndDate) {
		this.statEndDate = statEndDate;
	}
}
