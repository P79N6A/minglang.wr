package com.taobao.cun.auge.fence.service;

import com.taobao.cun.auge.fence.dto.job.*;

/**
 * 创建围栏实例的任务
 * 
 * @author chengyu.zhoucy
 *
 */
public interface FenceInstanceJobService {
	/**
	 * 通过模板来开启围栏的任务
	 * 
	 * @param templateOpenFenceInstanceJob
	 */
	void createJob(TemplateOpenFenceInstanceJob templateOpenFenceInstanceJob);
	
	/**
	 * 通过模板来关闭围栏的任务
	 * 
	 * @param templateCloseFenceInstanceJob
	 */
	void createJob(TemplateCloseFenceInstanceJob templateCloseFenceInstanceJob);
	
	/**
	 * 删除围栏模板后，删除关联的实例
	 * @param templateDeleteFenceInstanceJob
	 */
	void createJob(TemplateDeleteFenceInstanceJob templateDeleteFenceInstanceJob);
	
	/**
	 * 编辑模板后触发的任务
	 * @param templateUpdateFenceInstanceJob
	 */
	void createJob(TemplateUpdateFenceInstanceJob templateUpdateFenceInstanceJob);
	
	/**
	 * 通过条件查询创建围栏的任务
	 * 
	 * @param conditionCreateFenceInstanceJob
	 */
	void createJob(ConditionCreateFenceInstanceJob conditionCreateFenceInstanceJob);
	
	/**
	 * 通过条件查询删除围栏的任务
	 * 
	 * @param conditionDeleteFenceInstanceJob
	 */
	void createJob(ConditionDeleteFenceInstanceJob conditionDeleteFenceInstanceJob);
	
	/**
	 * 创建单个站点的围栏
	 * @param stationCreateFenceInstanceJob
	 */
	void createJob(StationCreateFenceInstanceJob stationCreateFenceInstanceJob);
	
	/**
	 * 删除单个站点的围栏
	 * @param stationDeleteFenceInstanceJob
	 */
	void createJob(StationDeleteFenceInstanceJob stationDeleteFenceInstanceJob);
	
	/**
	 * 更新单个站点的围栏
	 * @param stationUpdateFenceInstanceJob
	 */
	void createJob(StationUpdateFenceInstanceJob stationUpdateFenceInstanceJob);
	
	/**
	 * 删除退出的站点的围栏实例
	 * @param stationQuitFenceInstanceJob
	 */
	void createJob(BatchStationQuitFenceInstanceJob stationQuitFenceInstanceJob);
	
	/**
	 * 初始化站点的围栏实例
	 * @param stationInitFenceInstanceJob
	 */
	void createJob(BatchStationInitFenceInstanceJob stationInitFenceInstanceJob);

	/**
	 * 菜鸟站点（县仓、菜鸟站点）停业，要关闭相关围栏
	 * @param cainiaoStationCloseFenceInstanceJob
	 */
	void createJob(CainiaoStationCloseFenceInstanceJob cainiaoStationCloseFenceInstanceJob);
}
