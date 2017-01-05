package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PartnerFlowerNameApplyStatusEnum implements Serializable{
	private static final long serialVersionUID = 1L;
	public static final PartnerFlowerNameApplyStatusEnum WAIT_AUDIT = new PartnerFlowerNameApplyStatusEnum("WAIT_AUDIT", "待审核");
	public static final PartnerFlowerNameApplyStatusEnum AUDIT_PASS = new PartnerFlowerNameApplyStatusEnum("PAY", "审核通过");
	public static final PartnerFlowerNameApplyStatusEnum AUDIT_NOT_PASS = new PartnerFlowerNameApplyStatusEnum("DONE", "审核未通过");

	private static final Map<String, PartnerFlowerNameApplyStatusEnum> mappings = new HashMap<String, PartnerFlowerNameApplyStatusEnum>();

	static {
		mappings.put("WAIT_AUDIT", WAIT_AUDIT);
		mappings.put("AUDIT_PASS", AUDIT_PASS);
		mappings.put("AUDIT_NOT_PASS", AUDIT_NOT_PASS);
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

	public PartnerFlowerNameApplyStatusEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static PartnerFlowerNameApplyStatusEnum valueof(String code) {
		if (code == null)
			return null;
		return mappings.get(code);
	}

	public PartnerFlowerNameApplyStatusEnum() {

	}
}
