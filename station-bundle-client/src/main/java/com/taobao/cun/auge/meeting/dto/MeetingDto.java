package com.taobao.cun.auge.meeting.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class MeetingDto implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long id;

    private String meetingType;

    private String ownerId;

    private String ownerType;

    private Date gmtStart;

    private Date gmtEnd;

    private String status;

    private String title;

    private String description;
    
    private String meetingPassword;

    private String meetingUrl;

    private String meetingUuid;

    private String meetingCode;
    
    private List<MeetingAttempDto> meetingAttemps;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getMeetingType() {
		return meetingType;
	}

	public void setMeetingType(String meetingType) {
		this.meetingType = meetingType;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Date getGmtStart() {
		return gmtStart;
	}

	public void setGmtStart(Date gmtStart) {
		this.gmtStart = gmtStart;
	}

	public Date getGmtEnd() {
		return gmtEnd;
	}

	public void setGmtEnd(Date gmtEnd) {
		this.gmtEnd = gmtEnd;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<MeetingAttempDto> getMeetingAttemps() {
		return meetingAttemps;
	}

	public void setMeetingAttemps(List<MeetingAttempDto> meetingAttemps) {
		this.meetingAttemps = meetingAttemps;
	}

	public String getMeetingPassword() {
		return meetingPassword;
	}

	public void setMeetingPassword(String meetingPassword) {
		this.meetingPassword = meetingPassword;
	}

	public String getMeetingUrl() {
		return meetingUrl;
	}

	public void setMeetingUrl(String meetingUrl) {
		this.meetingUrl = meetingUrl;
	}

	public String getMeetingUuid() {
		return meetingUuid;
	}

	public void setMeetingUuid(String meetingUuid) {
		this.meetingUuid = meetingUuid;
	}

	public String getMeetingCode() {
		return meetingCode;
	}

	public void setMeetingCode(String meetingCode) {
		this.meetingCode = meetingCode;
	}
    
    
}
