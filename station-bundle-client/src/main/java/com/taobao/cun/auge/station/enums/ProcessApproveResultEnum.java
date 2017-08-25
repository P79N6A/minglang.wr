package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ProcessApproveResultEnum implements Serializable {

	private static final long serialVersionUID = 2593451750753952572L;
	
	public static final ProcessApproveResultEnum APPROVE_PASS = new ProcessApproveResultEnum("APPROVE_PASS", "审批通过");
	public static final ProcessApproveResultEnum APPROVE_REFUSE = new ProcessApproveResultEnum("APPROVE_REFUSE", "审批不通过 ");

	private static final Map<String, ProcessApproveResultEnum> mappings = new HashMap<String, ProcessApproveResultEnum>();

	static {
		mappings.put("APPROVE_PASS", APPROVE_PASS);
		mappings.put("APPROVE_REFUSE", APPROVE_REFUSE);
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
		if (!(obj instanceof ProcessApproveResultEnum)) {
            return false;
        }
		ProcessApproveResultEnum objType = (ProcessApproveResultEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public ProcessApproveResultEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static ProcessApproveResultEnum valueof(String code) {
		if (code == null) {
            return null;
        }
		return mappings.get(code);
	}

	@SuppressWarnings("unused")
	private ProcessApproveResultEnum() {

	}
}