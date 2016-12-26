package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PeixunPurchaseTypeEnum implements Serializable{

	private static final long serialVersionUID = 1L;
	public static final PeixunPurchaseTypeEnum APPLY_IN = new PeixunPurchaseTypeEnum("APPLY_IN", "启航班");
	public static final PeixunPurchaseTypeEnum UPGRADE = new PeixunPurchaseTypeEnum("UPGRADE", "橙长营");

	private static final Map<String, PeixunPurchaseTypeEnum> mappings = new HashMap<String, PeixunPurchaseTypeEnum>();

	static {
		mappings.put("APPLY_IN", APPLY_IN);
		mappings.put("UPGRADE", UPGRADE);
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
		if (!(obj instanceof OperatorTypeEnum))
			return false;
		OperatorTypeEnum objType = (OperatorTypeEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public PeixunPurchaseTypeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static PeixunPurchaseTypeEnum valueof(String code) {
		if (code == null)
			return null;
		return mappings.get(code);
	}

	public PeixunPurchaseTypeEnum() {

	}
}
