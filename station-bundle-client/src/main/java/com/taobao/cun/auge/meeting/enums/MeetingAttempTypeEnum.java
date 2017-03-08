package com.taobao.cun.auge.meeting.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.taobao.cun.auge.station.enums.OperatorTypeEnum;


public class MeetingAttempTypeEnum implements Serializable{

	private static final long serialVersionUID = 1L;
	public static final MeetingAttempTypeEnum TP = new MeetingAttempTypeEnum("TP", "合伙人");

	private static final Map<String, MeetingAttempTypeEnum> mappings = new HashMap<String, MeetingAttempTypeEnum>();

	static {
		mappings.put("TP", TP);
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

	public MeetingAttempTypeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static MeetingAttempTypeEnum valueof(String code) {
		if (code == null)
			return null;
		return mappings.get(code);
	}

	public MeetingAttempTypeEnum() {

	}
}
