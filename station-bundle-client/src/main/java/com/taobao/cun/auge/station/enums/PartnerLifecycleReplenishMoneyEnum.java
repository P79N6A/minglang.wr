package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
/**
 * 合伙人生命周期表，补货金冻结枚举
 * @author quanzhu.wangqz
 *
 */
public class PartnerLifecycleReplenishMoneyEnum  implements Serializable {
	
	private static final long serialVersionUID = -119918213648000754L;
	public static final PartnerLifecycleReplenishMoneyEnum WAIT_FROZEN  = new PartnerLifecycleReplenishMoneyEnum("WAIT_FROZEN", "待冻结");
    public static final PartnerLifecycleReplenishMoneyEnum HAS_FROZEN = new PartnerLifecycleReplenishMoneyEnum("HAS_FROZEN", "已冻结");
    public static final PartnerLifecycleReplenishMoneyEnum WAIT_THAW = new PartnerLifecycleReplenishMoneyEnum("WAIT_THAW", "待解冻");
 	public static final PartnerLifecycleReplenishMoneyEnum HAS_THAW = new PartnerLifecycleReplenishMoneyEnum("HAS_THAW", "已解冻");
	
	private static final Map<String, PartnerLifecycleReplenishMoneyEnum> mappings = new HashMap<String, PartnerLifecycleReplenishMoneyEnum>();
	
	static {
		mappings.put("WAIT_FROZEN", WAIT_FROZEN);
		mappings.put("HAS_FROZEN", HAS_FROZEN);
		mappings.put("WAIT_THAW", WAIT_THAW);
		mappings.put("HAS_THAW", HAS_THAW);
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
		if (!(obj instanceof PartnerLifecycleReplenishMoneyEnum)) {
            return false;
        }
		PartnerLifecycleReplenishMoneyEnum objType = (PartnerLifecycleReplenishMoneyEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public PartnerLifecycleReplenishMoneyEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static PartnerLifecycleReplenishMoneyEnum valueof(String code) {
		if (code == null) {
            return null;
        }
		return mappings.get(code);
	}

	
	@SuppressWarnings("unused")
	private PartnerLifecycleReplenishMoneyEnum() {

	}
}
