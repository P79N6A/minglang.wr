package com.taobao.cun.auge.county.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class StationManagerEnum implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	public static final StationManagerEnum operatingOperator = new StationManagerEnum("operatingOperator", "运营负责人");
	public static final StationManagerEnum trainingOperator = new StationManagerEnum("trainingOperator", "培训负责人");
	public static final StationManagerEnum thirdOperator = new StationManagerEnum(
			"thirdOperator", "第三方负责人");
	
	public static final StationManagerEnum propagandaOperator = new StationManagerEnum("propagandaOperator", "宣传和招募");
	public static final StationManagerEnum otherOperator = new StationManagerEnum("otherOperator", "其他负责人");
	public static final StationManagerEnum totalOperator = new StationManagerEnum("totalOperator", "综合支持");
	
	
	
	
	public static final Map<String, StationManagerEnum> mappings = new HashMap<String, StationManagerEnum>();
	static {
		mappings.put("operatingOperator", operatingOperator);
		mappings.put("trainingOperator", trainingOperator);
		mappings.put("thirdOperator", thirdOperator);
		mappings.put("propagandaOperator", propagandaOperator);
		mappings.put("otherOperator", otherOperator);
		mappings.put("totalOperator", totalOperator);
	}

	private String code;
	private String desc;

	@SuppressWarnings("unused")
	private StationManagerEnum() {

	}

	public StationManagerEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
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

		if (!(obj instanceof StationManagerEnum))
			return false;

		StationManagerEnum objType = (StationManagerEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public static StationManagerEnum valueof(String code) {
		if (code == null)
			return null;
		return mappings.get(code);
	}
}
