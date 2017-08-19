package com.taobao.cun.auge.store.bo;

import com.taobao.cun.auge.store.dto.StoreDto;

public interface StoreReadBO {
	StoreDto getStoreDtoByStation(Long stationId);
}
