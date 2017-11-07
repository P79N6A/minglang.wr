package com.taobao.cun.auge.store.service.impl;

import javax.annotation.Resource;

import com.taobao.cun.auge.store.bo.StoreWriteBO;
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
	public Boolean updateStoreTag(Long shareStoreId) {
		return storeWriteBO.updateStoreTag(shareStoreId);
	}

}
