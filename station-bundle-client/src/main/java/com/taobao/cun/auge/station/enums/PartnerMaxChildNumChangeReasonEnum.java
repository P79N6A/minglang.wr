package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 最大子成员变更原因
 */
public class PartnerMaxChildNumChangeReasonEnum implements Serializable {

	private static final long serialVersionUID = 2804785929923849859L;

	private static final Map<String, PartnerMaxChildNumChangeReasonEnum> mappings = new HashMap<String, PartnerMaxChildNumChangeReasonEnum>();

	private String code;
	private String desc;

	public static final PartnerMaxChildNumChangeReasonEnum INIT = new PartnerMaxChildNumChangeReasonEnum("INIT", "初始化");

	public static final PartnerMaxChildNumChangeReasonEnum EDIT = new PartnerMaxChildNumChangeReasonEnum("EDIT",
			"信息变更");

	public static final PartnerMaxChildNumChangeReasonEnum TPA_PERFORMANCE_REWARD = new PartnerMaxChildNumChangeReasonEnum(
			"TPA_PERFORMANCE_REWARD", "淘帮手绩效奖励");

	public static final PartnerMaxChildNumChangeReasonEnum TPA_UPGRADE_REWARD = new PartnerMaxChildNumChangeReasonEnum(
			"TPA_UPGRADE_REWARD", "淘帮手升级为合伙人");

	public static final PartnerMaxChildNumChangeReasonEnum TP_DEGREE_2_TPA = new PartnerMaxChildNumChangeReasonEnum(
			"TP_DEGREE_2_TPA", "合伙人降级为淘帮手");

	static {
		mappings.put("INIT", INIT);
		mappings.put("EDIT", EDIT);
		mappings.put("TPA_PERFORMANCE_REWARD", TPA_PERFORMANCE_REWARD);
		mappings.put("TPA_UPGRADE_REWARD", TPA_UPGRADE_REWARD);
		mappings.put("TP_DEGREE_2_TPA", TP_DEGREE_2_TPA);
	}

	public PartnerMaxChildNumChangeReasonEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public PartnerMaxChildNumChangeReasonEnum() {

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

	public static PartnerMaxChildNumChangeReasonEnum valueof(String code) {
		if (code == null)
			return null;
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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PartnerMaxChildNumChangeReasonEnum other = (PartnerMaxChildNumChangeReasonEnum) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}
}