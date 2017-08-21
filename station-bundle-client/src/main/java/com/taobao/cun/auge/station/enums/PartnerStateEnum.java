package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * partner实体状态枚举，包括 暂存（TEMP），有效(normal)
 * 
 * @author quanzhu.wangqz
 *
 */

public class PartnerStateEnum implements Serializable {

	private static final long serialVersionUID = -2698634278143088180L;
	// 暂存
	public static final PartnerStateEnum TEMP = new PartnerStateEnum("TEMP","暂存");
	// 有效
	public static final PartnerStateEnum NORMAL = new PartnerStateEnum("NORMAL", "有效");

	private static final Map<String, PartnerStateEnum> mappings = new HashMap<String, PartnerStateEnum>();

	static {
		mappings.put("TEMP", TEMP);
		mappings.put("NORMAL", NORMAL);
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
		if (!(obj instanceof PartnerStateEnum)) {
            return false;
        }
		PartnerStateEnum objType = (PartnerStateEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public PartnerStateEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static PartnerStateEnum valueof(String code) {
		if (code == null) {
            return null;
        }
		return mappings.get(code);
	}

	@SuppressWarnings("unused")
	private PartnerStateEnum() {

	}
}
