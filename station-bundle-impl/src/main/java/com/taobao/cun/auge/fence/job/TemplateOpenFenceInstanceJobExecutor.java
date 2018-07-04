package com.taobao.cun.auge.fence.job;

import java.util.List;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.FenceEntity;
import com.taobao.cun.auge.fence.dto.job.TemplateOpenFenceInstanceJob;

/**
 * 执行从模板批量开启围栏的任务
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class TemplateOpenFenceInstanceJobExecutor extends AbstractFenceInstanceJobExecutor<TemplateOpenFenceInstanceJob> {

	@Override
	protected int doExecute(TemplateOpenFenceInstanceJob fenceInstanceJob) {
		int instanceNum = 0;
		for(Long templateId : fenceInstanceJob.getTemplateIds()) {
			fenceEntityBO.enableEntityListByTemplateId(templateId, "job");
			List<FenceEntity> fenceEntities = getFenceEntityList(templateId);
			if(fenceEntities != null) {
				instanceNum += fenceEntities.size();
				for(FenceEntity fenceEntity : fenceEntities) {
					//更新菜鸟的状态
				}
			}
		}
		
		return instanceNum;
	}

}
