package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务站Feature字段，VillageFlag(是否本村)属性枚举 
 * @author quanzhu.wangqz
 *
 */
public class StationFeatureVillageFlagEnum  implements Serializable {
	
	private static final long serialVersionUID = -119918219648000754L;
	public static final StationFeatureVillageFlagEnum Y  = new StationFeatureVillageFlagEnum("Y", "本村");
	public static final StationFeatureVillageFlagEnum N = new StationFeatureVillageFlagEnum("N", "邻村");
	
	private static final Map<String, StationFeatureVillageFlagEnum> mappings = new HashMap<String, StationFeatureVillageFlagEnum>();
	
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
		if (obj == null) {
            return false;
        }
		if (!(obj instanceof StationFeatureVillageFlagEnum)) {
            return false;
        }
		StationFeatureVillageFlagEnum objType = (StationFeatureVillageFlagEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public StationFeatureVillageFlagEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static StationFeatureVillageFlagEnum valueof(String code) {
		if (code == null) {
            return null;
        }
		return mappings.get(code);
	}

	
	@SuppressWarnings("unused")
	private StationFeatureVillageFlagEnum() {

	}
}
