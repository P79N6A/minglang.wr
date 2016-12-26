package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CloseStationApplyCloseReasonEnum implements Serializable {
	
	private static final long serialVersionUID = 3969120375620371099L;
	
	public static final CloseStationApplyCloseReasonEnum ASSESS_FAIL = new CloseStationApplyCloseReasonEnum("ASSESS_FAIL",
			"考核不达标");
	public static final CloseStationApplyCloseReasonEnum CHEAT = new CloseStationApplyCloseReasonEnum("CHEAT", "涉嫌欺诈");
	public static final CloseStationApplyCloseReasonEnum OTHER = new CloseStationApplyCloseReasonEnum("OTHER", "其它原因");
	
	public static final Map<String, CloseStationApplyCloseReasonEnum> mappings = new HashMap<String, CloseStationApplyCloseReasonEnum>();

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
		if (!(obj instanceof CloseStationApplyCloseReasonEnum))
			return false;
		CloseStationApplyCloseReasonEnum objType = (CloseStationApplyCloseReasonEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public CloseStationApplyCloseReasonEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static CloseStationApplyCloseReasonEnum valueof(String code) {
		if (code == null)
			return null;
		return mappings.get(code);
	}

	@SuppressWarnings("unused")
	private CloseStationApplyCloseReasonEnum() {

	}
}
