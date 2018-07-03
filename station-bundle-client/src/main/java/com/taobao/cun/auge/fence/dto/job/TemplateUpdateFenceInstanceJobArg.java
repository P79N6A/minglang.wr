package com.taobao.cun.auge.fence.dto.job;

import javax.validation.constraints.NotNull;

public class TemplateUpdateFenceInstanceJobArg {
	@NotNull(message="模板ID不能为空")
	private Long templateId;

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

}
