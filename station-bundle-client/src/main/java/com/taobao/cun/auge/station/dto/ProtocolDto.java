package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.util.Date;

import com.taobao.cun.auge.station.enums.ProtocolGroupTypeEnum;
import com.taobao.cun.auge.station.enums.ProtocolStateEnum;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;

public class ProtocolDto implements Serializable {
	
	private static final long serialVersionUID = -2730692635052885624L;
	
	private Long id;
	private Date submitTime;
	private String type;
	private ProtocolTypeEnum protocolTypeEnum;
	private String name;
	private Long version;
	private String submitId;
	private String state;
	private ProtocolStateEnum protocolStateEnum;
	private String description;
	private String groupType;
	private ProtocolGroupTypeEnum protocolGroupTypeEnum;
	private String groupName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getSubmitId() {
		return submitId;
	}

	public void setSubmitId(String submitId) {
		this.submitId = submitId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public ProtocolTypeEnum getProtocolTypeEnum() {
		return protocolTypeEnum;
	}

	public void setProtocolTypeEnum(ProtocolTypeEnum protocolTypeEnum) {
		this.protocolTypeEnum = protocolTypeEnum;
	}

	public ProtocolStateEnum getProtocolStateEnum() {
		return protocolStateEnum;
	}

	public void setProtocolStateEnum(ProtocolStateEnum protocolStateEnum) {
		this.protocolStateEnum = protocolStateEnum;
	}

	public ProtocolGroupTypeEnum getProtocolGroupTypeEnum() {
		return protocolGroupTypeEnum;
	}

	public void setProtocolGroupTypeEnum(ProtocolGroupTypeEnum protocolGroupTypeEnum) {
		this.protocolGroupTypeEnum = protocolGroupTypeEnum;
	}
}
