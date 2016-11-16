package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务站Feature字段，PlaceFlag(是否有经营场地)属性枚举 
 * @author quanzhu.wangqz
 *
 */
public class StationFeaturePlaceFlagEnum  implements Serializable {
	
	private static final long serialVersionUID = -119918219648000754L;
	public static final StationFeaturePlaceFlagEnum Y  = new StationFeaturePlaceFlagEnum("Y", "有");
	public static final StationFeaturePlaceFlagEnum N = new StationFeaturePlaceFlagEnum("N", "无");
	
	private static final Map<String, StationFeaturePlaceFlagEnum> mappings = new HashMap<String, StationFeaturePlaceFlagEnum>();
	
	static {
		mappings.put("Y", Y);
		mappings.put("N", N);
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
		if (!(obj instanceof StationFeaturePlaceFlagEnum))
			return false;
		StationFeaturePlaceFlagEnum objType = (StationFeaturePlaceFlagEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public StationFeaturePlaceFlagEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static StationFeaturePlaceFlagEnum valueof(String code) {
		if (code == null)
			return null;
		return mappings.get(code);
	}

	
	@SuppressWarnings("unused")
	private StationFeaturePlaceFlagEnum() {

	}
}
