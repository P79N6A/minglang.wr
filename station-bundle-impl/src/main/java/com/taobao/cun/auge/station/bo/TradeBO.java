package com.taobao.cun.auge.station.bo;

import java.util.Date;

import com.taobao.cun.auge.station.exception.AugeServiceException;

public interface TradeBO {
	public void validateNoEndTradeOrders(Long buyerId,Date endDate) throws AugeServiceException;
}
