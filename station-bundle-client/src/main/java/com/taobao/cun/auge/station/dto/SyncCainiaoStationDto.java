package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

/**
 * 同步菜鸟物流站dto
 * @author quanzhu.wangqz
 *
 */
public class SyncCainiaoStationDto extends BaseDto implements Serializable {

	private static final long serialVersionUID = -6630188995042897116L;
	
	/**
	 * 合伙人实例id
	 */
	private Long partnerInstanceId;
	
	/**
	 * 是否同步删除物流站，默认是true, 如果是false,菜鸟需要提供修改接口，解决
	 */
	private boolean isDeleteCainiaoStation = true;

	public Long getPartnerInstanceId() {
		return partnerInstanceId;
	}

	public void setPartnerInstanceId(Long partnerInstanceId) {
		this.partnerInstanceId = partnerInstanceId;
	}

	public boolean isDeleteCainiaoStation() {
		return isDeleteCainiaoStation;
	}

	public void setDeleteCainiaoStation(boolean isDeleteCainiaoStation) {
		this.isDeleteCainiaoStation = isDeleteCainiaoStation;
	}

}
