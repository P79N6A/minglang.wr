package com.taobao.cun.auge.meeting.dto;

import java.io.Serializable;

public class MeetingQueryCondition implements Serializable{

	private static final long serialVersionUID = 1L;
	private String userId;
	private String userType;
	private String queryType;
	private Long meetingId;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getQueryType() {
		return queryType;
	}
	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}
	public Long getMeetingId() {
		return meetingId;
	}
	public void setMeetingId(Long meetingId) {
		this.meetingId = meetingId;
	}
	
	
}
