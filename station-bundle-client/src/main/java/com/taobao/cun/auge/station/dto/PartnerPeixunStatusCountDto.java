package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

public class PartnerPeixunStatusCountDto implements Serializable{

	private static final long serialVersionUID = 1L;
	private String status;
	private String statusDesc;
	private String peixunCode;
	private String peixunDesc;
	private String courseType;
	private Integer countNum;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusDesc() {
		return statusDesc;
	}
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
	public String getPeixunCode() {
		return peixunCode;
	}
	public void setPeixunCode(String peixunCode) {
		this.peixunCode = peixunCode;
	}
	public String getPeixunDesc() {
		return peixunDesc;
	}
	public void setPeixunDesc(String peixunDesc) {
		this.peixunDesc = peixunDesc;
	}
	public Integer getCountNum() {
		return countNum;
	}
	public void setCountNum(Integer countNum) {
		this.countNum = countNum;
	}
	public String getPeixunType() {
		return courseType;
	}
	public void setPeixunType(String peixunType) {
		this.courseType = peixunType;
	}
	
	
}
