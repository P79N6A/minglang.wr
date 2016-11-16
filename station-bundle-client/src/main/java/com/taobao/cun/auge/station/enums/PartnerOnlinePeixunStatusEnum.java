package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PartnerOnlinePeixunStatusEnum implements Serializable{

	private static final long serialVersionUID = 1L;

	public static final PartnerOnlinePeixunStatusEnum WAIT_PEIXUN = new PartnerOnlinePeixunStatusEnum("WAIT_PEIXUN", "未培训");
	public static final PartnerOnlinePeixunStatusEnum WAIT_EXAM = new PartnerOnlinePeixunStatusEnum("WAIT_EXAM", "考试未通过");
	public static final PartnerOnlinePeixunStatusEnum DONE = new PartnerOnlinePeixunStatusEnum("DONE", "完成");

	private static final Map<String, PartnerOnlinePeixunStatusEnum> mappings = new HashMap<String, PartnerOnlinePeixunStatusEnum>();

	static {
		mappings.put("WAIT_PEIXUN", WAIT_PEIXUN);
		mappings.put("WAIT_EXAM", WAIT_EXAM);
		mappings.put("DONE", DONE);
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

	public PartnerOnlinePeixunStatusEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static PartnerOnlinePeixunStatusEnum valueof(String code) {
		if (code == null)
			return null;
		return mappings.get(code);
	}

	public PartnerOnlinePeixunStatusEnum() {

	}
	
}
