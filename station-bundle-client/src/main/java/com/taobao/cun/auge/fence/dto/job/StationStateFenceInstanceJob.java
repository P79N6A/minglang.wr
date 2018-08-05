package com.taobao.cun.auge.fence.dto.job;

import javax.validation.constraints.NotNull;

/**
 * 站点状态变化时的任务
 * 
 * @author chengyu.zhoucy
 *
 */
public abstract class StationStateFenceInstanceJob extends FenceInstanceJob{
	@NotNull(message="站点ID不能为空")
	private Long stationId;
	

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}
}
