package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ProcessTypeEnum implements Serializable {

	private static final long serialVersionUID = 3179198050002314841L;

	private static final Map<String, ProcessTypeEnum> mappings = new HashMap<String, ProcessTypeEnum>();

	private String code;
	private String desc;

	// 保证金
	public static final ProcessTypeEnum CLOSING_PRO = new ProcessTypeEnum("CLOSING_PRO", "停业流程");
	public static final ProcessTypeEnum QUIT_PRO = new ProcessTypeEnum("QUIT_PRO", "退出流程");

	static {
		mappings.put("CLOSING_PRO", CLOSING_PRO);
		mappings.put("QUIT_PRO", QUIT_PRO);
	}

	public ProcessTypeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public ProcessTypeEnum() {

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

	public static ProcessTypeEnum valueof(String code) {
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
		ProcessTypeEnum other = (ProcessTypeEnum) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}
}
