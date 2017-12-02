package com.taobao.cun.auge.store.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.store.bo.StoreReadBO;
import com.taobao.cun.auge.store.dto.StoreDto;
import com.taobao.cun.auge.store.dto.StoreQueryPageCondition;
import com.taobao.cun.auge.store.dto.StoreStatus;
import com.taobao.cun.auge.store.service.StoreReadService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@HSFProvider(serviceInterface = StoreReadService.class)
public class StoreReadServiceImpl implements StoreReadService {

	@Autowired
	private StoreReadBO storeReadBO;
	
	@Override
	public StoreDto getStoreByStationId(Long stationId) {
		return storeReadBO.getStoreDtoByStationId(stationId);
	}

	@Override
	public StoreDto getStoreByTaobaoUserId(Long taobaoUserId) {
		return storeReadBO.getStoreDtoByTaobaoUserId(taobaoUserId);
	}

	@Override
	public StoreDto getStoreByScmCode(String scmCode) {
		return storeReadBO.getStoreByScmCode(scmCode);
	}

	@Override
	public StoreDto getStoreBySharedStoreId(Long sharedStoreId) {
		return storeReadBO.getStoreBySharedStoreId(sharedStoreId);
	}

	@Override
	public String[] getStationDistance(Long stationId, Double lng, Double lat) {
		return storeReadBO.getStationDistance(stationId, lng, lat);
	}

	@Override
	public List<Long> getAllStoreIdsByStatus(StoreStatus status) {
		return storeReadBO.getAllStoreIdsByStatus(status);
	}

	@Override
	public Result<PageDto<StoreDto>> queryStoreByPage(StoreQueryPageCondition storeQueryPageCondition) {
		PageDto<StoreDto> stores = storeReadBO.queryStoreByPage(storeQueryPageCondition);
		return Result.of(stores);
	}

	@Override
	public List<StoreDto> getStoreByStationIds(List<Long> stationIds) {
		// TODO Auto-generated method stub
		return storeReadBO.getStoreByStationIds(stationIds);
	}

}
