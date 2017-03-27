package com.taobao.cun.auge.flowRecord.condition;

import javax.validation.constraints.NotNull;

import com.taobao.cun.auge.common.PageQuery;
import com.taobao.cun.auge.flowRecord.enums.CuntaoFlowRecordTargetTypeEnum;

public class CuntaoFlowRecordPageCondition extends PageQuery {
	private static final long serialVersionUID = 9206736673975554774L;
	
	@NotNull(message="targetId is null")
	private Long targetId;
	
	@NotNull(message="CuntaoFlowRecordTargetTypeEnum is null")
	private CuntaoFlowRecordTargetTypeEnum targetType;

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
}
