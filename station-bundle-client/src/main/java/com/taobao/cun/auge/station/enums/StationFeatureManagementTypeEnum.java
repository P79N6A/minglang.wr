package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务站Feature字段，managementType(经营类型)属性枚举 
 * @author quanzhu.wangqz
 *
 */
public class StationFeatureManagementTypeEnum  implements Serializable {
	
	private static final long serialVersionUID = -119918219648000754L;
	public static final StationFeatureManagementTypeEnum BUSINESS  = new StationFeatureManagementTypeEnum("business", "企业(个体工商户)");
	public static final StationFeatureManagementTypeEnum PERSONAL = new StationFeatureManagementTypeEnum("personal", "个人");
	
	private static final Map<String, StationFeatureManagementTypeEnum> mappings = new HashMap<String, StationFeatureManagementTypeEnum>();
	
	static {
		mappings.put("business", BUSINESS);
		mappings.put("personal", PERSONAL);
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
		if (!(obj instanceof StationFeatureManagementTypeEnum))
			return false;
		StationFeatureManagementTypeEnum objType = (StationFeatureManagementTypeEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public StationFeatureManagementTypeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static StationFeatureManagementTypeEnum valueof(String code) {
		if (code == null)
			return null;
		return mappings.get(code);
	}

	
	@SuppressWarnings("unused")
	private StationFeatureManagementTypeEnum() {

	}
}
