package com.taobao.cun.auge.fence.job;

import java.util.List;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.FenceEntity;
import com.taobao.cun.auge.fence.dto.job.TemplateDeleteFenceInstanceJob;

/**
 * 执行从模板批量开启围栏的任务
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class TemplateDeleteFenceInstanceJobExecutor extends AbstractFenceInstanceJobExecutor<TemplateDeleteFenceInstanceJob> {

	@Override
	protected int doExecute(TemplateDeleteFenceInstanceJob fenceInstanceJob) {
		int instanceNum = 0;
		for(Long templateId : fenceInstanceJob.getTemplateIds()) {
			List<FenceEntity> fenceEntities = fenceEntityBO.getFenceEntityByTemplateId(templateId);
			for(FenceEntity fenceEntity : fenceEntities) {
				deleteFenceEntity(fenceEntity);
			}
			instanceNum += fenceEntities.size();
		}
		return instanceNum;
	}
}
