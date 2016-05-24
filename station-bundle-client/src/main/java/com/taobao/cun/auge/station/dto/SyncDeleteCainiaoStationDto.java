package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

/**
 * 同步删除菜鸟物流站dto
 * @author quanzhu.wangqz
 *
 */
public class SyncDeleteCainiaoStationDto extends BaseDto implements Serializable {

	private static final long serialVersionUID = -6630188995042897116L;
	
	/**
	 * 合伙人实例id
	 */
	private Long partnerInstanceId;
	

	public Long getPartnerInstanceId() {
		return partnerInstanceId;
	}

	public void setPartnerInstanceId(Long partnerInstanceId) {
		this.partnerInstanceId = partnerInstanceId;
	}
}
