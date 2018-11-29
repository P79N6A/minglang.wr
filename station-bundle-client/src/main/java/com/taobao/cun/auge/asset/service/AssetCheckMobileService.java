package com.taobao.cun.auge.asset.service;

import com.taobao.cun.auge.asset.dto.AssetCheckInfoAddDto;
import com.taobao.cun.auge.asset.dto.AssetCheckInfoCondition;
import com.taobao.cun.auge.asset.dto.AssetCheckInfoDto;
import com.taobao.cun.auge.asset.dto.CountyCheckCountDto;
import com.taobao.cun.auge.asset.dto.CountyFollowCheckCountDto;
import com.taobao.cun.auge.asset.dto.FinishTaskForCountyDto;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.PageDto;

/**
 * 资产盘点手机服务
 * @author quanzhu.wangqz
 *
 */
public interface AssetCheckMobileService {

	/**
	 * 完结站点盘点任务
	 * @param taobaoUserId
	 */
	public void finishTaskForStation(Long taobaoUserId);
	
	/**
	 * 完结县点盘点任务
	 * @param FinishTaskForCountyDto
	 */
	public void finishTaskForCounty(FinishTaskForCountyDto param);
	
	/**
	 * 新增盘点信息
	 * @param addDto
	 */
	public void addCheckInfo(AssetCheckInfoAddDto addDto);
	
	/**
	 * 删除盘点信息
	 * @param infoId
	 * @param operator
	 */
	public void delCheckInfo(Long infoId,OperatorDto operator);
	
	/**
	 * 盘点信息列表
	 * @param param
	 * @return
	 */
	public PageDto<AssetCheckInfoDto> listInfo(AssetCheckInfoCondition  param);
	/**
	 * 县盘点总数
	 * @param countyOrgId
	 * @return
	 */
	public CountyCheckCountDto getCountyCheckCount(Long countyOrgId);
	/**
	 * 县跟踪盘点总数
	 * @param countyOrgId
	 * @return
	 */
	public CountyFollowCheckCountDto getCountyFollowCheckCount(Long countyOrgId);
	
}
