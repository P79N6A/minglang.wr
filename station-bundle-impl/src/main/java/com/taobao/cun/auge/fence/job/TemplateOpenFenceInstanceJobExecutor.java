package com.taobao.cun.auge.fence.job;

import java.util.List;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.FenceEntity;
import com.taobao.cun.auge.fence.constant.FenceConstants;
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
			instanceNum += updateFenceState(templateId);
		}
		
		return instanceNum;
	}

	private int updateFenceState(Long templateId) {
		fenceEntityBO.enableEntityListByTemplateId(templateId, "job");
		List<FenceEntity> fenceEntities = getFenceEntityList(templateId);
		if(fenceEntities != null) {
			for(FenceEntity fenceEntity : fenceEntities) {
				//更新菜鸟的状态
				fenceEntity.setState(FenceConstants.ENABLE);
				updateCainiaoFence(fenceEntity);
			}
			return fenceEntities.size();
		}
		return 0;
	}

}
