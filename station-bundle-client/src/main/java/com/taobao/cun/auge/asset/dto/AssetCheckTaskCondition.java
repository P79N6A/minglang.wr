package com.taobao.cun.auge.asset.dto;

import com.taobao.cun.auge.common.PageQuery;

/**
 * 
 * @author quanzhu.wangqz
 *
 */
public class AssetCheckTaskCondition  extends PageQuery {

	private static final long serialVersionUID = 5422593447655623370L;

	private String stationName;
	
	private String orgName;
	
	private String taskType;
	
	private String taskStatus;
	
	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	
	

}
