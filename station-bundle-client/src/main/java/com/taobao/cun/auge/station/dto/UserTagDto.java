package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;

public class UserTagDto implements Serializable {

	private static final long serialVersionUID = -5842295776075501429L;
	@NotNull
	private Long taobaoUserId;
	@NotNull
	private PartnerInstanceTypeEnum partnerType;

	public Long getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}

	public PartnerInstanceTypeEnum getPartnerType() {
		return partnerType;
	}

	public void setPartnerType(PartnerInstanceTypeEnum partnerType) {
		this.partnerType = partnerType;
	}

}
