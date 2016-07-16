package com.taobao.cun.auge.station.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.cun.auge.station.bo.StationDecorateOrderBO;
import com.taobao.cun.auge.station.dto.StationDecorateOrderDto;
import com.taobao.cun.auge.station.service.StationDecorateOrderService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("stationDecorateOrderService")
@HSFProvider(serviceInterface = StationDecorateOrderService.class)
public class StationDecorateOrderServiceImpl implements StationDecorateOrderService {

	@Autowired
	private StationDecorateOrderBO stationDecorateOrderBO;
	
	public StationDecorateOrderDto getDecorateOrderById(Long bizOrderId){
		return stationDecorateOrderBO.getDecorateOrderById(bizOrderId).orElse(null);
	}

	public StationDecorateOrderDto getByDecorateOrder(Long sellerTaobaoUserId, Long buyerTaobaoUserId,Long orderAmount){
		return stationDecorateOrderBO.getDecorateOrder(sellerTaobaoUserId,buyerTaobaoUserId,orderAmount).orElse(null);
	}

}
