package com.taobao.cun.auge.station.bo;

import java.util.Date;

import com.taobao.cun.auge.trade.dto.TaobaoNoEndTradeDto;

public interface TaobaoTradeBO {

	public TaobaoNoEndTradeDto findNoEndTradeOrders(Long buyerId, Date endDate);
}
