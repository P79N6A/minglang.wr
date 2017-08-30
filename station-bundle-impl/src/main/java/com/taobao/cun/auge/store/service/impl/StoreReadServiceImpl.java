package com.taobao.cun.auge.store.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.cun.auge.store.bo.StoreReadBO;
import com.taobao.cun.auge.store.dto.StoreDto;
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

}
