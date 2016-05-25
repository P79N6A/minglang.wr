package com.taobao.cun.auge.station.service.impl;

import java.util.Date;

import com.taobao.cun.auge.station.dto.TaobaoNoEndTradeDto;
import com.taobao.cun.auge.station.service.TaobaoTradeOrderQueryService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@HSFProvider(serviceInterface= TaobaoTradeOrderQueryService.class)
public class TaobaoTradeOrderQueryServiceImpl implements TaobaoTradeOrderQueryService{

	@Override
	public TaobaoNoEndTradeDto findNoEndTradeOrders(Long buyerId, Date endDate) {
		// TODO Auto-generated method stub
		return null;
	}

}
