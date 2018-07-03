package com.taobao.cun.auge.fence.service;

import com.taobao.cun.auge.fence.dto.job.ConditionCreateFenceInstanceJobArg;
import com.taobao.cun.auge.fence.dto.job.ConditionDeleteFenceInstanceJobArg;
import com.taobao.cun.auge.fence.dto.job.StationCreateFenceInstanceJobArg;
import com.taobao.cun.auge.fence.dto.job.StationDeleteFenceInstanceJobArg;
import com.taobao.cun.auge.fence.dto.job.StationUpdateFenceInstanceJobArg;
import com.taobao.cun.auge.fence.dto.job.TemplateCloseFenceInstanceJobArg;
import com.taobao.cun.auge.fence.dto.job.TemplateOpenFenceInstanceJobArg;
import com.taobao.cun.auge.fence.dto.job.TemplateUpdateFenceInstanceJobArg;

/**
 * 创建围栏实例的任务
 * 
 * @author chengyu.zhoucy
 *
 */
public interface FenceInstanceJob {
	/**
	 * 通过模板来开启围栏的任务
	 * 
	 * @param templateOpenFenceInstanceJobArg
	 */
	void createJob(TemplateOpenFenceInstanceJobArg templateOpenFenceInstanceJobArg);
	
	/**
	 * 通过模板来关闭围栏的任务
	 * 
	 * @param templateCloseFenceInstanceJobArg
	 */
	void createJob(TemplateCloseFenceInstanceJobArg templateCloseFenceInstanceJobArg);
	
	/**
	 * 编辑模板后触发的任务
	 * @param templateUpdateFenceInstanceJobArg
	 */
	void createJob(TemplateUpdateFenceInstanceJobArg templateUpdateFenceInstanceJobArg);
	
	/**
	 * 通过条件查询创建围栏的任务
	 * 
	 * @param conditionCreateFenceInstanceJobArg
	 */
	void createJob(ConditionCreateFenceInstanceJobArg conditionCreateFenceInstanceJobArg);
	
	/**
	 * 通过条件查询删除围栏的任务
	 * 
	 * @param conditionDeleteFenceInstanceJobArg
	 */
	void createJob(ConditionDeleteFenceInstanceJobArg conditionDeleteFenceInstanceJobArg);
	
	/**
	 * 创建单个站点的围栏
	 * @param stationCreateFenceInstanceJobArg
	 */
	void createJob(StationCreateFenceInstanceJobArg stationCreateFenceInstanceJobArg);
	
	/**
	 * 删除单个站点的围栏
	 * @param stationDeleteFenceInstanceJobArg
	 */
	void createJob(StationDeleteFenceInstanceJobArg stationDeleteFenceInstanceJobArg);
	
	/**
	 * 更新单个站点的围栏
	 * @param stationUpdateFenceInstanceJobArg
	 */
	void createJob(StationUpdateFenceInstanceJobArg stationUpdateFenceInstanceJobArg);
}
