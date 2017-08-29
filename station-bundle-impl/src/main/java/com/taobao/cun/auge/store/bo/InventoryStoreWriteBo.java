package com.taobao.cun.auge.store.bo;

import com.taobao.cun.auge.store.dto.InventoryStoreCreateDto;
import com.taobao.cun.auge.store.service.StoreException;

/**
 * 仓库
 */
public interface InventoryStoreWriteBo {
	/**
	 * 创建仓库，返回仓库CODE
	 * 
	 * CODE可以自己指定，如果没有指定，那么生成一个code
	 * 
	 * @param inventoryStoreCreateDto
	 * @return
	 */
	String create(InventoryStoreCreateDto inventoryStoreCreateDto)throws StoreException;
}
