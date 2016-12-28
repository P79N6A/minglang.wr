package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AssertUseStateEnum implements Serializable {

	private static final long serialVersionUID = 887254816612343446L;
	
	public static final AssertUseStateEnum HAS_RETURN = new AssertUseStateEnum("HAS_RETURN", "已归还");
	public static final AssertUseStateEnum NOT_RETURN = new AssertUseStateEnum("NOT_RETURN", "未归还 ");
	public static final AssertUseStateEnum NEED_PAY = new AssertUseStateEnum("NEED_PAY", "需赔付");

	private static final Map<String, AssertUseStateEnum> mappings = new HashMap<String, AssertUseStateEnum>();

	static {
		mappings.put("HAS_RETURN", HAS_RETURN);
		mappings.put("NOT_RETURN", NOT_RETURN);
		mappings.put("NEED_PAY", NEED_PAY);
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
		if (!(obj instanceof AssertUseStateEnum))
			return false;
		AssertUseStateEnum objType = (AssertUseStateEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public AssertUseStateEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static AssertUseStateEnum valueof(String code) {
		if (code == null)
			return null;
		return mappings.get(code);
	}

	@SuppressWarnings("unused")
	private AssertUseStateEnum() {

	}
}