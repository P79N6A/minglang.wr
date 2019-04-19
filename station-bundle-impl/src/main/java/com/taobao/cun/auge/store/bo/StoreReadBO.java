package com.taobao.cun.auge.store.bo;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.dal.domain.CuntaoStore;
import com.taobao.cun.auge.store.dto.StoreDto;
import com.taobao.cun.auge.store.dto.StoreQueryPageCondition;
import com.taobao.cun.auge.store.dto.StoreStatus;

import java.util.List;

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
    
	PageDto<StoreDto> queryStoreByPage(StoreQueryPageCondition storeQueryPageCondition);
	
	List<StoreDto> getStoreByStationIds(List<Long> stationIds);
	
	List<StoreDto> getStoreBySharedStoreIds(List<Long> sharedStoreIds);
	
	List<StoreDto> getStoreBySellerShareStoreIds(List<Long> sellerShareStoreId);

	/**
	 * 根据村点获得  CuntaoStore 数据
	 * @param stationId
	 * @return
	 */
	public CuntaoStore getCuntaoStoreByStationId(Long stationId);

	/**
	 * 根据storeId获得  CuntaoStore 数据
	 * @param sharedStoreId
	 * @return
	 */
	public CuntaoStore getCuntaoStoreBySharedStoreId(Long sharedStoreId);
}
