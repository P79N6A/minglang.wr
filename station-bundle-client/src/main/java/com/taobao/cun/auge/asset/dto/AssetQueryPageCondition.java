package com.taobao.cun.auge.asset.dto;

import java.util.List;

import com.taobao.cun.auge.common.PageQuery;

public class AssetQueryPageCondition extends PageQuery {

	private static final long serialVersionUID = -7816448399626409295L;

	private String aliNo;
	
	private String poNo;
	
	private List<String> statusList;
	
	private String checkStatus;
	
	private Long stationId;
	
	private String fullIdPath;

	//使用人id
	private String userId;

	public String getAliNo() {
		return aliNo;
	}

	public void setAliNo(String aliNo) {
		this.aliNo = aliNo;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public List<String> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<String> statusList) {
		this.statusList = statusList;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
