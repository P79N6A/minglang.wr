package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

/**
 * 同步修改菜鸟物流站dto
 * @author quanzhu.wangqz
 *
 */
public class SyncModifyCainiaoStationDto extends BaseDto implements Serializable {

	private static final long serialVersionUID = -6630188995042897116L;
	
	/**
	 * 合伙人实例id
	 */
	private Long partnerInstanceId;
	
	/**
	 * 是否同步修改物流站，是true,修改物流站和关系 
	 * 如果是false,修改关系
	 */
	private boolean isModifyStation;

	public Long getPartnerInstanceId() {
		return partnerInstanceId;
	}

	public void setPartnerInstanceId(Long partnerInstanceId) {
		this.partnerInstanceId = partnerInstanceId;
	}

	public boolean isModifyStation() {
		return isModifyStation;
	}

	public void setModifyStation(boolean isModifyStation) {
		this.isModifyStation = isModifyStation;
	}



}
