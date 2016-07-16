package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.station.dto.StationDecorateOrderDto;

public interface StationDecorateOrderService {

	StationDecorateOrderDto getDecorateOrderById(Long bizOrderId);
	
	
	StationDecorateOrderDto getByDecorateOrder(Long sellerTaobaoUserId, Long buyerTaobaoUserId,Long orderAmount);
}