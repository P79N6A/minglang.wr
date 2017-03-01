package com.taobao.cun.auge.station.dto;

import com.taobao.cun.auge.common.OperatorDto;

public class PartnerInstanceThrawSuccessDto extends OperatorDto {

	private static final long serialVersionUID = 4357245380089170482L;

	/**
	 * 主键
	 */
	private Long instanceId;

	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}

}
