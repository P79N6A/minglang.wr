package com.taobao.cun.auge.fence.service;

/**
 * 由定时任务，人工等触发构建围栏实例JOB
 * 
 * @author chengyu.zhoucy
 *
 */
public interface FenceInstanceJobExecuteService {
	/**
	 * 触发任务
	 */
	void execute();
}
