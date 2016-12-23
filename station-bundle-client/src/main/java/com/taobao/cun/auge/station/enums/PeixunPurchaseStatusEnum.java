package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PeixunPurchaseStatusEnum implements Serializable{

	private static final long serialVersionUID = 1L;
	public static final PeixunPurchaseStatusEnum WAIT_AUDIT = new PeixunPurchaseStatusEnum("WAIT_AUDIT", "待审核");
	public static final PeixunPurchaseStatusEnum AUDIT_PASS = new PeixunPurchaseStatusEnum("AUDIT_PASS", "审核通过");
	public static final PeixunPurchaseStatusEnum ROLLBACK = new PeixunPurchaseStatusEnum("ROLLBACK", "撤回");
	public static final PeixunPurchaseStatusEnum AUDIT_NOT_PASS = new PeixunPurchaseStatusEnum("AUDIT_NOT_PASS", "审核未通过");
	public static final PeixunPurchaseStatusEnum ORDER = new PeixunPurchaseStatusEnum("ORDER", "已下单");

	private static final Map<String, PeixunPurchaseStatusEnum> mappings = new HashMap<String, PeixunPurchaseStatusEnum>();

	static {
		mappings.put("WAIT_AUDIT", WAIT_AUDIT);
		mappings.put("AUDIT_PASS", AUDIT_PASS);
		mappings.put("ROLLBACK", ROLLBACK);
		mappings.put("AUDIT_NOT_PASS", AUDIT_NOT_PASS);
		mappings.put("ORDER", ORDER);
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

	public PeixunPurchaseStatusEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static PeixunPurchaseStatusEnum valueof(String code) {
		if (code == null)
			return null;
		return mappings.get(code);
	}

	public PeixunPurchaseStatusEnum() {

	}
}
