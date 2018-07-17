package com.taobao.cun.auge.data.partner.dto;

import java.math.BigDecimal;

public class PartnerAccessDto {
	private String type;
	
	private int totalNum;
	
	private int accessNum;
	
	private BigDecimal percentNum;
	
	private int stateDate;

	public int getStateDate() {
		return stateDate;
	}

	public void setStateDate(int stateDate) {
		this.stateDate = stateDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public int getAccessNum() {
		return accessNum;
	}

	public void setAccessNum(int accessNum) {
		this.accessNum = accessNum;
	}

	public BigDecimal getPercentNum() {
		return percentNum;
	}

	public void setPercentNum(BigDecimal percentNum) {
		this.percentNum = percentNum;
	}
}
