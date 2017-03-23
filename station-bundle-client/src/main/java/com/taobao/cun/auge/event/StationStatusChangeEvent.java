package com.taobao.cun.auge.event;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.event.enums.StationStatusChangeEnum;

/**
 * 村点状态变化事件
 * 
 */
public class StationStatusChangeEvent extends OperatorDto{
	
	private static final long serialVersionUID = -3768121889532085713L;

	/**
	 * 服务站id
	 */
	private Long stationId;
	
	/**
	 * 服务站名称
	 */
	private String stationName;
	
	/**
	 * 状态转换枚举
	 */
	private StationStatusChangeEnum statusChangeEnum;

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public StationStatusChangeEnum getStatusChangeEnum() {
		return statusChangeEnum;
	}

	public void setStatusChangeEnum(StationStatusChangeEnum statusChangeEnum) {
		this.statusChangeEnum = statusChangeEnum;
	}
}
