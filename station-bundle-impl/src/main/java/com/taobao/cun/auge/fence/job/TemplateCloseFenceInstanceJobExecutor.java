package com.taobao.cun.auge.fence.job;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.fence.constant.FenceConstants;
import com.taobao.cun.auge.fence.dto.job.TemplateCloseFenceInstanceJob;

/**
 * 执行从模板批量关闭围栏的任务
 * 
 * 将状态变更为关闭状态，并且通知到菜鸟
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
			instanceNum += updateFenceStateByTemplate(templateId, FenceConstants.DISABLE);
		}
		
		return instanceNum;
	}
}
