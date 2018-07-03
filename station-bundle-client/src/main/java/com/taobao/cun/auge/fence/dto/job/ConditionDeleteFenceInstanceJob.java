package com.taobao.cun.auge.fence.dto.job;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 通过条件删除围栏实例
 * 
 * @author chengyu.zhoucy
 *
 */
public class ConditionDeleteFenceInstanceJob extends FenceInstanceJob{
	/**
	 * 条件JSON字符串
	 */
	@NotEmpty(message="查询条件不能为空")
	private String condition;
	@NotEmpty(message="关联的模板ID不能为空")
	private List<Long> templateIds;
	
	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public List<Long> getTemplateIds() {
		return templateIds;
	}

	public void setTemplateIds(List<Long> templateIds) {
		this.templateIds = templateIds;
	}
}
