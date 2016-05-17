package com.taobao.cun.auge.station.strategy;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.station.condition.PartnerInstanceCondition;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;

@Component("tpaStrategy")
public class TpaStrategy implements PartnerInstanceStrategy {

	@Override
	public Long applySettle(PartnerInstanceCondition condition,
			PartnerInstanceTypeEnum partnerInstanceTypeEnum)
			throws AugeServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validateExistValidChildren(Long instanceId) {
		// TODO Auto-generated method stub
		
	}
	
}
