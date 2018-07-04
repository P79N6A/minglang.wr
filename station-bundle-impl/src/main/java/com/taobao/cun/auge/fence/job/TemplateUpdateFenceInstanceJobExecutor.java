package com.taobao.cun.auge.fence.job;

import java.util.List;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.FenceEntity;
import com.taobao.cun.auge.fence.dto.job.TemplateUpdateFenceInstanceJob;

/**
 * 执行模板修改的任务
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class TemplateUpdateFenceInstanceJobExecutor extends AbstractFenceInstanceJobExecutor<TemplateUpdateFenceInstanceJob> {

	@Override
	protected int doExecute(TemplateUpdateFenceInstanceJob fenceInstanceJob) {
		List<FenceEntity> fenceEntities = getFenceEntityList(fenceInstanceJob.getTemplateId());
		
		if(fenceEntities == null) {
			return 0;
		}
		//全部重新生成一遍
		
		for(FenceEntity fenceEntity : fenceEntities) {
			rebuildFenceEntity(fenceEntity);
		}
		
		return fenceEntities.size();
	}
}
