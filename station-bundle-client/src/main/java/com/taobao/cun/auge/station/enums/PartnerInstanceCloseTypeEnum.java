package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 合伙人实例 退出类型枚举
 * 
 *
 */
public class PartnerInstanceCloseTypeEnum implements Serializable {

	private static final long serialVersionUID = -7635436346233858685L;

	private static final Map<String, PartnerInstanceCloseTypeEnum> mappings = new HashMap<String, PartnerInstanceCloseTypeEnum>();

	private String code;
	private String desc;

	public static final PartnerInstanceCloseTypeEnum WORKER_QUIT = new PartnerInstanceCloseTypeEnum("worker-quit",
			"强制清退");

	public static final PartnerInstanceCloseTypeEnum PARTNER_QUIT = new PartnerInstanceCloseTypeEnum("partner-quit",
			"主动申请退出");

	static {
		mappings.put("worker-quit", WORKER_QUIT);
		mappings.put("partner-quit", PARTNER_QUIT);
	}

	public PartnerInstanceCloseTypeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public PartnerInstanceCloseTypeEnum() {

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

	public static PartnerInstanceCloseTypeEnum valueof(String code) {
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
		PartnerInstanceCloseTypeEnum other = (PartnerInstanceCloseTypeEnum) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}
}
