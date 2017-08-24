package com.taobao.cun.auge.store.service;

import com.taobao.cun.auge.store.dto.StoreDto;

public interface StoreReadService {
	
	/**
	 * 根据站点ID查询门店
	 * @param stationId
	 * @return
	 */
	StoreDto getStoreByStationId(Long stationId);
	
	/**
	 * 根据淘宝账号查询门店
	 * @param taobaoUserId
	 * @return
	 */
	StoreDto getStoreByTaobaoUserId(Long taobaoUserId);
}
