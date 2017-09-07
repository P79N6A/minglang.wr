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
	
	/**
	 * 获取门店信息
	 * @param scmCode
	 * @return
	 */
	StoreDto getStoreByScmCode(String scmCode);
	
	/**
	 * 获取门店信息
	 * @param scmCode
	 * @return
	 */
	StoreDto getStoreBySharedStoreId(Long sharedStoreId);
	
	/**
	 * 获取门店距离
	 * @param stationId
	 * @param x
	 * @param y
	 * @param distance
	 * @return
	 */
	 String getStoreDistance(Long stationId,Double lng,Double lat);
}
