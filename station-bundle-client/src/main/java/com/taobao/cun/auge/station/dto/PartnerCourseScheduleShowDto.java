package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class PartnerCourseScheduleShowDto implements Serializable{

	private static final long serialVersionUID = 1L;
	private Date courseDate;
	private String courseDateDesc;
	private String weekendDesc;
	private List<PartnerCourseScheduleDetailDto> courses;
	public Date getCourseDate() {
		return courseDate;
	}
	public void setCourseDate(Date courseDate) {
		this.courseDate = courseDate;
	}
	public String getCourseDateDesc() {
		return courseDateDesc;
	}
	public void setCourseDateDesc(String courseDateDesc) {
		this.courseDateDesc = courseDateDesc;
	}
	public List<PartnerCourseScheduleDetailDto> getCourses() {
		return courses;
	}
	public void setCourses(List<PartnerCourseScheduleDetailDto> courses) {
		this.courses = courses;
	}
	public String getWeekendDesc() {
		return weekendDesc;
	}
	public void setWeekendDesc(String weekendDesc) {
		this.weekendDesc = weekendDesc;
	}
	
	
}
