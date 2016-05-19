package com.taobao.cun.auge.station.strategy;

import com.taobao.cun.auge.station.dto.ApplySettleDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;

public interface PartnerInstanceStrategy {

	public Long applySettle(ApplySettleDto applySettleDto,PartnerInstanceTypeEnum partnerInstanceTypeEnum)throws AugeServiceException;
	
	public void validateExistValidChildren(Long instanceId)throws AugeServiceException ;
}
