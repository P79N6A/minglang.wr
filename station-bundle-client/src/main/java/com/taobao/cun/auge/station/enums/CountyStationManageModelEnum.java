package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CountyStationManageModelEnum  implements Serializable{
	
	private static final long serialVersionUID = -543989776847223763L;

	public static final CountyStationManageModelEnum SELF = new CountyStationManageModelEnum("SELF", "自营");
	public static final CountyStationManageModelEnum THIRD_PARTY = new CountyStationManageModelEnum("THIRD_PARTY","第三方");
	public static final CountyStationManageModelEnum NO_WAREHOUSE = new CountyStationManageModelEnum("NO_WAREHOUSE","无县仓");
	
	private static final Map<String, CountyStationManageModelEnum> mappings = new HashMap<String, CountyStationManageModelEnum>();
	static {
		mappings.put("SELF", SELF);
		mappings.put("THIRD_PARTY", THIRD_PARTY);
		mappings.put("NO_WAREHOUSE", NO_WAREHOUSE);
	}

	private String code;
	private String desc;

	@SuppressWarnings("unused")
	private CountyStationManageModelEnum() {

	}

	public CountyStationManageModelEnum(String code, String desc) {
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
		if (obj == null) {
            return false;
        }

		if (!(obj instanceof CountyStationManageModelEnum)) {
            return false;
        }

		CountyStationManageModelEnum objType = (CountyStationManageModelEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public static CountyStationManageModelEnum valueof(String code) {
		if (code == null) {
            return null;
        }
		return mappings.get(code);
	}
}
