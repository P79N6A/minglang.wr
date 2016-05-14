package com.taobao.cun.auge.station.strategy;

import com.taobao.cun.auge.station.condition.PartnerInstanceCondition;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;

public interface PartnerInstanceStrategy {

	public Long applySettle(PartnerInstanceCondition condition,PartnerInstanceTypeEnum partnerInstanceTypeEnum)throws AugeServiceException;
	
	public void validateExistValidChildren(Long instanceId)throws AugeServiceException ;
}
