package com.taobao.cun.auge.asset.service;

import java.io.Serializable;
import java.util.List;

public class AssetQueryCondition implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 323031550867884156L;

	
	private String aliNo;
	
	private String boNo;
	
	private String status;
	
	private String province;
	
	private String county;
	
	private String serialNo;
	
	private String checkStatus;
	
	private String role;
	
	private Integer pageSize;
	
	private Integer pageNum;
	
	private Long orgId;
	
	private Long stationId;
	
	private List<String> states;
	
	private List<String> noStates;
	
	private String fullIdPath;

	public String getAliNo() {
		return aliNo;
	}

	public void setAliNo(String aliNo) {
		this.aliNo = aliNo;
	}

	public String getBoNo() {
		return boNo;
	}

	public void setBoNo(String boNo) {
		this.boNo = boNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}


	public String getFullIdPath() {
		return fullIdPath;
	}

	public void setFullIdPath(String fullIdPath) {
		this.fullIdPath = fullIdPath;
	}

	public List<String> getStates() {
		return states;
	}

	public void setStates(List<String> states) {
		this.states = states;
	}

	public List<String> getNoStates() {
		return noStates;
	}

	public void setNoStates(List<String> noStates) {
		this.noStates = noStates;
	}
	
	
}
