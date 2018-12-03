package com.taobao.cun.auge.asset.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AssetCheckTaskTaskStatusEnum implements Serializable {

	private static final Map<String, AssetCheckTaskTaskStatusEnum> MAPPINGS = new HashMap<String, AssetCheckTaskTaskStatusEnum>();

	private static final long serialVersionUID = -2325045809951928493L;

	private String code;
	private String desc;

	public static final AssetCheckTaskTaskStatusEnum TODO = new AssetCheckTaskTaskStatusEnum("TODO", "未开始");
	public static final AssetCheckTaskTaskStatusEnum DOING = new AssetCheckTaskTaskStatusEnum("DOING", "进行中");
	public static final AssetCheckTaskTaskStatusEnum DONE = new AssetCheckTaskTaskStatusEnum("DONE", "已完成");


	static {
		MAPPINGS.put("TODO", TODO);
		MAPPINGS.put("DOING", DOING);
		MAPPINGS.put("DONE", DONE);
	}

	public AssetCheckTaskTaskStatusEnum(String code, String desc) {
	        this.code = code;
	        this.desc = desc;
	    }

	public AssetCheckTaskTaskStatusEnum() {

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

	public static AssetCheckTaskTaskStatusEnum valueof(String code) {
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
		AssetCheckTaskTaskStatusEnum other = (AssetCheckTaskTaskStatusEnum) obj;
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
