package com.taobao.cun.auge.fence.dto.job;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 单个站点创建围栏实例
 * 
 * @author chengyu.zhoucy
 *
 */
public class StationCreateFenceInstanceJob extends FenceInstanceJob{
	@NotNull(message="站点ID不能为空")
	private Long stationId;
	@NotEmpty(message="关联的模板ID不能为空")
	private List<Long> templateIds;
	@NotEmpty(message="覆盖规则不能为空")
	private String createRule;
	
	public String getCreateRule() {
		return createRule;
	}

	public void setCreateRule(String createRule) {
		this.createRule = createRule;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public List<Long> getTemplateIds() {
		return templateIds;
	}

	public void setTemplateIds(List<Long> templateIds) {
		this.templateIds = templateIds;
	}
}
