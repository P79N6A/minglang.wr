package com.taobao.cun.auge.store.service;

import java.util.List;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.store.dto.StoreDto;
import com.taobao.cun.auge.store.dto.StoreQueryPageCondition;
import com.taobao.cun.auge.store.dto.StoreStatus;

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
	 * 获取站点距离
	 * @param stationId
	 * @param x
	 * @param y
	 * @param distance
	 * @return
	 */
	 String[] getStationDistance(Long stationId,Double lng,Double lat);
	 
	 /**
	  * 获取所有门店ID
	  * @param status
	  * @return
	  */
	 List<Long> getAllStoreIdsByStatus(StoreStatus status);
	 
	 /**
	  * 分页查询名单列表
	  */
	 Result<PageDto<StoreDto>> queryStoreByPage(StoreQueryPageCondition storeQueryPageCondition);
	 
	 /**
	  * 批量门店ID查询门店列表
	  */
	 List<StoreDto> getStoreByStationIds(List<Long> stationIds);
	 
	 /**
	  * 共享门店ID批量查询门店
	  * @param sharedStoreId
	  * @return
	  */
	 List<StoreDto> getStoreBySharedStoreIds(List<Long> sharedStoreId);
	 
}
