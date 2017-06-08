package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.util.Date;

import com.taobao.cun.auge.common.OperatorDto;

public class PartnerPeixunListDetailDto extends OperatorDto implements Serializable{

	private static final long serialVersionUID = 1L;
	private String partnerName;
	private String phoneNum;
	private String taobaoNick;
	private String stationName;
	private Long stationId;
	private String courseType;
	private String courseStatus;
	private String qiHangStatus;
	private Date gmtDone;
	private String qiHangStatusDesc;
	private String chengZhangStatus;
	private String chengZhangStatusDesc;
	private Date gmtQiHangDone;
	private Date gmtChengZhangDone;
	private String gmtQiHangDoneDesc;
	private String gmtChengZhangDoneDesc;
	private Long userId;
	private String refundStatus;
	private String refundStatusDesc;
	
	
	public String getRefundStatus() {
		return refundStatus;
	}
	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}
	public String getRefundStatusDesc() {
		return refundStatusDesc;
	}
	public void setRefundStatusDesc(String refundStatusDesc) {
		this.refundStatusDesc = refundStatusDesc;
	}
	public String getPartnerName() {
		return partnerName;
	}
	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getTaobaoNick() {
		return taobaoNick;
	}
	public void setTaobaoNick(String taobaoNick) {
		this.taobaoNick = taobaoNick;
	}
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public Long getStationId() {
		return stationId;
	}
	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}
	public String getQiHangStatus() {
		return qiHangStatus;
	}
	public void setQiHangStatus(String qiHangStatus) {
		this.qiHangStatus = qiHangStatus;
	}
	public String getQiHangStatusDesc() {
		return qiHangStatusDesc;
	}
	public void setQiHangStatusDesc(String qiHangStatusDesc) {
		this.qiHangStatusDesc = qiHangStatusDesc;
	}
	public String getChengZhangStatus() {
		return chengZhangStatus;
	}
	public void setChengZhangStatus(String chengZhangStatus) {
		this.chengZhangStatus = chengZhangStatus;
	}
	public String getChengZhangStatusDesc() {
		return chengZhangStatusDesc;
	}
	public void setChengZhangStatusDesc(String chengZhangStatusDesc) {
		this.chengZhangStatusDesc = chengZhangStatusDesc;
	}
	public Date getGmtQiHangDone() {
		return gmtQiHangDone;
	}
	public void setGmtQiHangDone(Date gmtQiHangDone) {
		this.gmtQiHangDone = gmtQiHangDone;
	}
	public Date getGmtChengZhangDone() {
		return gmtChengZhangDone;
	}
	public void setGmtChengZhangDone(Date gmtChengZhangDone) {
		this.gmtChengZhangDone = gmtChengZhangDone;
	}
	public String getGmtQiHangDoneDesc() {
		return gmtQiHangDoneDesc;
	}
	public void setGmtQiHangDoneDesc(String gmtQiHangDoneDesc) {
		this.gmtQiHangDoneDesc = gmtQiHangDoneDesc;
	}
	public String getGmtChengZhangDoneDesc() {
		return gmtChengZhangDoneDesc;
	}
	public void setGmtChengZhangDoneDesc(String gmtChengZhangDoneDesc) {
		this.gmtChengZhangDoneDesc = gmtChengZhangDoneDesc;
	}
	public String getCourseType() {
		return courseType;
	}
	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}
	public String getCourseStatus() {
		return courseStatus;
	}
	public void setCourseStatus(String courseStatus) {
		this.courseStatus = courseStatus;
	}
	public Date getGmtDone() {
		return gmtDone;
	}
	public void setGmtDone(Date gmtDone) {
		this.gmtDone = gmtDone;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	
}
