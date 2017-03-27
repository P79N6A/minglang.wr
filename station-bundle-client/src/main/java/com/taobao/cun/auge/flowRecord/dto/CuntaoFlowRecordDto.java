package com.taobao.cun.auge.flowRecord.dto;

import java.io.Serializable;
import java.util.Date;

import com.taobao.cun.auge.flowRecord.enums.CuntaoFlowRecordTargetTypeEnum;

public class CuntaoFlowRecordDto implements Serializable {

	private static final long serialVersionUID = -515982876194572034L;

	/**
	 * TARGET_ID
	 */
	private Long targetId;
	/**
	 * TARGET_TYPE
	 */
	private CuntaoFlowRecordTargetTypeEnum targetType;
	/**
	 * NODE_TITLE
	 */
	private String nodeTitle;
	/**
	 * OPERATOR_NAME
	 */
	private String operatorName;
	/**
	 * OPERATOR_WORKID
	 */
	private String operatorWorkid;
	/**
	 * OPERATE_TIME
	 */
	private Date operateTime;
	/**
	 * OPERATE_OPINION
	 */
	private String operateOpinion;
	/**
	 * REMARKS
	 */
	private String remarks;
	/**
	 * FLOW_ID
	 */
	private Long flowId;

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public CuntaoFlowRecordTargetTypeEnum getTargetType() {
		return targetType;
	}

	public void setTargetType(CuntaoFlowRecordTargetTypeEnum targetType) {
		this.targetType = targetType;
	}

	public String getNodeTitle() {
		return nodeTitle;
	}

	public void setNodeTitle(String nodeTitle) {
		this.nodeTitle = nodeTitle;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getOperatorWorkid() {
		return operatorWorkid;
	}

	public void setOperatorWorkid(String operatorWorkid) {
		this.operatorWorkid = operatorWorkid;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public String getOperateOpinion() {
		return operateOpinion;
	}

	public void setOperateOpinion(String operateOpinion) {
		this.operateOpinion = operateOpinion;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Long getFlowId() {
		return flowId;
	}

	public void setFlowId(Long flowId) {
		this.flowId = flowId;
	}
}
