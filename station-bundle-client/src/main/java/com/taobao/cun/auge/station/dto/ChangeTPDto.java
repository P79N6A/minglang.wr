package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

import com.taobao.cun.auge.common.OperatorDto;

/**
 * Created by xiao on 16/8/25.
 */
public class ChangeTPDto extends OperatorDto implements Serializable {

    private static final long serialVersionUID = -36421095928748724L;
    
    /**
     * 实例id
     */
    private Long partnerInstanceId;

    private Long newParentStationId;

    public Long getPartnerInstanceId() {
		return partnerInstanceId;
	}

	public void setPartnerInstanceId(Long partnerInstanceId) {
		this.partnerInstanceId = partnerInstanceId;
	}

	public Long getNewParentStationId() {
        return newParentStationId;
    }

    public void setNewParentStationId(Long newParentStationId) {
        this.newParentStationId = newParentStationId;
    }
}
