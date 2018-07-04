package com.taobao.cun.auge.fence.job;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.fence.dto.job.StationCreateFenceInstanceJob;

/**
 * 执行从单个站点关联模板的任务
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class StationCreateFenceInstanceJobExecutor extends AbstractFenceInstanceJobExecutor<StationCreateFenceInstanceJob> {

	@Override
	protected int doExecute(StationCreateFenceInstanceJob fenceInstanceJob) {
		return 0;
	}
}
