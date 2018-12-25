package com.taobao.cun.auge.asset.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AssetCheckInfoCheckTypeEnum implements Serializable {

	private static final Map<String, AssetCheckInfoCheckTypeEnum> MAPPINGS = new HashMap<String, AssetCheckInfoCheckTypeEnum>();

	private static final long serialVersionUID = -2325045809951928493L;

	private String code;
	private String desc;

	public static final AssetCheckInfoCheckTypeEnum SPECIAL = new AssetCheckInfoCheckTypeEnum("SPECIAL", "异常盘点");
	public static final AssetCheckInfoCheckTypeEnum COMMON = new AssetCheckInfoCheckTypeEnum("COMMON", "普通盘点");

	static {
		MAPPINGS.put("SPECIAL", SPECIAL);
		MAPPINGS.put("COMMON", COMMON);
	}

	public AssetCheckInfoCheckTypeEnum(String code, String desc) {
	        this.code = code;
	        this.desc = desc;
	    }

	public AssetCheckInfoCheckTypeEnum() {

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

	public static AssetCheckInfoCheckTypeEnum valueof(String code) {
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
		AssetCheckInfoCheckTypeEnum other = (AssetCheckInfoCheckTypeEnum) obj;
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
