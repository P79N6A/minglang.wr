package com.taobao.cun.auge.fence.job;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.fence.dto.job.ConditionDeleteFenceInstanceJob;

/**
 * 执行条件筛选站点批量取消模板关联的任务
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class ConditionDeleteFenceInstanceJobExecutor extends AbstractFenceInstanceJobExecutor<ConditionDeleteFenceInstanceJob> {

	@Override
	protected int doExecute(ConditionDeleteFenceInstanceJob fenceInstanceJob) {
		return 0;
	}
}
