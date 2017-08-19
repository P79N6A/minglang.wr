package com.taobao.cun.auge.store.service;

import com.taobao.cun.auge.store.dto.StoreDto;

public interface StoreReadService {

	StoreDto getStoreByStationId(Long stationId);
}
