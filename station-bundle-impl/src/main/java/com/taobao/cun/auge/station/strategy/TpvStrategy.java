package com.taobao.cun.auge.station.strategy;

import com.taobao.cun.auge.station.condition.PartnerInstanceCondition;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;

public class TpvStrategy implements PartnerInstanceStrategy {

	@Override
	public Long applySettle(PartnerInstanceCondition condition,
			PartnerInstanceTypeEnum partnerInstanceTypeEnum)
			throws AugeServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validateExistServiceChildren(Long instanceId) {
		// TODO Auto-generated method stub
		
	}

}
