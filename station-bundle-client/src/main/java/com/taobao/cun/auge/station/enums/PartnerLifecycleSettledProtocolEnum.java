package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 合伙人生命周期表，入驻协议枚举
 * @author quanzhu.wangqz
 *
 */
public class PartnerLifecycleSettledProtocolEnum  implements Serializable {
	
	private static final long serialVersionUID = -119918219648000754L;
	public static final PartnerLifecycleSettledProtocolEnum SIGNING  = new PartnerLifecycleSettledProtocolEnum("SIGNING", "未签署");
    public static final PartnerLifecycleSettledProtocolEnum SIGNED = new PartnerLifecycleSettledProtocolEnum("SIGNED", "已签署");
	
	private static final Map<String, PartnerLifecycleSettledProtocolEnum> mappings = new HashMap<String, PartnerLifecycleSettledProtocolEnum>();
	
	static {
		mappings.put("SIGNING", SIGNING);
		mappings.put("SIGNED", SIGNED);
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
		if (!(obj instanceof PartnerLifecycleSettledProtocolEnum))
			return false;
		PartnerLifecycleSettledProtocolEnum objType = (PartnerLifecycleSettledProtocolEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public PartnerLifecycleSettledProtocolEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static PartnerLifecycleSettledProtocolEnum valueof(String code) {
		if (code == null)
			return null;
		return mappings.get(code);
	}

	
	@SuppressWarnings("unused")
	private PartnerLifecycleSettledProtocolEnum() {

	}
}
