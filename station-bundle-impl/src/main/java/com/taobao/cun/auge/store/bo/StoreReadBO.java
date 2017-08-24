package com.taobao.cun.auge.store.bo;

import com.taobao.cun.auge.store.dto.StoreDto;

public interface StoreReadBO {
	StoreDto getStoreDtoByStationId(Long stationId);
	
	StoreDto getStoreDtoByTaobaoUserId(Long taobaoUserId);
}
