package com.taobao.cun.auge.partner.service;

import java.util.Date;

import com.taobao.cun.auge.station.exception.AugeServiceException;

public interface PartnerTradeService {
	/**
	 * 校验是否有未结束的订单
	 * 
	 * @param buyerId
	 * @param endDate
	 * @throws AugeServiceException
	 */
	public void validateNoEndTradeOrders(Long buyerId, Date endDate) throws AugeServiceException;
}
