package com.taobao.cun.auge.event;

import java.util.Date;

import com.taobao.cun.auge.common.OperatorDto;

public class AssetChangeEvent extends OperatorDto{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1850095593443493656L;

	private Date operateTime;
	
	private String type;
	
	private Long assetId;
	
	private String status;
	
	private String description;

	private String operatorId;
	public AssetChangeEvent(){}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getAssetId() {
		return assetId;
	}

	public void setAssetId(Long assetId) {
		this.assetId = assetId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}
	
}
