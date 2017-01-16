package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 装修付款类型枚举
 * 
 * @author yi.shaoy
 *
 */
public class StationDecoratePaymentTypeEnum implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final StationDecoratePaymentTypeEnum SELF = new StationDecoratePaymentTypeEnum(
			"SELF", "自费");
	public static final StationDecoratePaymentTypeEnum GOV_PART = new StationDecoratePaymentTypeEnum(
			"GOV_PART", "政府部分出资");
	public static final StationDecoratePaymentTypeEnum GOV_ALL = new StationDecoratePaymentTypeEnum(
			"GOV_ALL", "政府全款");
	public static final StationDecoratePaymentTypeEnum NONE = new StationDecoratePaymentTypeEnum(
			"NONE", "免费");

	private static final Map<String, StationDecoratePaymentTypeEnum> mappings = new HashMap<String, StationDecoratePaymentTypeEnum>();

	static {
		mappings.put("SELF", SELF);
		mappings.put("GOV_PART", GOV_PART);
		mappings.put("GOV_ALL", GOV_ALL);
		mappings.put("NONE", NONE);
	}

	private String code;
	private String desc;

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof StationDecoratePaymentTypeEnum))
			return false;
		StationDecoratePaymentTypeEnum objType = (StationDecoratePaymentTypeEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public StationDecoratePaymentTypeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static StationDecoratePaymentTypeEnum valueof(String code) {
		if (code == null)
			return null;
		return mappings.get(code);
	}

	@SuppressWarnings("unused")
	private StationDecoratePaymentTypeEnum() {

	}
}
