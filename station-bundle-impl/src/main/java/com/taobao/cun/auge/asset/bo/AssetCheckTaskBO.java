package com.taobao.cun.auge.asset.bo;

import java.util.List;

import com.taobao.cun.auge.asset.dto.AssetCheckTaskCondition;
import com.taobao.cun.auge.asset.dto.AssetCheckTaskDto;
import com.taobao.cun.auge.common.PageDto;

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
	public void finishTaskForStation(Long taobaoUserId);
	
	/**
	 * 初始化县点盘点任务
	 * @param orgId
	 */
	public void initTaskForCounty(List<Long> orgId);
	
	/**
	 * 完结县点盘点任务
	 * @param orgId
	 * @param workNo
	 */
	public void finishTaskForCounty(Long orgId,String workNo);
	
	
    public PageDto<AssetCheckTaskDto> listTasks(AssetCheckTaskCondition  param);
	
	
}
