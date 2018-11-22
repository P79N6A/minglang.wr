package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author quanzhu.wangqz
 *
 */
public class StationClassEnum implements Serializable {

	private static final long serialVersionUID = -6688380187765631177L;

	public static final StationClassEnum STATION_YP = new StationClassEnum("STATION_YP", "天猫优品服务站");
	public static final StationClassEnum STATION_ELEC = new StationClassEnum("STATION_ELEC", "天猫优品服务站-电器");
	public static final StationClassEnum STORE_ELEC = new StationClassEnum("STORE_ELEC", "天猫优品电器体验店");

	public static final Map<String, StationClassEnum> MAPPINGS = new HashMap<String, StationClassEnum>();

	static {
		MAPPINGS.put("STATION_YP", STATION_YP);
		MAPPINGS.put("STATION_ELEC", STATION_ELEC);
		MAPPINGS.put("STORE_ELEC", STORE_ELEC);
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
		if (!(obj instanceof StationClassEnum)) {
            return false;
        }
		StationClassEnum objType = (StationClassEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public StationClassEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static StationClassEnum valueof(String code) {
		if (code == null) {
            return null;
        }
		return MAPPINGS.get(code);
	}

	@SuppressWarnings("unused")
	private StationClassEnum() {

	}
}
