package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PartnerPeixunStatusEnum implements Serializable{

	private static final long serialVersionUID = 1L;
	public static final PartnerPeixunStatusEnum NEW = new PartnerPeixunStatusEnum("NEW", "未培训");
	public static final PartnerPeixunStatusEnum PAY = new PartnerPeixunStatusEnum("PAY", "待签到");
	public static final PartnerPeixunStatusEnum DONE = new PartnerPeixunStatusEnum("DONE", "已签到");
	public static final PartnerPeixunStatusEnum WAIT_PAY = new PartnerPeixunStatusEnum("WAIT_PAY", "待付款");
	public static final PartnerPeixunStatusEnum REFUND = new PartnerPeixunStatusEnum("REFUND", "已退款");
	public static final PartnerPeixunStatusEnum REFUNDING = new PartnerPeixunStatusEnum("REFUNDING", "退款进行中");


	private static final Map<String, PartnerPeixunStatusEnum> mappings = new HashMap<String, PartnerPeixunStatusEnum>();

	static {
		mappings.put("NEW", NEW);
		mappings.put("PAY", PAY);
		mappings.put("DONE", DONE);
		mappings.put("WAIT_PAY", WAIT_PAY);
		mappings.put("REFUND", REFUND);
		mappings.put("REFUNDING", REFUNDING);

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
		if (obj == null) {
            return false;
        }
		if (!(obj instanceof OperatorTypeEnum)) {
            return false;
        }
		OperatorTypeEnum objType = (OperatorTypeEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public PartnerPeixunStatusEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static PartnerPeixunStatusEnum valueof(String code) {
		if (code == null) {
            return null;
        }
		return mappings.get(code);
	}

	public PartnerPeixunStatusEnum() {

	}
}
