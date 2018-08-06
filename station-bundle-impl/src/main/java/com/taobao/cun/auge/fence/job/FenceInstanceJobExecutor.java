package com.taobao.cun.auge.fence.job;

import com.taobao.cun.auge.fence.dto.job.FenceInstanceJob;

/**
 * Job执行器
 * 
 * @author chengyu.zhoucy
 *
 * @param <F>
 */
public interface FenceInstanceJobExecutor<F extends FenceInstanceJob> {
	/**
	 * 执行任务
	 * @param fenceInstanceJob
	 */
	void execute(F fenceInstanceJob);
}
