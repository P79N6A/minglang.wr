package com.taobao.cun.auge.station.bo;

import java.util.Optional;

import com.taobao.cun.auge.station.dto.StationDecorateOrderDto;

public interface StationDecorateOrderBO {

	Optional<StationDecorateOrderDto> getDecorateOrderById(Long bizOrderId);

	Optional<StationDecorateOrderDto> getDecorateOrder(Long sellerTaobaoUserId, Long buyerTaobaoUserId);

    public void judgeTcOrderStatusForQuit(Long sellerTaobaoUserId, Long buyerTaobaoUserId);
}