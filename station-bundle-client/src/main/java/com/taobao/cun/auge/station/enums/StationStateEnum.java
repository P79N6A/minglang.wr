package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author quanzhu.wangqz
 *
 */
public class StationStateEnum implements Serializable {

	private static final long serialVersionUID = -6688380187765631177L;
	
	public static final StationStateEnum NORMAL = new StationStateEnum(
			"NORMAL", "正常");
	public static final StationStateEnum INVALID = new StationStateEnum(
			"INVALID", "失效");
	
	public static final Map<String, StationStateEnum> mappings = new HashMap<String, StationStateEnum>();
	
	static {
		mappings.put("NORMAL", NORMAL);
		mappings.put("INVALID", INVALID);
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
		if (!(obj instanceof StationStateEnum))
			return false;
		StationStateEnum objType = (StationStateEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public StationStateEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static StationStateEnum valueof(String code) {
		if (code == null)
			return null;
		return mappings.get(code);
	}

	@SuppressWarnings("unused")
	private StationStateEnum() {

	}
}
