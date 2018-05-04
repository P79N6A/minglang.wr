package com.taobao.cun.auge.store.bo;

import java.util.List;

import com.taobao.cun.auge.store.dto.StoreCategory;
import com.taobao.cun.auge.store.dto.StoreCreateDto;
import com.taobao.cun.auge.store.service.StoreException;

public interface StoreWriteBO {
	Long create(StoreCreateDto dto) throws StoreException;
	
	Boolean updateStoreTag(Long shareStoreId,StoreCategory category);
	
	public Boolean createSampleStore(Long stationId);
	
	public Boolean createSupplyStore(Long stationId);
	
	public Boolean batchCreateSupplyStore(List<Long> stationIds);
	
	public Boolean initSampleWarehouse(Long stationId);
	
	public Boolean initStoreWarehouse(Long stationId);
	
	public Long tb2gbCode(Long taobaocode);
	
	public Boolean batchUpdateStore(List<Long> sharedStoreIds);
	
	public Boolean batchRemoveCainiaoFeature(List<Long> stationIds);
	
	public Boolean initStoreEndorOrg(Long stationId);
	
	public void batchInitStoreEndorOrg();
	
	public void initStoreEmployees(Long  stationId);
	
	public void initGoodSupplyFeature(Long stationId);
	
	public Integer getCountyCode(String countyCode,String countyDetail,String cityCode);
}
