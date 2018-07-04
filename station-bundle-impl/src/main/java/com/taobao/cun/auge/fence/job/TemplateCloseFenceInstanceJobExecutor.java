package com.taobao.cun.auge.fence.job;

import java.util.List;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.FenceEntity;
import com.taobao.cun.auge.fence.dto.job.TemplateCloseFenceInstanceJob;

/**
 * 执行从模板批量关闭围栏的任务
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class TemplateCloseFenceInstanceJobExecutor extends AbstractFenceInstanceJobExecutor<TemplateCloseFenceInstanceJob> {

	@Override
	protected int doExecute(TemplateCloseFenceInstanceJob fenceInstanceJob) {
		int instanceNum = 0;
		for(Long templateId : fenceInstanceJob.getTemplateIds()) {
			fenceEntityBO.disableEntityListByTemplateId(templateId, "job");
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
