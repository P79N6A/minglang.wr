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
	
	/**
	 * 县点状态
	 */
	private String state;
	
	private String fullIdPath;
	
	private Date protocolEndDateFrom;
	
	private Date protocolEndDateTo;

	private String tag;

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

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

	public Date getProtocolEndDateFrom() {
		return protocolEndDateFrom;
	}

	public void setProtocolEndDateFrom(Date protocolEndDateFrom) {
		this.protocolEndDateFrom = protocolEndDateFrom;
	}

	public Date getProtocolEndDateTo() {
		return protocolEndDateTo;
	}

	public void setProtocolEndDateTo(Date protocolEndDateTo) {
		this.protocolEndDateTo = protocolEndDateTo;
	}
}
