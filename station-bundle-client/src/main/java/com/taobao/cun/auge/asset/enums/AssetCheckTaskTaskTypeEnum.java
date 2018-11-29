package com.taobao.cun.auge.asset.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AssetCheckTaskTaskTypeEnum implements Serializable {

	private static final Map<String, AssetCheckTaskTaskTypeEnum> MAPPINGS = new HashMap<String, AssetCheckTaskTaskTypeEnum>();

	private static final long serialVersionUID = -2325045809951928493L;

	private String code;
	private String desc;

	public static final AssetCheckTaskTaskTypeEnum STATION_CHECK = new AssetCheckTaskTaskTypeEnum("STATION_CHECK", "村点盘点");
	public static final AssetCheckTaskTaskTypeEnum COUNTY_FOLLOW = new AssetCheckTaskTaskTypeEnum("COUNTY_FOLLOW", "县点跟踪村点进度");
	public static final AssetCheckTaskTaskTypeEnum COUNTY_CHECK = new AssetCheckTaskTaskTypeEnum("COUNTY_CHECK", "县点盘点");


	static {
		MAPPINGS.put("STATION_CHECK", STATION_CHECK);
		MAPPINGS.put("COUNTY_FOLLOW", COUNTY_FOLLOW);
		MAPPINGS.put("COUNTY_CHECK", COUNTY_CHECK);
	}

	public AssetCheckTaskTaskTypeEnum(String code, String desc) {
	        this.code = code;
	        this.desc = desc;
	    }

	public AssetCheckTaskTaskTypeEnum() {

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

	public static AssetCheckTaskTaskTypeEnum valueof(String code) {
		if (code == null) {
			return null;
		}
		return MAPPINGS.get(code);
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
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AssetCheckTaskTaskTypeEnum other = (AssetCheckTaskTaskTypeEnum) obj;
		if (code == null) {
			if (other.code != null) {
				return false;
			}
		} else if (!code.equals(other.code)) {
			return false;
		}
		return true;
	}
}
