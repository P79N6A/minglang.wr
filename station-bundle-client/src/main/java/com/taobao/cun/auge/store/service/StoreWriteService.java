package com.taobao.cun.auge.store.service;

import com.taobao.cun.auge.store.dto.StoreCategory;
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
	
	/**
	 * 测试接口不要调用
	 * @param shareStoreId
	 * @param category
	 * @return
	 */
	Boolean updateStoreTag(Long shareStoreId,StoreCategory category);
	
	/**
	 * 创建拍样门店
	 * @param stationId
	 * @return
	 */
	Boolean createSampleStore(Long stationId);
}
