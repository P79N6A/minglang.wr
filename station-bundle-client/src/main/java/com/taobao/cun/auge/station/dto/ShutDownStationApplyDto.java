package com.taobao.cun.auge.station.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.taobao.cun.auge.common.OperatorDto;

public class ShutDownStationApplyDto extends OperatorDto{

	private static final long serialVersionUID = 5198576840053716625L;
	
	//村点id
	@NotNull(message = "stationId not null")
	private Long stationId;
	
	//撤点原因
	private String reason;
	
	//申请人
    private String applierId;
    
    //申请时间
    private Date applyTime;

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getApplierId() {
		return applierId;
	}

	public void setApplierId(String applierId) {
		this.applierId = applierId;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}
}
