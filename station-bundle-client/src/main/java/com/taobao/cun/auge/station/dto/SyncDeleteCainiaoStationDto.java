package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

import com.taobao.cun.auge.common.OperatorDto;

/**
 * 同步删除菜鸟物流站dto
 * @author quanzhu.wangqz
 *
 */
public class SyncDeleteCainiaoStationDto extends OperatorDto implements Serializable {

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
