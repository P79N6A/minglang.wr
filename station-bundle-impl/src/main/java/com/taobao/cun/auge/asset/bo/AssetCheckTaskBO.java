package com.taobao.cun.auge.asset.bo;

import java.util.List;

import com.taobao.cun.auge.asset.dto.AssetCheckTaskCondition;
import com.taobao.cun.auge.asset.dto.AssetCheckTaskDto;
import com.taobao.cun.auge.asset.dto.FinishTaskForCountyDto;
import com.taobao.cun.auge.asset.dto.QueryStationTaskCondition;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.dal.domain.AssetCheckTask;

/**
 * 盘点任务 基础服务
 * @author quanzhu.wangqz
 *
 */
public interface AssetCheckTaskBO {
	/**
	 * 初始化站点盘点任务
	 * @param taskType 任务类型
	 * @param taskCode  任务code
	 * @param taobaoUserId  淘宝userid
	 */
	public void initTaskForStation(String taskType,String taskCode,Long taobaoUserId);
	/**
	 * 完结站点盘点任务
	 * @param taobaoUserId
	 */
	public Boolean finishTaskForStation(Long taobaoUserId);
	
	/**
	 * 初始化县点盘点任务
	 * @param orgId
	 */
	public void initTaskForCounty(List<Long> orgId);
	
	/**
	 * 完结县点盘点任务
	 * @param FinishTaskForCountyDto
	 */
	public Boolean finishTaskForCounty(FinishTaskForCountyDto param);
	
	/**
	 * 盘点任务列表
	 * @param param
	 * @return
	 */
    public PageDto<AssetCheckTaskDto> listTasks(AssetCheckTaskCondition  param);
    
    /**
     * 县盘点任务
     * @param orgId
     * @param taskType
     * @return
     */
    public AssetCheckTask  getTaskForCounty(Long orgId,String taskType);
    
    /**
     * 村盘点任务
     * @param taobaoUserId
     * @return
     */
    public AssetCheckTask  getTaskForStation(String taobaoUserId);
    /**
     * 未完成盘点 村点数
     * @param countyOrgId
     * @return
     */
    public Integer getWaitCheckStationCount(Long countyOrgId);
	 /**
	  * 完成盘点 村点数
	  * @param countyOrgId
	  * @return
	  */
    public Integer getDoneCheckStationCount(Long countyOrgId);
    
    /**
     * 存点盘点状况列表
     * @param param
     * @return
     */
    public PageDto<AssetCheckTaskDto> listTasks(QueryStationTaskCondition param)
}
