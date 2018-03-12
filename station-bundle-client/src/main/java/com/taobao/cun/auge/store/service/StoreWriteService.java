package com.taobao.cun.auge.store.service;

import java.util.List;

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
	
	/**
	 * 创建补货村点门店
	 * @param stationId
	 * @return
	 */
	public Boolean createSupplyStore(Long stationId);
	
	/**
	 * 创建拍样库存
	 * @param stationId
	 * @return
	 */
	public Boolean initSampleWarehouse(Long stationId);
	
	/**
	 * 构建门店库存
	 * @param stationId
	 * @return
	 */
	public Boolean initStoreWarehouse(Long stationId);
	
	
	public Boolean batchInitStoreWarehouse(List<Long> stationIds);
	
	/**
	 * 批量初始化补货村点门店
	 * @param stationIds
	 * @return
	 */
	public Boolean batchCreateSupplyStore(List<Long> stationIds);
	
	/**
	 * 淘标国标地址转换
	 * @param taobaocode
	 * @return
	 */
	public Long tb2gbCode(Long taobaocode);
	
	
	public Boolean batchUpdateStore(List<Long> sharedStoreIds);
	
	/**
	 * 批量删除菜鸟标
	 * @param stationIds
	 * @return
	 */
	public Boolean batchRemoveCainiaoFeature(List<Long> stationIds);
	
	/**
	 * 同步村点Endor组织
	 * @param station
	 * @return
	 */
	public Boolean initEndorOrg(Long station);
	
	/**
	 * 批量同步门店组织
	 */
	public void batchInitStoreEndorOrg();
}
