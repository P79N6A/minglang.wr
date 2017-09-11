package com.taobao.cun.auge.store.bo;

import java.util.List;

import com.taobao.cun.auge.store.dto.StoreDto;
import com.taobao.cun.auge.store.dto.StoreStatus;

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
    
    String[] getStationDistance(Long stationId,Double lng,Double lat);
    
    List<Long> getAllStoreIdsByStatus(StoreStatus status);
}
