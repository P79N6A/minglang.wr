package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.util.Date;

public class PartnerCourseRecordDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private Date gmtCreate;

	private Date gmtModified;

	private String creator;

	private String modifier;

	private String isDeleted;

	private String courseType;

	private String courseCode;

	private String status;

	private String orderNum;

	private Date gmtDone;

	private Long partnerUserId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Date getGmtModified() {
		return gmtModified;
	}

	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getCourseType() {
		return courseType;
	}

	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public Date getGmtDone() {
		return gmtDone;
	}

	public void setGmtDone(Date gmtDone) {
		this.gmtDone = gmtDone;
	}

	public Long getPartnerUserId() {
		return partnerUserId;
	}

	public void setPartnerUserId(Long partnerUserId) {
		this.partnerUserId = partnerUserId;
	}

}
