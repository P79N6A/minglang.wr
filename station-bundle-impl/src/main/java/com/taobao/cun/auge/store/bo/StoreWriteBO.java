package com.taobao.cun.auge.store.bo;

import com.taobao.cun.auge.store.dto.StoreCategory;
import com.taobao.cun.auge.store.dto.StoreCreateDto;
import com.taobao.cun.auge.store.service.StoreException;

public interface StoreWriteBO {
	Long create(StoreCreateDto dto) throws StoreException;
	
	Boolean updateStoreTag(Long shareStoreId,StoreCategory category);
	
}
