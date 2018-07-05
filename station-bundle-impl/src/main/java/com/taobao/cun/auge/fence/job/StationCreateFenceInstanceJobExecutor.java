package com.taobao.cun.auge.fence.job;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.fence.dto.job.FenceInstanceJob;
import com.taobao.cun.auge.fence.dto.job.StationCreateFenceInstanceJob;

/**
 * 执行从单个站点关联模板的任务
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class StationCreateFenceInstanceJobExecutor extends AbstractFenceInstanceJobExecutor<StationCreateFenceInstanceJob> {
	@Override
	protected int doExecute(StationCreateFenceInstanceJob fenceInstanceJob) {
		for(Long templateId : fenceInstanceJob.getTemplateIds()) {
			if(fenceInstanceJob.getCreateRule().equals(FenceInstanceJob.CREATE_RULE_OVERRIDE)){
				overrideFenceEntity(fenceInstanceJob.getStationId(), templateId);
			}else {
				buildFenceEntity(fenceInstanceJob.getStationId(), templateId);
			}
		}
		return fenceInstanceJob.getTemplateIds().size();
	}
}
