package com.taobao.cun.auge.user.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CuntaoUserStausEnum implements Serializable{

	private static final long serialVersionUID = -6892098699575414897L;
	public static final CuntaoUserStausEnum QUIT = new CuntaoUserStausEnum(
			"QUIT", "离职");
	public static final CuntaoUserStausEnum TRANSFER = new CuntaoUserStausEnum(
			"TRANSFER", "转岗");
	public static final CuntaoUserStausEnum VALID = new CuntaoUserStausEnum(
			"VALID", "有效");
	public static final CuntaoUserStausEnum INVALID = new CuntaoUserStausEnum(
			"INVALID", "无效");
    private static final Map<String, CuntaoUserStausEnum> mappings = new HashMap<String, CuntaoUserStausEnum>();
	static {
		mappings.put("SELF_SUPPORT", QUIT);
		mappings.put("NO_SELF_SUPPORT", TRANSFER);
		mappings.put("VALID", VALID);
		mappings.put("INVALID", INVALID);


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

	public CuntaoUserStausEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	public final static CuntaoUserStausEnum valueof(String code) {
		if (code==null) {
            return null;
        }
		return mappings.get(code);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
            return false;
        }
		if (!(obj instanceof CuntaoUserStausEnum)) {
            return false;
        }
		CuntaoUserStausEnum objType = (CuntaoUserStausEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

    @Override
    public int hashCode() {
        int result = code.hashCode();
        result = 31 * result + desc.hashCode();
        return result;
    }

	public CuntaoUserStausEnum() {
		super();
	}
}
