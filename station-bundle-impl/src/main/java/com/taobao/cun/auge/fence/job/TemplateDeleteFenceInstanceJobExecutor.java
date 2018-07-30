package com.taobao.cun.auge.fence.job;

import java.util.List;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.FenceEntity;
import com.taobao.cun.auge.fence.dto.job.TemplateDeleteFenceInstanceJob;

/**
 * 删除围栏模板后，删除关联的实例
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
