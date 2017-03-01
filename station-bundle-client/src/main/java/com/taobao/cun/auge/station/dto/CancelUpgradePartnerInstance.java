package com.taobao.cun.auge.station.dto;

import javax.validation.constraints.NotNull;

import com.taobao.cun.auge.common.OperatorDto;

public class CancelUpgradePartnerInstance extends OperatorDto {

	private static final long serialVersionUID = 5080663174569378143L;

	@NotNull(message = "instanceId not null")
	private Long instanceId;

	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}
}
