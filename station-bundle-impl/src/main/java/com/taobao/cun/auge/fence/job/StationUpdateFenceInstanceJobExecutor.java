package com.taobao.cun.auge.fence.job;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.fence.dto.job.StationUpdateFenceInstanceJob;

/**
 * 执行站点（行政地址、地标、领用地址）变化的任务
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class StationUpdateFenceInstanceJobExecutor extends AbstractFenceInstanceJobExecutor<StationUpdateFenceInstanceJob> {

	@Override
	protected int doExecute(StationUpdateFenceInstanceJob fenceInstanceJob) {
		
		return 0;
	}
}
