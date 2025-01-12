package com.taobao.cun.auge.station.service.impl;

import com.taobao.cun.auge.station.bo.StationDecorateOrderBO;
import com.taobao.cun.auge.station.dto.StationDecorateOrderDto;
import com.taobao.cun.auge.station.service.StationDecorateOrderService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("stationDecorateOrderService")
@HSFProvider(serviceInterface = StationDecorateOrderService.class)
public class StationDecorateOrderServiceImpl implements StationDecorateOrderService {

	@Autowired
	private StationDecorateOrderBO stationDecorateOrderBO;
	
	@Override
    public StationDecorateOrderDto getDecorateOrderById(Long bizOrderId){
		return stationDecorateOrderBO.getDecorateOrderById(bizOrderId).orElse(null);
	}

	@Override
	public StationDecorateOrderDto getDecorateOrder(Long sellerTaobaoUserId, Long buyerTaobaoUserId){
		return stationDecorateOrderBO.getDecorateOrder(sellerTaobaoUserId,buyerTaobaoUserId).orElse(null);
	}

}
