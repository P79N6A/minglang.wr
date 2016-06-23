package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
/**
 * 合伙人生命周期表，系统任务枚举
 * @author quanzhu.wangqz
 *
 */
public class PartnerLifecycleSystemEnum  implements Serializable {
	
	private static final long serialVersionUID = -119918219648000754L;
	public static final PartnerLifecycleSystemEnum WAIT_PROCESS  = new PartnerLifecycleSystemEnum("WAIT_PROCESS", "待处理");
    public static final PartnerLifecycleSystemEnum HAS_PROCESS = new PartnerLifecycleSystemEnum("HAS_FROZEN", "已处理");
	
	private static final Map<String, PartnerLifecycleSystemEnum> mappings = new HashMap<String, PartnerLifecycleSystemEnum>();
	
	static {
		mappings.put("WAIT_PROCESS", WAIT_PROCESS);
		mappings.put("HAS_PROCESS", HAS_PROCESS);
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
		if (!(obj instanceof PartnerLifecycleSystemEnum))
			return false;
		PartnerLifecycleSystemEnum objType = (PartnerLifecycleSystemEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public PartnerLifecycleSystemEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static PartnerLifecycleSystemEnum valueof(String code) {
		if (code == null)
			return null;
		return mappings.get(code);
	}

	
	@SuppressWarnings("unused")
	private PartnerLifecycleSystemEnum() {

	}
}
