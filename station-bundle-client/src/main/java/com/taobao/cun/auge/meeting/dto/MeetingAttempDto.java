package com.taobao.cun.auge.meeting.dto;

import java.io.Serializable;
import java.util.Date;

public class MeetingAttempDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;

	private Long meetingId;

	private String attemperId;

	private String attemperType;

	private Date gmtAttemp;

	private Date gmtLeave;

	private String status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public Long getMeetingId() {
		return meetingId;
	}

	public void setMeetingId(Long meetingId) {
		this.meetingId = meetingId;
	}

	public String getAttemperId() {
		return attemperId;
	}

	public void setAttemperId(String attemperId) {
		this.attemperId = attemperId;
	}

	public String getAttemperType() {
		return attemperType;
	}

	public void setAttemperType(String attemperType) {
		this.attemperType = attemperType;
	}

	public Date getGmtAttemp() {
		return gmtAttemp;
	}

	public void setGmtAttemp(Date gmtAttemp) {
		this.gmtAttemp = gmtAttemp;
	}

	public Date getGmtLeave() {
		return gmtLeave;
	}

	public void setGmtLeave(Date gmtLeave) {
		this.gmtLeave = gmtLeave;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
