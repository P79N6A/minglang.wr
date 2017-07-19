package com.taobao.cun.auge.station.adapter;

import java.util.Date;

public interface TradeAdapter {
	
	/**
	 * 校验是否存在未结束的订单
	 * 
	 * @param buyerId
	 * @param endDate
	 */
	public void validateNoEndTradeOrders(Long buyerId, Date endDate);
}
