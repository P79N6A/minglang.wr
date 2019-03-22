package com.taobao.cun.auge.cuncounty.dto;

import java.util.Date;

import com.taobao.cun.auge.common.PageInput;

/**
 * 查询条件
 * 
 * @author chengyu.zhoucy
 *
 */
public class CuntaoCountyCondition extends PageInput {

	private String name;
	
	private Long orgId;
	
	private String fullIdPath;
	
	private Date protocolStartDate;
	
	private Date protocolEndDate;

	public String getFullIdPath() {
		return fullIdPath;
	}

	public void setFullIdPath(String fullIdPath) {
		this.fullIdPath = fullIdPath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Date getProtocolStartDate() {
		return protocolStartDate;
	}

	public void setProtocolStartDate(Date protocolStartDate) {
		this.protocolStartDate = protocolStartDate;
	}

	public Date getProtocolEndDate() {
		return protocolEndDate;
	}

	public void setProtocolEndDate(Date protocolEndDate) {
		this.protocolEndDate = protocolEndDate;
	}
}
