package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

public class PartnerCourseScheduleReflectDto implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long taobaoUserId;

    private Long scheduleId;

    private String courseCode;

    private String attempNum;

    private String courseSatisfied;

    private String teacherSatisfied;

    private String childSatisfied;

    private String description;

	public Long getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}

	public Long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getAttempNum() {
		return attempNum;
	}

	public void setAttempNum(String attempNum) {
		this.attempNum = attempNum;
	}

	public String getCourseSatisfied() {
		return courseSatisfied;
	}

	public void setCourseSatisfied(String courseSatisfied) {
		this.courseSatisfied = courseSatisfied;
	}

	public String getTeacherSatisfied() {
		return teacherSatisfied;
	}

	public void setTeacherSatisfied(String teacherSatisfied) {
		this.teacherSatisfied = teacherSatisfied;
	}

	public String getChildSatisfied() {
		return childSatisfied;
	}

	public void setChildSatisfied(String childSatisfied) {
		this.childSatisfied = childSatisfied;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
    
    
}
