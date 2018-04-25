package com.taobao.cun.auge.dal.domain;

import javax.persistence.Column;

public class InspectionStatusSummary {

	@Column
	private String status;
	
	@Column
	private Integer count;
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	
}
