package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

/**
 * 菜鸟同步新增村点dto
 * @author quanzhu.wangqz
 *
 */
public class SyncCainiaoStationDto extends BaseDto implements Serializable {

	private static final long serialVersionUID = -6630188995042897116L;
	
	private Long partnerInstanceId;

	public Long getPartnerInstanceId() {
		return partnerInstanceId;
	}

	public void setPartnerInstanceId(Long partnerInstanceId) {
		this.partnerInstanceId = partnerInstanceId;
	}

}
