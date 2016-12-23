package com.taobao.cun.auge.station.adapter;

import java.util.Date;

import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.exception.AugeSystemException;

public interface TradeAdapter {
	
	/**
	 * 校验是否存在未结束的订单
	 * 
	 * @param buyerId
	 * @param endDate
	 */
	public void validateNoEndTradeOrders(Long buyerId, Date endDate)throws AugeBusinessException,AugeSystemException;
}
