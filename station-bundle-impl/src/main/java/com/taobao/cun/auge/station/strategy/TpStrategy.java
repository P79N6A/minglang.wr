package com.taobao.cun.auge.station.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.condition.PartnerInstanceCondition;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.StationExceptionEnum;

public class TpStrategy implements PartnerInstanceStrategy{
	

	private static final Logger logger = LoggerFactory.getLogger(TpStrategy.class);

	@Autowired
	PartnerInstanceBO partnerInstanceBO;
	
	@Override
	public Long applySettle(PartnerInstanceCondition condition, PartnerInstanceTypeEnum partnerInstanceTypeEnum)
			throws AugeServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validateExistServiceChildren(Long instanceId) throws AugeServiceException {
		int count = partnerInstanceBO.findChildPartners(instanceId, PartnerInstanceStateEnum.SERVICING);
		if (count > 0) {
			logger.error(StationExceptionEnum.HAS_CHILDREN_TPA.getDesc());
			throw new AugeServiceException(StationExceptionEnum.HAS_CHILDREN_TPA);
		}
		
	}
	
	
	

}
