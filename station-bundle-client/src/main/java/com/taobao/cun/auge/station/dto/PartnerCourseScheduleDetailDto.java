package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.util.Date;


public class PartnerCourseScheduleDetailDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String courseName;
	private String courseDesc;
	private Date gmtBegin;
	private Date gmtEnd;
	private Date gmtCourse;
	private String status;
	private Integer seqNum;
	private String courseUrl;
	private String courseType;
	private String courseCode;
	private Long id;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCourseUrl() {
		return courseUrl;
	}
	public void setCourseUrl(String courseUrl) {
		this.courseUrl = courseUrl;
	}
	public String getCourseType() {
		return courseType;
	}
	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}
	public Integer getSeqNum() {
		return seqNum;
	}
	public void setSeqNum(Integer seqNum) {
		this.seqNum = seqNum;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getCourseDesc() {
		return courseDesc;
	}
	public void setCourseDesc(String courseDesc) {
		this.courseDesc = courseDesc;
	}
	public Date getGmtBegin() {
		return gmtBegin;
	}
	public void setGmtBegin(Date gmtBegin) {
		this.gmtBegin = gmtBegin;
	}
	public Date getGmtEnd() {
		return gmtEnd;
	}
	public void setGmtEnd(Date gmtEnd) {
		this.gmtEnd = gmtEnd;
	}
	public Date getGmtCourse() {
		return gmtCourse;
	}
	public void setGmtCourse(Date gmtCourse) {
		this.gmtCourse = gmtCourse;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCourseCode() {
		return courseCode;
	}
	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}
	
	

}
