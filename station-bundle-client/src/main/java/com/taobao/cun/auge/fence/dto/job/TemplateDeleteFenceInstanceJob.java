package com.taobao.cun.auge.fence.dto.job;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 删除围栏模板后，删除关联的实例
 * @author chengyu.zhoucy
 *
 */
public class TemplateDeleteFenceInstanceJob extends FenceInstanceJob{
	@NotEmpty(message="模板ID不能为空")
	private List<Long> templateIds;

	public List<Long> getTemplateIds() {
		return templateIds;
	}

	public void setTemplateIds(List<Long> templateIds) {
		this.templateIds = templateIds;
	}
}
