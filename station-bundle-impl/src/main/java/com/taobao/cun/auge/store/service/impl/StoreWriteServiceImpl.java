package com.taobao.cun.auge.store.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.taobao.cun.auge.store.bo.StoreWriteBO;
import com.taobao.cun.auge.store.dto.StoreCategory;
import com.taobao.cun.auge.store.dto.StoreCreateDto;
import com.taobao.cun.auge.store.service.StoreException;
import com.taobao.cun.auge.store.service.StoreWriteService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@HSFProvider(serviceInterface = StoreWriteService.class)
public class StoreWriteServiceImpl implements StoreWriteService {
	@Resource
	private StoreWriteBO storeWriteBO;
	
	@Override
	public Long create(StoreCreateDto dto) throws StoreException{
		return storeWriteBO.create(dto);
	}

	@Override
	public Boolean updateStoreTag(Long shareStoreId,StoreCategory category) {
		return storeWriteBO.updateStoreTag(shareStoreId,category);
	}

	@Override
	public Boolean createSampleStore(Long stationId) {
		return storeWriteBO.createSampleStore(stationId);
	}

	@Override
	public Boolean createSupplyStore(Long stationId){
		return storeWriteBO.createSupplyStore(stationId);
	}

	@Override
	public Boolean initSampleWarehouse(Long stationId) {
		return storeWriteBO.initSampleWarehouse(stationId);
	}

	@Override
	public Boolean initStoreWarehouse(Long stationId) {
		return storeWriteBO.initStoreWarehouse(stationId);
	}

	@Override
	public Boolean batchCreateSupplyStore(List<Long> stationIds) {
		return storeWriteBO.batchCreateSupplyStore(stationIds);
	}

	@Override
	public Long tb2gbCode(Long taobaocode) {
		return storeWriteBO.tb2gbCode(taobaocode);
	}

	@Override
	public Boolean batchUpdateStore(List<Long> sharedStoreIds) {
		return storeWriteBO.batchUpdateStore(sharedStoreIds);
	}

	@Override
	public Boolean batchRemoveCainiaoFeature(List<Long> stationIds) {
		return storeWriteBO.batchRemoveCainiaoFeature(stationIds);
	}
}
