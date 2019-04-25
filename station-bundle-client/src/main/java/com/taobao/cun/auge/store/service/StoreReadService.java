package com.taobao.cun.auge.store.service;

import java.util.Date;
import java.util.List;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.store.dto.StoreDto;
import com.taobao.cun.auge.store.dto.StoreQueryPageCondition;
import com.taobao.cun.auge.store.dto.StoreStatus;

public interface StoreReadService {
	
	/**
	 * 根据站点ID查询门店
	 * @param stationId
	 * @return
	 */
	StoreDto getStoreByStationId(Long stationId);
	
	/**
	 * 根据淘宝账号查询门店
	 * @param taobaoUserId
	 * @return
	 */
	StoreDto getStoreByTaobaoUserId(Long taobaoUserId);
	
	/**
	 * 获取门店信息
	 * @param scmCode
	 * @return
	 */
	StoreDto getStoreByScmCode(String scmCode);
	
	/**
	 * 获取门店信息
	 * @param sharedStoreId
	 * @return
	 */
	StoreDto getStoreBySharedStoreId(Long sharedStoreId);
	
	/**
	 * 获取站点距离
	 */
	 String[] getStationDistance(Long stationId,Double lng,Double lat);
	 
	 /**
	  * 获取所有门店ID
	  * @param status
	  * @return
	  */
	 List<Long> getAllStoreIdsByStatus(StoreStatus status);
	 
	 /**
	  * 分页查询名单列表
	  */
	 Result<PageDto<StoreDto>> queryStoreByPage(StoreQueryPageCondition storeQueryPageCondition);
	 
	 /**
	  * 批量门店ID查询门店列表
	  */
	 List<StoreDto> getStoreByStationIds(List<Long> stationIds);
	 
	 /**
	  * 共享门店ID批量查询门店
	  * @param sharedStoreId
	  * @return
	  */
	 List<StoreDto> getStoreBySharedStoreIds(List<Long> sharedStoreId);
	 
	 /**
	  * 根据门店员工查询门店
	  * @param taobaoUserId
	  * @return
	  */
	 StoreDto getStoreByStoreEmployeeTaobaoUserId(Long taobaoUserId);
	 
	 
	 /**
	  * 根据卖家的共享门店ID批量查询门店
	  * @param sellerShareStoreId
	  * @return
	  */
	 List<StoreDto> getStoreBySellerShareStoreIds(List<Long> sellerShareStoreId);
	 
	 /**
	  * 是否是测试门店
	  * @param sharedStoreId
	  * @return
	  */
	 boolean isTestStore(Long sharedStoreId);
	 
	 /**
	  *  服务业务判断，门店是否可以退出。
	  * @param storeId
	  * @return
	  */
	 public Boolean serviceJudgmentForStoreQuit(Long storeId);
	 
	 /**
	  * 批量查询 要开送货入户权限的门店信息
	  * @return
	  */
	 public List<Long> queryListForShrhPermission(Date beginDate);

	/**
	 * 批量查询 要开送货入户权限的门店信息
	 * @return
	 */
	public PageDto<Long> queryByPageForShrhPermission(Date beginDate, int pageNum,int pageSize);

	/**
	 * 批量查询 要开送货入户权限的体验店信息
	 * @return
	 */
	public PageDto<Long> queryByPageForShrhPermissionToTPS(Date beginDate, int pageNum,int pageSize);

}
