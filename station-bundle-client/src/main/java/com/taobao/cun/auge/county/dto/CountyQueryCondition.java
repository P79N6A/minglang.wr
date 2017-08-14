package com.taobao.cun.auge.county.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CountyQueryCondition implements Serializable{

	private static final long serialVersionUID = 1L;
	private String countyOfficial;
	private String teamLeader;
	private String state;
	private List<String> fullIdPaths;
	private String countyName;
	private Date leaseProtocolEndTime;
	private int pageSize;
	private int page;
	private Long orgId;
	private Boolean isMobile;
	
	
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public Boolean getIsMobile() {
		return isMobile;
	}
	public void setIsMobile(Boolean isMobile) {
		this.isMobile = isMobile;
	}
	public String getCountyOfficial() {
		return countyOfficial;
	}
	public void setCountyOfficial(String countyOfficial) {
		this.countyOfficial = countyOfficial;
	}
	public String getTeamLeader() {
		return teamLeader;
	}
	public void setTeamLeader(String teamLeader) {
		this.teamLeader = teamLeader;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public List<String> getFullIdPaths() {
		return fullIdPaths;
	}
	public void setFullIdPaths(List<String> fullIdPaths) {
		this.fullIdPaths = fullIdPaths;
	}
	public String getCountyName() {
		return countyName;
	}
	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}
	public Date getLeaseProtocolEndTime() {
		return leaseProtocolEndTime;
	}
	public void setLeaseProtocolEndTime(Date leaseProtocolEndTime) {
		this.leaseProtocolEndTime = leaseProtocolEndTime;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	
	
}
