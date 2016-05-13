package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.station.condition.PartnerInstanceCondition;
import com.taobao.cun.auge.station.exception.AugeServiceException;

public interface TPAPatnerInstanceService extends PatnerInstanceService {
	
	
	/**
	 * 申请入驻
	 * 
	 * @param condition
	 * @return
	 */
	public Long applySettle(PartnerInstanceCondition condition) throws AugeServiceException;
}
