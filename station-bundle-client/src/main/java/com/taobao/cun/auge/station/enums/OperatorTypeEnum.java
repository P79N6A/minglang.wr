package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class OperatorTypeEnum  implements Serializable {

	private static final long serialVersionUID = 3404367997386610523L;

	public static final OperatorTypeEnum BUC = new OperatorTypeEnum("BUC", "BUC用户");
	public static final OperatorTypeEnum HAVANA = new OperatorTypeEnum("HAVANA", "集团会员");
	public static final OperatorTypeEnum TAOBAO_FOR_GOV = new OperatorTypeEnum("TAOBAO_FOR_GOV", "政府合作淘宝会员");
	public static final OperatorTypeEnum SYSTEM = new OperatorTypeEnum("SYSTEM", "系统");
	public static final OperatorTypeEnum TPC = new OperatorTypeEnum("TPC", "tp商");
	
	private static final Map<String, OperatorTypeEnum> mappings = new HashMap<String, OperatorTypeEnum>();
	static {
		mappings.put("BUC", BUC);
		mappings.put("HAVANA", HAVANA);
		mappings.put("SYSTEM", SYSTEM);
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
		if (obj == null) {
            return false;
        }
		if (!(obj instanceof OperatorTypeEnum)) {
            return false;
        }
		OperatorTypeEnum objType = (OperatorTypeEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public OperatorTypeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static OperatorTypeEnum valueof(String code) {
		if (code == null) {
            return null;
        }
		return mappings.get(code);
	}

	public OperatorTypeEnum() {

	}
}
