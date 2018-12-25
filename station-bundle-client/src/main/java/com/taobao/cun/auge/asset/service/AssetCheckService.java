package com.taobao.cun.auge.asset.service;

import java.util.List;

import com.taobao.cun.auge.asset.dto.AssetCheckInfoCondition;
import com.taobao.cun.auge.asset.dto.AssetCheckInfoDto;
import com.taobao.cun.auge.asset.dto.AssetCheckTaskCondition;
import com.taobao.cun.auge.asset.dto.AssetCheckTaskDto;
import com.taobao.cun.auge.asset.dto.AssetDetailDto;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.PageDto;

/**
 * 资产盘点服务
 * 
 * @author quanzhu.wangqz
 *
 */
public interface AssetCheckService {

	/**
	 * 盘点任务列表
	 * 
	 * @param param
	 * @return
	 */
	public PageDto<AssetCheckTaskDto> listTasks(AssetCheckTaskCondition param);

	/**
	 * 盘点信息列表
	 * 
	 * @param param
	 * @return
	 */
	public PageDto<AssetCheckInfoDto> listInfoForOrg(AssetCheckInfoCondition param);

	/**
	 * 资产批量确认
	 * 
	 * @param ids
	 */
	public void confirmSelect(List<Long> infoIds, Long countyOrgId, String categoryType, OperatorDto ope);

	/**
	 * 单个资产确认
	 */
	public void confirmOne(Long infoId, String aliNo, OperatorDto ope);

	/**
	 * 资产业务表 待盘点IT 资产数据
	 */
	public PageDto<AssetDetailDto> listAssetToChecking(Long countyOrgId, Integer pageNum, Integer pageSize);

	public PageDto<AssetDetailDto> listAssetToCheckingForStation(Long stationId, Integer pageNum, Integer pageSize);

	/**
	 * 资产业务表 待盘点IT 资产数据
	 */
	public PageDto<AssetDetailDto> listAssetForStation(Long stationId, Integer pageNum, Integer pageSize);

	/**
	 * 资产业务表 待盘点的特定资产总数
	 */
	public Integer getWaitCheckAsset(String categoryType, Long countyOrgId);

	/**
	 * 初始化县域盘点任务
	 * 
	 * @param orgIds
	 */
	public void initTaskForCounty(List<Long> orgIds);

	/**
	 * 单个资产打回
	 */
	public void backOne(Long infoId, String reason, OperatorDto ope);

	/**
	 * 初始化站点盘点任务
	 * 
	 * @param taskType
	 *            任务类型
	 * @param taskCode
	 *            任务code
	 * @param taobaoUserId
	 *            淘宝userid
	 */
	public void initTaskForStation(String taskType, String taskCode, Long taobaoUserId);
	
	
	/**
	 * 系统确认村点盘点信息
	 * @param infoId
	 * @param operator
	 */
	public Boolean confrimCheckInfoForSystemToStation(Long stationId,String checkerId,String checkerName);
	
	/**
	 * 系统确认县点盘点信息
	 * @param infoId
	 * @param operator
	 */
	public Boolean confrimCheckInfoForSystemToCounty(Long countyOrgId, String operator);
}
