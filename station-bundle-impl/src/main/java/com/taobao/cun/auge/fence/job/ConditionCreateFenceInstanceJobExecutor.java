package com.taobao.cun.auge.fence.job;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.fence.dto.job.ConditionCreateFenceInstanceJob;

/**
 * 执行条件筛选站点关联模板的任务
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class ConditionCreateFenceInstanceJobExecutor extends AbstractFenceInstanceJobExecutor<ConditionCreateFenceInstanceJob> {

	@Override
	protected int doExecute(ConditionCreateFenceInstanceJob fenceInstanceJob) {
		return 0;
	}
}
