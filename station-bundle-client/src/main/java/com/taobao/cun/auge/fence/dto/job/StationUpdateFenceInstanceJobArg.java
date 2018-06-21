package com.taobao.cun.auge.fence.dto.job;

import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * 更新单个站点的围栏实例，如果不指定模板ID，那么更新全部
 * 
 * @author chengyu.zhoucy
 *
 */
public class StationUpdateFenceInstanceJobArg {
	@NotNull(message="站点ID不能为空")
	private Long stationId;
	
	private List<Long> templateIds;
	
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
