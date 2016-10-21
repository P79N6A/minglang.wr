package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

/**
 * 合伙人实例表 类型枚举
 * 
 * @author quanzhu.wangqz
 *
 */
public class PartnerInstanceTypeEnum implements Serializable {

	private static final long serialVersionUID = 7138576667532117709L;

	public static final PartnerInstanceTypeEnum TP = new PartnerInstanceTypeEnum("TP", "合伙人", PartnerInstanceType.TP);
	public static final PartnerInstanceTypeEnum TPA = new PartnerInstanceTypeEnum("TPA", "淘帮手", PartnerInstanceType.TPA);
	public static final PartnerInstanceTypeEnum TPV = new PartnerInstanceTypeEnum("TPV", "村拍档", PartnerInstanceType.TPV);

	private static final Map<String, PartnerInstanceTypeEnum> mappings = new HashMap<String, PartnerInstanceTypeEnum>();
	static {
		mappings.put("TPA", TPA);
		mappings.put("TP", TP);
		mappings.put("TPV", TPV);
	}
	@NotNull
	private String code;
	@NotNull
	private String desc;
	@NotNull
	private PartnerInstanceType type;

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

	public PartnerInstanceType getType() {
		return type;
	}

	public void setType(PartnerInstanceType type) {
		this.type = type;
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
		if (!(obj instanceof PartnerInstanceTypeEnum))
			return false;
		PartnerInstanceTypeEnum objType = (PartnerInstanceTypeEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public PartnerInstanceTypeEnum(String code, String desc, PartnerInstanceType type) {
		this.code = code;
		this.desc = desc;
		this.type = type;
	}

	public static PartnerInstanceTypeEnum valueof(String code) {
		if (code == null)
			return null;
		return mappings.get(code);
	}

	@SuppressWarnings("unused")
	private PartnerInstanceTypeEnum() {

	}
	
	public enum PartnerInstanceType {
		TP, TPA, TPV;
	}
}


