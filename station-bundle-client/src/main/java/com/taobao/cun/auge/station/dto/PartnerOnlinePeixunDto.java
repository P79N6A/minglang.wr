package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

import com.taobao.cun.auge.station.enums.PartnerOnlinePeixunStatusEnum;

public class PartnerOnlinePeixunDto implements Serializable{

	private static final long serialVersionUID = 1L;
	private Long taobaoUserId;
	private String courseCode;
	private String courseUrl;
	private String examUrl;
	private PartnerOnlinePeixunStatusEnum status;
	public Long getTaobaoUserId() {
		return taobaoUserId;
	}
	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}
	public String getCourseCode() {
		return courseCode;
	}
	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}
	public String getCourseUrl() {
		return courseUrl;
	}
	public void setCourseUrl(String courseUrl) {
		this.courseUrl = courseUrl;
	}
	public String getExamUrl() {
		return examUrl;
	}
	public void setExamUrl(String examUrl) {
		this.examUrl = examUrl;
	}
	public PartnerOnlinePeixunStatusEnum getStatus() {
		return status;
	}
	public void setStatus(PartnerOnlinePeixunStatusEnum status) {
		this.status = status;
	}
	
	
}
