package com.taobao.cun.auge.fence.dto.job;

import javax.validation.constraints.NotNull;

/**
 * 更新单个站点的围栏实例
 * 
 * @author chengyu.zhoucy
 *
 */
public class StationUpdateFenceInstanceJob extends FenceInstanceJob{
	@NotNull(message="站点ID不能为空")
	private Long stationId;
	
	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}
}
