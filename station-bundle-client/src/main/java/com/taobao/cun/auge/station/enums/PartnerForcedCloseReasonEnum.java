package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PartnerForcedCloseReasonEnum implements Serializable {
	
	private static final long serialVersionUID = 3969120375620371099L;
	
	public static final PartnerForcedCloseReasonEnum ASSESS_FAIL = new PartnerForcedCloseReasonEnum("ASSESS_FAIL  ",
			"考核不达标");
	public static final PartnerForcedCloseReasonEnum CHEAT = new PartnerForcedCloseReasonEnum("CHEAT", "涉嫌欺诈");
	public static final PartnerForcedCloseReasonEnum OTHER = new PartnerForcedCloseReasonEnum("OTHER", "其它原因");
	
	public static final Map<String, PartnerForcedCloseReasonEnum> mappings = new HashMap<String, PartnerForcedCloseReasonEnum>();

	static {
		mappings.put("ASSESS_FAIL", ASSESS_FAIL);
		mappings.put("CHEAT", CHEAT);
		mappings.put("OTHER", OTHER);
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
		if (!(obj instanceof PartnerForcedCloseReasonEnum))
			return false;
		PartnerForcedCloseReasonEnum objType = (PartnerForcedCloseReasonEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public PartnerForcedCloseReasonEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static PartnerForcedCloseReasonEnum valueof(String code) {
		if (code == null)
			return null;
		return mappings.get(code);
	}

	@SuppressWarnings("unused")
	private PartnerForcedCloseReasonEnum() {

	}
}
