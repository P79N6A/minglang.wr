package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;

/**
 * 申请入驻dto
 * @author quanzhu.wangqz
 *
 */
public class ApplySettleDto  extends BaseDto implements Serializable {

	private static final long serialVersionUID = 4878635616874042880L;
	
	private PartnerInstanceTypeEnum partnerInstanceTypeEnum;

	public PartnerInstanceTypeEnum getPartnerInstanceTypeEnum() {
		return partnerInstanceTypeEnum;
	}

	public void setPartnerInstanceTypeEnum(
			PartnerInstanceTypeEnum partnerInstanceTypeEnum) {
		this.partnerInstanceTypeEnum = partnerInstanceTypeEnum;
	}

}
