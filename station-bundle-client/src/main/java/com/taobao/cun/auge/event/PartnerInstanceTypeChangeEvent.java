package com.taobao.cun.auge.event;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.event.enums.PartnerInstanceTypeChangeEnum;

public class PartnerInstanceTypeChangeEvent extends OperatorDto {

	private static final long serialVersionUID = -7749930284453050824L;

	/**
	 * 实例id,内部使用，外部系统不要使用
	 */
	private Long partnerInstanceId;

	/**
	 * 村点id
	 */
	private Long stationId;

	/**
	 * 当前合伙人淘宝userId
	 */
	private Long taobaoUserId;

	/**
	 * 淘帮手关联的合伙人淘宝userId，仅限淘帮手使用
	 */
	private Long parentTaobaoUserId;

	/**
	 * 类型变更类型
	 */
	private PartnerInstanceTypeChangeEnum typeChangeEnum;

	public Long getPartnerInstanceId() {
		return partnerInstanceId;
	}

	public void setPartnerInstanceId(Long partnerInstanceId) {
		this.partnerInstanceId = partnerInstanceId;
	}

	public Long getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}

	public Long getParentTaobaoUserId() {
		return parentTaobaoUserId;
	}

	public void setParentTaobaoUserId(Long parentTaobaoUserId) {
		this.parentTaobaoUserId = parentTaobaoUserId;
	}

	public PartnerInstanceTypeChangeEnum getTypeChangeEnum() {
		return typeChangeEnum;
	}

	public void setTypeChangeEnum(PartnerInstanceTypeChangeEnum typeChangeEnum) {
		this.typeChangeEnum = typeChangeEnum;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}
}
