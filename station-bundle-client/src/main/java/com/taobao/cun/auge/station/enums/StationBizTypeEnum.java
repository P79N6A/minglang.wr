package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class StationBizTypeEnum implements Serializable {

	private static final Map<String, StationBizTypeEnum> MAPPING = new HashMap<String, StationBizTypeEnum>();

	private static final long serialVersionUID = -2325045809951918493L;

	private String code;
	private String desc;

	public static final StationBizTypeEnum STATION = new StationBizTypeEnum("STATION", "农村淘宝服务站");
	public static final StationBizTypeEnum YOUPIN = new StationBizTypeEnum("YOUPIN", "天猫优品服务站");
	public static final StationBizTypeEnum YOUPIN_ELEC = new StationBizTypeEnum("YOUPIN_ELEC",
			"天猫优品服务站-电器合作店");
	public static final StationBizTypeEnum TPS_ELEC = new StationBizTypeEnum("TPS_ELEC", "天猫优品电器体验店");

	static {
		MAPPING.put("STATION", STATION);
		MAPPING.put("YOUPIN", YOUPIN);
		MAPPING.put("YOUPIN_ELEC", YOUPIN_ELEC);
		MAPPING.put("TPS_ELEC", TPS_ELEC);
	}

	public StationBizTypeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public StationBizTypeEnum() {

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

	public static StationBizTypeEnum valueof(String code) {
		if (code == null) {
			return null;
		}
		return MAPPING.get(code);
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
		StationBizTypeEnum other = (StationBizTypeEnum) obj;
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
