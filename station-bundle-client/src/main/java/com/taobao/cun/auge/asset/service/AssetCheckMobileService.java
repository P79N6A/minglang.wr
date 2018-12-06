package com.taobao.cun.auge.asset.service;

import com.taobao.cun.auge.asset.dto.AssetCheckInfoAddDto;
import com.taobao.cun.auge.asset.dto.AssetCheckInfoCondition;
import com.taobao.cun.auge.asset.dto.AssetCheckInfoDto;
import com.taobao.cun.auge.asset.dto.AssetCheckTaskDto;
import com.taobao.cun.auge.asset.dto.CountyCheckCountDto;
import com.taobao.cun.auge.asset.dto.CountyFollowCheckCountDto;
import com.taobao.cun.auge.asset.dto.FinishTaskForCountyDto;
import com.taobao.cun.auge.asset.dto.QueryStationTaskCondition;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.result.Result;

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
	public Result<Boolean> finishTaskForStation(Long taobaoUserId);
	
	/**
	 * 完结县点盘点任务
	 * @param FinishTaskForCountyDto
	 */
	public Result<Boolean> finishTaskForCounty(FinishTaskForCountyDto param);
	
	/**
	 * 新增盘点信息
	 * @param addDto
	 */
	public Result<Boolean> addCheckInfo(AssetCheckInfoAddDto addDto);
	
	/**
	 * 删除盘点信息
	 * @param infoId
	 * @param operator
	 */
	public Result<Boolean> delCheckInfo(Long infoId,OperatorDto operator);
	
	/**
	 * 盘点信息列表
	 * @param param
	 * @return
	 */
	public Result<PageDto<AssetCheckInfoDto>> listInfo(AssetCheckInfoCondition  param);
	/**
	 * 县盘点总数
	 * @param countyOrgId
	 * @return
	 */
	public Result<CountyCheckCountDto> getCountyCheckCount(Long countyOrgId);
	/**
	 * 县跟踪盘点总数
	 * @param countyOrgId
	 * @return
	 */
	public Result<CountyFollowCheckCountDto> getCountyFollowCheckCount(Long countyOrgId);
	
	/**
	 * 村盘点任务列表
	 * @param param
	 * @return
	 */
    public Result<PageDto<AssetCheckTaskDto>> listTasks(QueryStationTaskCondition  param);
	
}
