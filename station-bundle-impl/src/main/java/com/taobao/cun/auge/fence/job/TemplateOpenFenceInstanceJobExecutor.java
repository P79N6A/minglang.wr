package com.taobao.cun.auge.fence.job;

import org.springframework.stereotype.Component;

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
			instanceNum += updateFenceState(templateId, FenceConstants.ENABLE);
		}
		
		return instanceNum;
	}
}
