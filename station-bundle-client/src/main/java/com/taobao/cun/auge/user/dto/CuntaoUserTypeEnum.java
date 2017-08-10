package com.taobao.cun.auge.user.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CuntaoUserTypeEnum implements Serializable {
	private static final long serialVersionUID = 3404367997386610523L;
	public static final CuntaoUserTypeEnum BUC = new CuntaoUserTypeEnum(
			"BUC", "BUC用户");
	public static final CuntaoUserTypeEnum TAOBAO_FOR_GOV = new CuntaoUserTypeEnum(
			"TAOBAO_FOR_GOV", "政府用户");
	public static final CuntaoUserTypeEnum TPC = new CuntaoUserTypeEnum(
			"TPC", "TP商");
	private static final Map<String, CuntaoUserTypeEnum> mappings = new HashMap<String, CuntaoUserTypeEnum>();
	static {
		mappings.put("BUC", BUC);
		mappings.put("TAOBAO_FOR_GOV", TAOBAO_FOR_GOV);
		mappings.put("TPC", TPC);
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
		if (!(obj instanceof CuntaoUserTypeEnum))
			return false;
		CuntaoUserTypeEnum objType = (CuntaoUserTypeEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public CuntaoUserTypeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static CuntaoUserTypeEnum valueof(String code) {
		if (code==null){
			return BUC;
		}
		CuntaoUserTypeEnum userType= mappings.get(code);
		if(userType==null){
			return BUC;
		}
		return userType;
	}

	@SuppressWarnings("unused")
	public CuntaoUserTypeEnum() {

	}
}
