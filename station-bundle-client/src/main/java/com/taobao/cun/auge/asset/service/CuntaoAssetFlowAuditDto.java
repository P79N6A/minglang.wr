package com.taobao.cun.auge.asset.service;

import java.io.Serializable;


public class CuntaoAssetFlowAuditDto implements Serializable  {

	private static final long serialVersionUID = -7942490965653126106L;
	
	/**流程id
	 * 
	 */
	private Long assetFlowId;
	
	/**
	 * 审批状态
	 */
	private AssetFlowApplyStatusEnum ApplyStatus;
	
	/**
	 * 工作流备注
	 */
	private String workFLowRemarks;
	
	/**
	 * 节点ID
	 */
	private String taskId;
	
	/**
	 * 流程ID
	 */
	private String procInsId;
	
	private Long stationOrgId;
	

	public Long getAssetFlowId() {
		return assetFlowId;
	}

	public void setAssetFlowId(Long assetFlowId) {
		this.assetFlowId = assetFlowId;
	}

	public AssetFlowApplyStatusEnum getApplyStatus() {
		return ApplyStatus;
	}

	public void setApplyStatus(AssetFlowApplyStatusEnum applyStatus) {
		ApplyStatus = applyStatus;
	}

	public String getWorkFLowRemarks() {
		return workFLowRemarks;
	}

	public void setWorkFLowRemarks(String workFLowRemarks) {
		this.workFLowRemarks = workFLowRemarks;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}

	public Long getStationOrgId() {
		return stationOrgId;
	}

	public void setStationOrgId(Long stationOrgId) {
		this.stationOrgId = stationOrgId;
	}
}
