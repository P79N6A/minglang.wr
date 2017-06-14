package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PartnerPeixunRefundStatusEnum implements Serializable{

	private static final long serialVersionUID = 1L;

	public static final PartnerPeixunRefundStatusEnum REFOND_COMMIT = new PartnerPeixunRefundStatusEnum("REFOND_COMMIT", "退款已提交");
	public static final PartnerPeixunRefundStatusEnum REFOND_WAIT_AUDIT = new PartnerPeixunRefundStatusEnum("REFOND_WAIT_AUDIT", "退款待审核");
	public static final PartnerPeixunRefundStatusEnum REFOUNDING = new PartnerPeixunRefundStatusEnum("REFOUNDING", "退款中");
	public static final PartnerPeixunRefundStatusEnum REFOUND_DONE = new PartnerPeixunRefundStatusEnum("REFOUND_DONE", "已退款至通用账户");
	public static final PartnerPeixunRefundStatusEnum REFOUND_AUDIT_NOT_PASS = new PartnerPeixunRefundStatusEnum("REFOUND_AUDIT_NOT_PASS", "退款审核未通过");


	private static final Map<String, PartnerPeixunRefundStatusEnum> mappings = new HashMap<String, PartnerPeixunRefundStatusEnum>();

	static {
		mappings.put("REFOND_COMMIT", REFOND_COMMIT);
		mappings.put("REFOND_WAIT_AUDIT", REFOND_WAIT_AUDIT);
		mappings.put("REFOUNDING", REFOUNDING);
		mappings.put("REFOUND_DONE", REFOUND_DONE);
		mappings.put("REFOUND_AUDIT_NOT_PASS", REFOUND_AUDIT_NOT_PASS);

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

	public PartnerPeixunRefundStatusEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static PartnerPeixunRefundStatusEnum valueof(String code) {
		if (code == null)
			return null;
		return mappings.get(code);
	}

	public PartnerPeixunRefundStatusEnum() {

	}
}
