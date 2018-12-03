package com.taobao.cun.auge.asset.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AssetCheckInfoAssetTypeEnum implements Serializable {

	private static final Map<String, AssetCheckInfoAssetTypeEnum> MAPPINGS = new HashMap<String, AssetCheckInfoAssetTypeEnum>();

	private static final long serialVersionUID = -2325045809951928493L;

	private String code;
	private String desc;

	public static final AssetCheckInfoAssetTypeEnum IT = new AssetCheckInfoAssetTypeEnum("IT", "it资产");
	public static final AssetCheckInfoAssetTypeEnum ADMIN = new AssetCheckInfoAssetTypeEnum("ADMIN", "行政资产");

	static {
		MAPPINGS.put("IT", IT);
		MAPPINGS.put("ADMIN", ADMIN);
	}

	public AssetCheckInfoAssetTypeEnum(String code, String desc) {
	        this.code = code;
	        this.desc = desc;
	    }

	public AssetCheckInfoAssetTypeEnum() {

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

	public static AssetCheckInfoAssetTypeEnum valueof(String code) {
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
		AssetCheckInfoAssetTypeEnum other = (AssetCheckInfoAssetTypeEnum) obj;
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
