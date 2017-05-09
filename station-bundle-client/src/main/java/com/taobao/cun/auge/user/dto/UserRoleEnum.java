package com.taobao.cun.auge.user.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UserRoleEnum implements Serializable {
	private static final long serialVersionUID = 6513131051440488964L;

	public static final UserRoleEnum COUNTY_LEADER = new UserRoleEnum("COUNTY_LEADER", "县负责人");

	public static final UserRoleEnum TEAM_LEADER = new UserRoleEnum("TEAM_LEADER", "特战队长");

	public static final UserRoleEnum PROVINCE_LEADER = new UserRoleEnum("PROVINCE_LEADER", "省负责人");

	private static final Map<String, UserRoleEnum> mappings = new HashMap<String, UserRoleEnum>();
	static {
		mappings.put("COUNTY_LEADER", COUNTY_LEADER);
		mappings.put("TEAM_LEADER", TEAM_LEADER);
		mappings.put("PROVINCE_LEADER", PROVINCE_LEADER);
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
		if (!(obj instanceof UserRoleEnum))
			return false;
		UserRoleEnum objType = (UserRoleEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public UserRoleEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static UserRoleEnum valueof(String code) {
		if (code == null)
			return null;
		return mappings.get(code);
	}

	@SuppressWarnings("unused")
	private UserRoleEnum() {

	}
}
