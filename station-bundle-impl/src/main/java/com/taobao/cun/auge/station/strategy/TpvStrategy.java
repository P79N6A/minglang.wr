package com.taobao.cun.auge.station.strategy;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.station.dto.ApplySettleDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;

@Component("tpvStrategy")
public class TpvStrategy implements PartnerInstanceStrategy {

	@Override
	public Long applySettle(ApplySettleDto applySettleDto,
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
