package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class StationTransInfoBizTypeEnum implements Serializable {

	private static final Map<String, StationTransInfoBizTypeEnum> MAPPING = new HashMap<String, StationTransInfoBizTypeEnum>();

	private static final long serialVersionUID = -2325045809951918493L;

	private String code;
	private String desc;

	public static final StationTransInfoBizTypeEnum STATION = new StationTransInfoBizTypeEnum("STATION", "农村淘宝服务站");
	public static final StationTransInfoBizTypeEnum YOUPIN = new StationTransInfoBizTypeEnum("YOUPIN", "天猫优品服务站");
	public static final StationTransInfoBizTypeEnum YOUPIN_ELEC = new StationTransInfoBizTypeEnum("YOUPIN_ELEC",
			"天猫优品服务站-电器合作店");
	public static final StationTransInfoBizTypeEnum TPS_ELEC = new StationTransInfoBizTypeEnum("TPS_ELEC", "天猫优品电器体验店");

	static {
		MAPPING.put("STATION", STATION);
		MAPPING.put("YOUPIN", YOUPIN);
		MAPPING.put("YOUPIN_ELEC", YOUPIN_ELEC);
		MAPPING.put("TPS_ELEC", TPS_ELEC);
	}

	public StationTransInfoBizTypeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public StationTransInfoBizTypeEnum() {

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

	public static StationTransInfoBizTypeEnum valueof(String code) {
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
		StationTransInfoBizTypeEnum other = (StationTransInfoBizTypeEnum) obj;
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
