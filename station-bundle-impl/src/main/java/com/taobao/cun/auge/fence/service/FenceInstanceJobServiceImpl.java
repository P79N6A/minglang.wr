package com.taobao.cun.auge.fence.service;

import java.util.List;

import javax.annotation.Resource;

import com.taobao.cun.auge.fence.bo.FenceInstanceJobBo;
import com.taobao.cun.auge.fence.dto.job.*;
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
		List<FenceInstanceJob> fenceInstanceJobs = fenceInstanceJobBo.getNewFenceInstanceJobs();
		for(FenceInstanceJob fenceInstanceJob : fenceInstanceJobs) {
			if(fenceInstanceJob instanceof TemplateUpdateFenceInstanceJob) {
				TemplateUpdateFenceInstanceJob job = (TemplateUpdateFenceInstanceJob) fenceInstanceJob;
				if(job.getTemplateId().equals(templateUpdateFenceInstanceJob.getTemplateId())) {
					return;
				}
			}
		}
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
		List<FenceInstanceJob> fenceInstanceJobs = fenceInstanceJobBo.getNewFenceInstanceJobs();
		for(FenceInstanceJob fenceInstanceJob : fenceInstanceJobs) {
			if(fenceInstanceJob instanceof StationUpdateFenceInstanceJob) {
				StationUpdateFenceInstanceJob job = (StationUpdateFenceInstanceJob) fenceInstanceJob;
				if(job.getStationId().equals(stationUpdateFenceInstanceJob.getStationId())) {
					return;
				}
			}
		}
		fenceInstanceJobBo.insertJob(stationUpdateFenceInstanceJob);
	}

	@Override
	public void createJob(BatchStationQuitFenceInstanceJob stationQuitFenceInstanceJob) {
		fenceInstanceJobBo.insertJob(stationQuitFenceInstanceJob);
	}

	@Override
	public void createJob(CainiaoStationCloseFenceInstanceJob cainiaoStationCloseFenceInstanceJob) {
		fenceInstanceJobBo.insertJob(cainiaoStationCloseFenceInstanceJob);
	}

	@Override
	public void createJob(BatchStationInitFenceInstanceJob stationInitFenceInstanceJob) {
		fenceInstanceJobBo.insertJob(stationInitFenceInstanceJob);
	}

	@Override
	public void createJob(TemplateDeleteFenceInstanceJob templateDeleteFenceInstanceJob) {
		fenceInstanceJobBo.insertJob(templateDeleteFenceInstanceJob);
	}
}
