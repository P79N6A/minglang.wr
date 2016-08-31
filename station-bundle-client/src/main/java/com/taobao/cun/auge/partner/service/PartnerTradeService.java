package com.taobao.cun.auge.partner.service;

import com.taobao.cun.auge.station.exception.AugeServiceException;

public interface PartnerTradeService {
	/**
	 * 校验是否有未结束的订单
	 * 
	 * @param instanceId
	 * @throws AugeServiceException
	 */
	public void validateNoEndTradeOrders(Long instanceId) throws AugeServiceException;
}
