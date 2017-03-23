package com.taobao.cun.auge.meeting.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.taobao.cun.auge.station.enums.OperatorTypeEnum;

public class MeetingAttempStatusEnum implements Serializable{

	private static final long serialVersionUID = 1L;
	public static final MeetingAttempStatusEnum INVITED = new MeetingAttempStatusEnum("INVITED", "邀请");
	public static final MeetingAttempStatusEnum ATTEMPED = new MeetingAttempStatusEnum("ATTEMPED", "参加");

	private static final Map<String, MeetingAttempStatusEnum> mappings = new HashMap<String, MeetingAttempStatusEnum>();

	static {
		mappings.put("INVITED", INVITED);
		mappings.put("ATTEMPED", ATTEMPED);
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

	public MeetingAttempStatusEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static MeetingAttempStatusEnum valueof(String code) {
		if (code == null)
			return null;
		return mappings.get(code);
	}

	public MeetingAttempStatusEnum() {

	}
}

