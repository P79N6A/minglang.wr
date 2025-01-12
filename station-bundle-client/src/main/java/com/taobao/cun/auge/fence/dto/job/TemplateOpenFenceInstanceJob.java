package com.taobao.cun.auge.fence.dto.job;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 通过模板开启实例（更新状态）
 * @author chengyu.zhoucy
 *
 */
public class TemplateOpenFenceInstanceJob extends FenceInstanceJob{
	@NotEmpty(message="模板ID不能为空")
	private List<Long> templateIds;

	public List<Long> getTemplateIds() {
		return templateIds;
	}

	public void setTemplateIds(List<Long> templateIds) {
		this.templateIds = templateIds;
	}
}
