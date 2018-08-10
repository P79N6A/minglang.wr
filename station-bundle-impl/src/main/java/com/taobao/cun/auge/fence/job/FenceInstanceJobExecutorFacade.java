package com.taobao.cun.auge.fence.job;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.fence.bo.FenceInstanceJobBo;
import com.taobao.cun.auge.fence.dto.job.FenceInstanceJob;

@Component
public class FenceInstanceJobExecutorFacade {
	@Resource
	private FenceInstanceJobBo fenceInstanceJobBo;
	@Resource
	private FenceInstanceJobExecutorFactory fenceInstanceJobExecutorFactory;
	
	public void startJob() {
		List<FenceInstanceJob> fenceInstanceJobs = fenceInstanceJobBo.getNewFenceInstanceJobs();
		for(FenceInstanceJob fenceInstanceJob : fenceInstanceJobs) {
			FenceInstanceJobExecutor<FenceInstanceJob> executor = fenceInstanceJobExecutorFactory.getFenceInstanceJobExecutor(fenceInstanceJob);
			if(executor != null) {
				executor.execute(fenceInstanceJob);
			}
		}
	}
}
