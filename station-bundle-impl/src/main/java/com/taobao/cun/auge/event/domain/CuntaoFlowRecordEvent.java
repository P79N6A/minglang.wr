package com.taobao.cun.auge.event.domain;

import java.io.Serializable;
import java.util.Date;

import com.taobao.cun.crius.event.annotation.EventDesc;
import com.taobao.cun.crius.event.annotation.EventField;

@EventDesc("cuntao-flow-record-event")
public class CuntaoFlowRecordEvent implements Serializable {

	private static final long serialVersionUID = -5349467603652567585L;

	@EventField("业务主键id")
	private Long targetId;

	@EventField("业务类型")
	private String targetType;

	@EventField("日志节点名称")
	private String nodeTitle;

	@EventField("操作人姓名")
	private String operatorName;

	@EventField("操作人工号")
	private String operatorWorkid;

	@EventField("操作时间")
	private Date operateTime;

	@EventField("操作人意见")
	private String operateOpinion;

	@EventField("备注")
	private String remarks;

	@EventField("流程flow主键id")
	private Long flowId;

	@EventField("鹰眼trace_id")
	private String trace_id;

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
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

	public String getTrace_id() {
		return trace_id;
	}

	public void setTrace_id(String trace_id) {
		this.trace_id = trace_id;
	}

}
