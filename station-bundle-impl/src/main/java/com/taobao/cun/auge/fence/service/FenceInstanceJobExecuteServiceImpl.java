package com.taobao.cun.auge.fence.service;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Async;

import com.taobao.cun.auge.fence.job.FenceInstanceJobExecutorFacade;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@HSFProvider(serviceInterface = FenceInstanceJobExecuteService.class)
public class FenceInstanceJobExecuteServiceImpl implements FenceInstanceJobExecuteService {
	@Resource
	private FenceInstanceJobExecutorFacade fenceInstanceJobExecutorFacade;
	
	@Override
	@Async
	public void execute() {
		fenceInstanceJobExecutorFacade.startJob();
	}

}
