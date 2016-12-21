package com.taobao.cun.auge.station.dto;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.enums.PartnerMaxChildNumChangeReasonEnum;

public class PartnerChildMaxNumUpdateDto extends OperatorDto {
	
	private static final long serialVersionUID = 1752137567596084418L;

	private Long instanceId;

	private Integer maxChildNum;

	private String childNumChangDate;

	// 变更原因
	private PartnerMaxChildNumChangeReasonEnum reason;

	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}

	public Integer getMaxChildNum() {
		return maxChildNum;
	}

	public void setMaxChildNum(Integer maxChildNum) {
		this.maxChildNum = maxChildNum;
	}

	public String getChildNumChangDate() {
		return childNumChangDate;
	}

	public void setChildNumChangDate(String childNumChangDate) {
		this.childNumChangDate = childNumChangDate;
	}

	public PartnerMaxChildNumChangeReasonEnum getReason() {
		return reason;
	}

	public void setReason(PartnerMaxChildNumChangeReasonEnum reason) {
		this.reason = reason;
	}

}
