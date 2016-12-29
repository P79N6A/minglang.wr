package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

import com.taobao.cun.auge.station.enums.ProcessApproveResultEnum;

public class ProcessApproveResultDto implements Serializable {

	private static final long serialVersionUID = -10219403422276130L;

	/**
	 * 业务code
	 */
	private String businessCode;

	/**
	 * 业务主键id
	 */
	private String objectId;
	/**
	 * 审批结果
	 */
	private ProcessApproveResultEnum result;

	/**
	 * 备注
	 */
	private String remarks;

	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public ProcessApproveResultEnum getResult() {
		return result;
	}

	public void setResult(ProcessApproveResultEnum result) {
		this.result = result;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
