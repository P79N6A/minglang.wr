package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

public class PartnerInstanceExtDto implements Serializable {

	private static final long serialVersionUID = 5575424342867122921L;

	private Long instanceId;

	private int curChildNum;

	private int maxChildNum;

	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}

	public int getCurChildNum() {
		return curChildNum;
	}

	public void setCurChildNum(int curChildNum) {
		this.curChildNum = curChildNum;
	}

	public int getMaxChildNum() {
		return maxChildNum;
	}

	public void setMaxChildNum(int maxChildNum) {
		this.maxChildNum = maxChildNum;
	}

}
