package com.taobao.cun.auge.fence.service;

import java.util.concurrent.ExecutorService;

import javax.annotation.Resource;

import com.taobao.cun.auge.common.concurrent.Executors;
import com.taobao.cun.auge.fence.job.FenceInstanceJobExecutorFacade;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

/**
 * 
 * 由定时任务，人工等触发构建围栏实例JOB
 * 
 * @author chengyu.zhoucy
 *
 */
@HSFProvider(serviceInterface = FenceInstanceJobExecuteService.class)
public class FenceInstanceJobExecuteServiceImpl implements FenceInstanceJobExecuteService {
	@Resource
	private FenceInstanceJobExecutorFacade fenceInstanceJobExecutorFacade;
	
	private ExecutorService executorService = null;
	
	public FenceInstanceJobExecuteServiceImpl() {
		executorService = Executors.newFixedThreadPool(1, "fence-instance-job-");
	}
	
	@Override
	public void execute() {
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				fenceInstanceJobExecutorFacade.startJob();
			}
		});
	}

}
