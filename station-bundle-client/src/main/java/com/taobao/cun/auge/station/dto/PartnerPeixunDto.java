package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PartnerPeixunDto implements Serializable{

	private static final long serialVersionUID = 1L;
	private Long userId;
	private String courseCode;
	private String status;
	private String statusDesc;
	private String orderNum;
	private Date gmtDone;
	private String courseType;
	private String courseTypeDesc;
	private String courseName;
	private BigDecimal courseAmount;
	private String logo;
	private Date gmtOrder;
	private String ticketNo;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
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
	public String getStatusDesc() {
		return statusDesc;
	}
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
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
	public String getCourseType() {
		return courseType;
	}
	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}
	public String getCourseTypeDesc() {
		return courseTypeDesc;
	}
	public void setCourseTypeDesc(String courseTypeDesc) {
		this.courseTypeDesc = courseTypeDesc;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public BigDecimal getCourseAmount() {
		return courseAmount;
	}
	public void setCourseAmount(BigDecimal courseAmount) {
		this.courseAmount = courseAmount;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public Date getGmtOrder() {
		return gmtOrder;
	}
	public void setGmtOrder(Date gmtOrder) {
		this.gmtOrder = gmtOrder;
	}
	public String getTicketNo() {
		return ticketNo;
	}
	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}
	
	
}
