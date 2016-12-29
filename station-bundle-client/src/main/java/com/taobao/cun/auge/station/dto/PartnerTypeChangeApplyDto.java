package com.taobao.cun.auge.station.dto;

import java.util.Map;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.event.enums.PartnerInstanceTypeChangeEnum;

public class PartnerTypeChangeApplyDto extends OperatorDto {

	private static final long serialVersionUID = -395193218316322678L;

	private Long partnerInstanceId;

	private Long nextPartnerInstanceId;

	/**
	 * 其他特性，用于扩展服务站属性
	 */
	private Map<String, String> feature;

	private PartnerInstanceTypeChangeEnum typeChangeEnum;

	public Long getPartnerInstanceId() {
		return partnerInstanceId;
	}

	public void setPartnerInstanceId(Long partnerInstanceId) {
		this.partnerInstanceId = partnerInstanceId;
	}

	public Long getNextPartnerInstanceId() {
		return nextPartnerInstanceId;
	}

	public void setNextPartnerInstanceId(Long nextPartnerInstanceId) {
		this.nextPartnerInstanceId = nextPartnerInstanceId;
	}

	public Map<String, String> getFeature() {
		return feature;
	}

	public void setFeature(Map<String, String> feature) {
		this.feature = feature;
	}

	public PartnerInstanceTypeChangeEnum getTypeChangeEnum() {
		return typeChangeEnum;
	}

	public void setTypeChangeEnum(PartnerInstanceTypeChangeEnum typeChangeEnum) {
		this.typeChangeEnum = typeChangeEnum;
	}

}
