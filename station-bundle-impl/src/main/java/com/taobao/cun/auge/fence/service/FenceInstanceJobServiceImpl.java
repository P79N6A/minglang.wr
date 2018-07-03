package com.taobao.cun.auge.fence.service;

import javax.annotation.Resource;

import com.taobao.cun.auge.fence.bo.FenceInstanceJobBo;
import com.taobao.cun.auge.fence.dto.job.ConditionCreateFenceInstanceJob;
import com.taobao.cun.auge.fence.dto.job.ConditionDeleteFenceInstanceJob;
import com.taobao.cun.auge.fence.dto.job.StationCreateFenceInstanceJob;
import com.taobao.cun.auge.fence.dto.job.StationDeleteFenceInstanceJob;
import com.taobao.cun.auge.fence.dto.job.StationUpdateFenceInstanceJob;
import com.taobao.cun.auge.fence.dto.job.TemplateCloseFenceInstanceJob;
import com.taobao.cun.auge.fence.dto.job.TemplateOpenFenceInstanceJob;
import com.taobao.cun.auge.fence.dto.job.TemplateUpdateFenceInstanceJob;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@HSFProvider(serviceInterface = FenceInstanceJobService.class)
public class FenceInstanceJobServiceImpl implements FenceInstanceJobService {
	@Resource
	private FenceInstanceJobBo fenceInstanceJobBo;
	
	@Override
	public void createJob(TemplateOpenFenceInstanceJob templateOpenFenceInstanceJob) {
		fenceInstanceJobBo.insertJob(templateOpenFenceInstanceJob);
	}

	@Override
	public void createJob(TemplateCloseFenceInstanceJob templateCloseFenceInstanceJob) {
		fenceInstanceJobBo.insertJob(templateCloseFenceInstanceJob);
	}

	@Override
	public void createJob(TemplateUpdateFenceInstanceJob templateUpdateFenceInstanceJob) {
		fenceInstanceJobBo.insertJob(templateUpdateFenceInstanceJob);
	}

	@Override
	public void createJob(ConditionCreateFenceInstanceJob conditionCreateFenceInstanceJob) {
		fenceInstanceJobBo.insertJob(conditionCreateFenceInstanceJob);
	}

	@Override
	public void createJob(ConditionDeleteFenceInstanceJob conditionDeleteFenceInstanceJob) {
		fenceInstanceJobBo.insertJob(conditionDeleteFenceInstanceJob);
	}

	@Override
	public void createJob(StationCreateFenceInstanceJob stationCreateFenceInstanceJob) {
		fenceInstanceJobBo.insertJob(stationCreateFenceInstanceJob);
	}

	@Override
	public void createJob(StationDeleteFenceInstanceJob stationDeleteFenceInstanceJob) {
		fenceInstanceJobBo.insertJob(stationDeleteFenceInstanceJob);
	}

	@Override
	public void createJob(StationUpdateFenceInstanceJob stationUpdateFenceInstanceJob) {
		fenceInstanceJobBo.insertJob(stationUpdateFenceInstanceJob);
	}

}
