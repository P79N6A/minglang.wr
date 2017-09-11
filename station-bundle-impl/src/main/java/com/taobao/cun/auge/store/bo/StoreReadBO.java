package com.taobao.cun.auge.store.bo;

import com.taobao.cun.auge.store.dto.StoreDto;

public interface StoreReadBO {
	StoreDto getStoreDtoByStationId(Long stationId);
	
	StoreDto getStoreDtoByTaobaoUserId(Long taobaoUserId);
	
	/**
	 * 获取门店信息
	 * @param scmCode
	 * @return
	 */
	StoreDto getStoreByScmCode(String scmCode);
	
    StoreDto getStoreBySharedStoreId(Long sharedStoreId);
    
    String getStoreDistance(Long stationId,Double lng,Double lat);
}
