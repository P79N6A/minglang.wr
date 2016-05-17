package com.taobao.cun.auge.station.service;

import java.util.Date;

import com.taobao.cun.auge.station.dto.TaobaoNoEndTradeDto;

public interface TaobaoTradeOrderQueryService {
	
	  /**
     * 查询未完结的订单，包括退款的订单,按照各自的分页查询
     * @param buyerId
     * @param endDate
     * @return
     */
    TaobaoNoEndTradeDto findNoEndTradeOrders(Long buyerId,Date endDate);
    
}
