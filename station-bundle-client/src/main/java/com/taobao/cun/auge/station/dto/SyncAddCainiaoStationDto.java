package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

/**
 * 同步新增菜鸟物流站dto
 * @author quanzhu.wangqz
 *
 */
public class SyncAddCainiaoStationDto extends BaseDto implements Serializable {

	private static final long serialVersionUID = -6630188995042897116L;
	
	/**
	 * 合伙人实例id
	 */
	private Long partnerInstanceId;
	
	/**
	 * 是否同步新增物流站，true, 增加物流站，关系，
	 * false,只增加关系
	 */
	private boolean isAddStation;

	public Long getPartnerInstanceId() {
		return partnerInstanceId;
	}

	public void setPartnerInstanceId(Long partnerInstanceId) {
		this.partnerInstanceId = partnerInstanceId;
	}

	public boolean isAddStation() {
		return isAddStation;
	}

	public void setAddStation(boolean isAddStation) {
		this.isAddStation = isAddStation;
	}
}