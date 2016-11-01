package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 合伙人生命周期表，业务类型枚举
 * @author quanzhu.wangqz
 *
 */
public class PartnerLifecycleBusinessTypeEnum  implements Serializable {
	
	private static final long serialVersionUID = -119918219648000754L;
	public static final PartnerLifecycleBusinessTypeEnum SETTLING  = new PartnerLifecycleBusinessTypeEnum("SETTLING", "入驻中");
    public static final PartnerLifecycleBusinessTypeEnum CLOSING = new PartnerLifecycleBusinessTypeEnum("CLOSING", "停业中");
 	public static final PartnerLifecycleBusinessTypeEnum QUITING = new PartnerLifecycleBusinessTypeEnum("QUITING", "退出中");
 	public static final PartnerLifecycleBusinessTypeEnum DECORATING = new PartnerLifecycleBusinessTypeEnum("DECORATING", "装修中");
	
	private static final Map<String, PartnerLifecycleBusinessTypeEnum> mappings = new HashMap<String, PartnerLifecycleBusinessTypeEnum>();
	
	static {
		mappings.put("SETTLING", SETTLING);
		mappings.put("CLOSING", CLOSING);
		mappings.put("QUITING", QUITING);
		mappings.put("DECORATING", DECORATING);
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
		if (!(obj instanceof PartnerLifecycleBusinessTypeEnum))
			return false;
		PartnerLifecycleBusinessTypeEnum objType = (PartnerLifecycleBusinessTypeEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public PartnerLifecycleBusinessTypeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static PartnerLifecycleBusinessTypeEnum valueof(String code) {
		if (code == null)
			return null;
		return mappings.get(code);
	}

	
	@SuppressWarnings("unused")
	private PartnerLifecycleBusinessTypeEnum() {

	}
}
