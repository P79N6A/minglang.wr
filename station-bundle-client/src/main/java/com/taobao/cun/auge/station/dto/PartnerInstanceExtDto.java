package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

import com.taobao.cun.auge.common.OperatorDto;

public class PartnerInstanceExtDto extends OperatorDto implements Serializable {

	private static final long serialVersionUID = 5575424342867122921L;

	private Long instanceId;

	private Integer curChildNum;

	private Integer maxChildNum;

	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}

	public Integer getCurChildNum() {
		return curChildNum;
	}

	public void setCurChildNum(Integer curChildNum) {
		this.curChildNum = curChildNum;
	}

	public Integer getMaxChildNum() {
		return maxChildNum;
	}

	public void setMaxChildNum(Integer maxChildNum) {
		this.maxChildNum = maxChildNum;
	}
}
