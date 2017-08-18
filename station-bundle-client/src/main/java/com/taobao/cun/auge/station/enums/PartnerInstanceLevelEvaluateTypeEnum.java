package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PartnerInstanceLevelEvaluateTypeEnum implements Serializable {

	private static final long serialVersionUID = -3645487560971801482L;

	private static final Map<String, PartnerInstanceLevelEvaluateTypeEnum> mappings = new HashMap<String, PartnerInstanceLevelEvaluateTypeEnum>();

	private String code;
	private String desc;

	public static final PartnerInstanceLevelEvaluateTypeEnum SYSTEM = new PartnerInstanceLevelEvaluateTypeEnum("SYSTEM", "系统评定");
	public static final PartnerInstanceLevelEvaluateTypeEnum MANUAL = new PartnerInstanceLevelEvaluateTypeEnum("MANUAL", "人工评定");

	static {
		mappings.put("SYSTEM", SYSTEM);
		mappings.put("MANUAL", MANUAL);
	}

	public PartnerInstanceLevelEvaluateTypeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public PartnerInstanceLevelEvaluateTypeEnum() {

	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public static PartnerInstanceLevelEvaluateTypeEnum valueof(String code) {
		if (code == null) {
            return null;
        }
		return mappings.get(code);
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
		if (this == obj) {
            return true;
        }
		if (obj == null) {
            return false;
        }
		if (getClass() != obj.getClass()) {
            return false;
        }
		PartnerInstanceLevelEvaluateTypeEnum other = (PartnerInstanceLevelEvaluateTypeEnum) obj;
		if (code == null) {
			if (other.code != null) {
                return false;
            }
		} else if (!code.equals(other.code)) {
            return false;
        }
		return true;
	}
}
