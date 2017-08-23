package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CountyStationLeaseTypeEnum implements Serializable{
	private static final long serialVersionUID = -5254016933305435258L;

	public static final CountyStationLeaseTypeEnum FREE = new CountyStationLeaseTypeEnum("free", "免费");
	public static final CountyStationLeaseTypeEnum PAY = new CountyStationLeaseTypeEnum("pay", "付费");

	private static final Map<String, CountyStationLeaseTypeEnum> mappings = new HashMap<String, CountyStationLeaseTypeEnum>();
	static {
		mappings.put("free", FREE);
		mappings.put("pay", PAY);
	}

	private String code;
	private String desc;

	@SuppressWarnings("unused")
	private CountyStationLeaseTypeEnum() {

	}

	public CountyStationLeaseTypeEnum(String code, String desc) {
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

		if (!(obj instanceof CountyStationLeaseTypeEnum)) {
            return false;
        }

		CountyStationLeaseTypeEnum objType = (CountyStationLeaseTypeEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public static CountyStationLeaseTypeEnum valueof(String code) {
		if (code == null) {
            return null;
        }
		return mappings.get(code);
	}
}
