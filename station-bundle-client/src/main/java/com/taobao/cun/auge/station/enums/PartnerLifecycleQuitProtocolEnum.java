package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 合伙人生命周期表，退出协议枚举
 * @author quanzhu.wangqz
 *
 */
public class PartnerLifecycleQuitProtocolEnum  implements Serializable {
	
	private static final long serialVersionUID = -119918219648000754L;
	public static final PartnerLifecycleQuitProtocolEnum SIGNING  = new PartnerLifecycleQuitProtocolEnum("SIGNING", "未签署");
    public static final PartnerLifecycleQuitProtocolEnum SIGNED = new PartnerLifecycleQuitProtocolEnum("SIGNED", "已签署");
	
	private static final Map<String, PartnerLifecycleQuitProtocolEnum> mappings = new HashMap<String, PartnerLifecycleQuitProtocolEnum>();
	
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
		if (obj == null) {
            return false;
        }
		if (!(obj instanceof PartnerLifecycleQuitProtocolEnum)) {
            return false;
        }
		PartnerLifecycleQuitProtocolEnum objType = (PartnerLifecycleQuitProtocolEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public PartnerLifecycleQuitProtocolEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static PartnerLifecycleQuitProtocolEnum valueof(String code) {
		if (code == null) {
            return null;
        }
		return mappings.get(code);
	}

	
	@SuppressWarnings("unused")
	private PartnerLifecycleQuitProtocolEnum() {

	}
}
