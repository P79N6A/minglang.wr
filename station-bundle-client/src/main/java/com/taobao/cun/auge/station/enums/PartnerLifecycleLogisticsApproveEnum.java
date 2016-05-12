package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * 合伙人生命周期表，物流审批枚举
 * @author quanzhu.wangqz
 *
 */
public class PartnerLifecycleLogisticsApproveEnum  implements Serializable {
	
	private static final long serialVersionUID = -119918219648000754L;
	public static final PartnerLifecycleLogisticsApproveEnum TO_AUDIT  = new PartnerLifecycleLogisticsApproveEnum("TO_AUDIT", "待审批");
    public static final PartnerLifecycleLogisticsApproveEnum AUDIT_PASS = new PartnerLifecycleLogisticsApproveEnum("AUDIT_PASS", "审批通过");
    public static final PartnerLifecycleLogisticsApproveEnum AUDIT_NOPASS = new PartnerLifecycleLogisticsApproveEnum("AUDIT_NOPASS", "审批不通过");
	
	private static final Map<String, PartnerLifecycleLogisticsApproveEnum> mappings = new HashMap<String, PartnerLifecycleLogisticsApproveEnum>();
	
	static {
		mappings.put("TO_AUDIT", TO_AUDIT);
		mappings.put("AUDIT_PASS", AUDIT_PASS);
		mappings.put("AUDIT_NOPASS", AUDIT_NOPASS);
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
		if (!(obj instanceof PartnerLifecycleLogisticsApproveEnum))
			return false;
		PartnerLifecycleLogisticsApproveEnum objType = (PartnerLifecycleLogisticsApproveEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public PartnerLifecycleLogisticsApproveEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static PartnerLifecycleLogisticsApproveEnum valueof(String code) {
		if (code == null)
			return null;
		return mappings.get(code);
	}

	
	@SuppressWarnings("unused")
	private PartnerLifecycleLogisticsApproveEnum() {

	}
}