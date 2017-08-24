package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 合伙人生命周期表，装修状态枚举
 * @author quanzhu.wangqz
 *
 */
public class PartnerLifecyclePositionConfirmEnum  implements Serializable {
	
	private static final long serialVersionUID = -119918219648000754L;
	public static final PartnerLifecyclePositionConfirmEnum N  = new PartnerLifecyclePositionConfirmEnum("N", "未确认");
 	public static final PartnerLifecyclePositionConfirmEnum Y = new PartnerLifecyclePositionConfirmEnum("Y", "已确认");
 	
	
	private static final Map<String, PartnerLifecyclePositionConfirmEnum> mappings = new HashMap<String, PartnerLifecyclePositionConfirmEnum>();
	
	static {
		mappings.put("N", N);
		mappings.put("Y", Y);
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
		if (!(obj instanceof PartnerLifecyclePositionConfirmEnum)) {
            return false;
        }
		PartnerLifecyclePositionConfirmEnum objType = (PartnerLifecyclePositionConfirmEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public PartnerLifecyclePositionConfirmEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static PartnerLifecyclePositionConfirmEnum valueof(String code) {
		if (code == null) {
            return null;
        }
		return mappings.get(code);
	}

	
	@SuppressWarnings("unused")
	private PartnerLifecyclePositionConfirmEnum() {

	}
}
