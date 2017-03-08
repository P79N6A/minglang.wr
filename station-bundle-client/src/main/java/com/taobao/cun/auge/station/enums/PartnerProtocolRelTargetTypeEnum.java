package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author quanzhu.wangqz
 *
 */
public class PartnerProtocolRelTargetTypeEnum  implements Serializable {

	private static final Map<String, PartnerProtocolRelTargetTypeEnum> mappings = new HashMap<String, PartnerProtocolRelTargetTypeEnum>();

	private static final long serialVersionUID = -2325045809951918493L;

	private String code;
	private String desc;
	
	public static final PartnerProtocolRelTargetTypeEnum CRIUS_STATION = new PartnerProtocolRelTargetTypeEnum(
			"CRIUS_STATION", "村点");

	public static final PartnerProtocolRelTargetTypeEnum PARTNER_INSTANCE = new PartnerProtocolRelTargetTypeEnum(
			"PARTNER_INSTANCE", "合伙实例");
	
	public static final PartnerProtocolRelTargetTypeEnum PARTNER = new PartnerProtocolRelTargetTypeEnum(
			"PARTNER", "合伙人");
	static {
		mappings.put("CRIUS_STATION", CRIUS_STATION);
		mappings.put("PARTNER_INSTANCE", PARTNER_INSTANCE);
		mappings.put("PARTNER", PARTNER);

	}

	public PartnerProtocolRelTargetTypeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public PartnerProtocolRelTargetTypeEnum() {

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

	public static PartnerProtocolRelTargetTypeEnum valueof(String code) {
		if (code == null)
			return null;
		return mappings.get(code);
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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PartnerProtocolRelTargetTypeEnum other = (PartnerProtocolRelTargetTypeEnum) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}
}
