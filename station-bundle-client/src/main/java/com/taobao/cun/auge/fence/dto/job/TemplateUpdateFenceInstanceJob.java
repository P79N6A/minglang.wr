package com.taobao.cun.auge.fence.dto.job;

import javax.validation.constraints.NotNull;

/**
 * 更新了模板之后重新生成实例
 * 
 * @author chengyu.zhoucy
 *
 */
public class TemplateUpdateFenceInstanceJob extends FenceInstanceJob{
	@NotNull(message="模板ID不能为空")
	private Long templateId;

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

}
