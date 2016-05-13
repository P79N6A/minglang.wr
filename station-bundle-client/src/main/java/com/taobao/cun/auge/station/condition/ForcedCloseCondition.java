package com.taobao.cun.auge.station.condition;

import java.io.Serializable;

public class ForcedCloseCondition implements Serializable{

	private static final long serialVersionUID = -2259932744519518324L;
	//合伙人实例id
	private Long instanceId;

	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}
	
	
	
}
