package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class StationForcedCloseReasonEnum implements Serializable {

	private static final long serialVersionUID = 7917664329231212153L;
	
	public static final StationForcedCloseReasonEnum ASSESS_FAIL = new StationForcedCloseReasonEnum("ASSESS_FAIL  ",
			"考核不达标");
	public static final StationForcedCloseReasonEnum CHEAT = new StationForcedCloseReasonEnum("CHEAT", "涉嫌欺诈");
	public static final StationForcedCloseReasonEnum OTHER = new StationForcedCloseReasonEnum("OTHER", "其它原因");
	
	public static final Map<String, StationForcedCloseReasonEnum> mappings = new HashMap<String, StationForcedCloseReasonEnum>();

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
		if (!(obj instanceof StationForcedCloseReasonEnum))
			return false;
		StationForcedCloseReasonEnum objType = (StationForcedCloseReasonEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public StationForcedCloseReasonEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static StationForcedCloseReasonEnum valueof(String code) {
		if (code == null)
			return null;
		return mappings.get(code);
	}

	@SuppressWarnings("unused")
	private StationForcedCloseReasonEnum() {

	}
}
