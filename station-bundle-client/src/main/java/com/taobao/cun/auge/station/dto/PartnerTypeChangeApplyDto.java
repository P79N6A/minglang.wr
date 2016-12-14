package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.util.Map;

import com.taobao.cun.auge.event.enums.PartnerInstanceTypeChangeEnum;

public class PartnerTypeChangeApplyDto implements Serializable {

	private static final long serialVersionUID = -395193218316322678L;

	private Long instanceId;

	private Long nextInstanceId;

	/**
	 * 其他特性，用于扩展服务站属性
	 */
	private Map<String, String> feature;

	private PartnerInstanceTypeChangeEnum typeChangeEnum;

	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}

	public Long getNextInstanceId() {
		return nextInstanceId;
	}

	public void setNextInstanceId(Long nextInstanceId) {
		this.nextInstanceId = nextInstanceId;
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
