package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 合伙人生命周期表，小二确认枚举
 * @author quanzhu.wangqz
 *
 */
public class PartnerLifecycleConfirmEnum  implements Serializable {
	
	private static final long serialVersionUID = -119918219648000754L;
	public static final PartnerLifecycleConfirmEnum CONFIRM  = new PartnerLifecycleConfirmEnum("CONFIRM", "确认");
    public static final PartnerLifecycleConfirmEnum CANCEL = new PartnerLifecycleConfirmEnum("CANCEL", "取消");
 	public static final PartnerLifecycleConfirmEnum WAIT_CONFIRM = new PartnerLifecycleConfirmEnum("WAIT_CONFIRM", "待确认");
	
	private static final Map<String, PartnerLifecycleConfirmEnum> mappings = new HashMap<String, PartnerLifecycleConfirmEnum>();
	
	static {
		mappings.put("CONFIRM", CONFIRM);
		mappings.put("CANCEL", CANCEL);
		mappings.put("WAIT_CONFIRM", WAIT_CONFIRM);
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
		if (!(obj instanceof PartnerLifecycleConfirmEnum))
			return false;
		PartnerLifecycleConfirmEnum objType = (PartnerLifecycleConfirmEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public PartnerLifecycleConfirmEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static PartnerLifecycleConfirmEnum valueof(String code) {
		if (code == null)
			return null;
		return mappings.get(code);
	}

	
	@SuppressWarnings("unused")
	private PartnerLifecycleConfirmEnum() {

	}
}
