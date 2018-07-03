package com.taobao.cun.auge.fence.dto.job;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 通过条件创建围栏实例
 * 
 * @author chengyu.zhoucy
 *
 */
public class ConditionCreateFenceInstanceJob extends FenceInstanceJob{
	/**新建*/
	public static String CREATE_RULE_NEW = "NEW";
	/**覆盖*/
	public static String CREATE_RULE_OVERRIDE = "OVERRIDE";
	
	/**
	 * 条件JSON字符串
	 */
	@NotEmpty(message="查询条件不能为空")
	private String condition;
	@NotEmpty(message="覆盖规则不能为空")
	private String createRule;
	@NotEmpty(message="关联的模板ID不能为空")
	private List<Long> templateIds;
	
	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getCreateRule() {
		return createRule;
	}

	public void setCreateRule(String createRule) {
		this.createRule = createRule;
	}

	public List<Long> getTemplateIds() {
		return templateIds;
	}

	public void setTemplateIds(List<Long> templateIds) {
		this.templateIds = templateIds;
	}
}
