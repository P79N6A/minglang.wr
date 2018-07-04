package com.taobao.cun.auge.fence.job;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.fence.dto.job.StationDeleteFenceInstanceJob;

/**
 * 执行从单个站点跟模板取消关联的任务
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class StationDeleteFenceInstanceJobExecutor extends AbstractFenceInstanceJobExecutor<StationDeleteFenceInstanceJob> {

	@Override
	protected int doExecute(StationDeleteFenceInstanceJob fenceInstanceJob) {
		return 0;
	}
}
