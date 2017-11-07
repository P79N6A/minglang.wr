package com.taobao.cun.auge.store.service;

import com.taobao.cun.auge.store.dto.StoreCreateDto;

/**
 * 
 * 店铺的写操作
 *
 */
public interface StoreWriteService {
	/**
	 * 创建店铺
	 * @param dto
	 * @return 返回店铺ID
	 */
	Long create(StoreCreateDto dto) throws StoreException;
	
	Boolean updateStoreTag(Long shareStoreId);
}
