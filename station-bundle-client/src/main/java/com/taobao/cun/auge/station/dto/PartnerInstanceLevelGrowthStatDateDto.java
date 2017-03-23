package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.util.Date;

public class PartnerInstanceLevelGrowthStatDateDto implements Serializable{
	
	private static final long serialVersionUID = 8206206311712077629L;

	/**
	 * 统计日期
	 */
	private String statDate;
	
	/**
	 * 统计开始日期
	 */
	private Date statStartDate;
	/**
	 * 统计截止日期
	 * 
	 */
	private Date statEndDate;
	
	public String getStatDate() {
		return statDate;
	}
	public void setStatDate(String statDate) {
		this.statDate = statDate;
	}
	public Date getStatStartDate() {
		return statStartDate;
	}
	public void setStatStartDate(Date statStartDate) {
		this.statStartDate = statStartDate;
	}
	public Date getStatEndDate() {
		return statEndDate;
	}
	public void setStatEndDate(Date statEndDate) {
		this.statEndDate = statEndDate;
	}
	
}
