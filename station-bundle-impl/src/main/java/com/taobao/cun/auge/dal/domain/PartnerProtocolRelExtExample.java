package com.taobao.cun.auge.dal.domain;

import java.io.Serializable;

public class PartnerProtocolRelExtExample implements Serializable {
	private static final long serialVersionUID = 7799503668363897681L;

	private Long objectId;

    private String targetType;

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}
    
    
}
