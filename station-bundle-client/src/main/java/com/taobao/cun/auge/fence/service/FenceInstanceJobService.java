package com.taobao.cun.auge.fence.service;

import com.taobao.cun.auge.fence.dto.job.BatchStationInitFenceInstanceJob;
import com.taobao.cun.auge.fence.dto.job.BatchStationQuitFenceInstanceJob;
import com.taobao.cun.auge.fence.dto.job.ConditionCreateFenceInstanceJob;
import com.taobao.cun.auge.fence.dto.job.ConditionDeleteFenceInstanceJob;
import com.taobao.cun.auge.fence.dto.job.StationCreateFenceInstanceJob;
import com.taobao.cun.auge.fence.dto.job.StationDeleteFenceInstanceJob;
import com.taobao.cun.auge.fence.dto.job.StationStateClosedFenceInstanceJob;
import com.taobao.cun.auge.fence.dto.job.StationStateReopenFenceInstanceJob;
import com.taobao.cun.auge.fence.dto.job.StationUpdateFenceInstanceJob;
import com.taobao.cun.auge.fence.dto.job.TemplateCloseFenceInstanceJob;
import com.taobao.cun.auge.fence.dto.job.TemplateOpenFenceInstanceJob;
import com.taobao.cun.auge.fence.dto.job.TemplateUpdateFenceInstanceJob;

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
	 * 站点停业
	 * @param stationStateClosedFenceInstanceJob
	 */
	void createJob(StationStateClosedFenceInstanceJob stationStateClosedFenceInstanceJob);
	/**
	 * 站点重新开业
	 * @param stationStateReopenFenceInstanceJob
	 */
	void createJob(StationStateReopenFenceInstanceJob stationStateReopenFenceInstanceJob);
}
