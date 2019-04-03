package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

/**
 * 合伙人实例表 类型枚举
 * 
 *  这个类里 引用了枚举  会导致老版本用户反序列化失败 以后用PartnerInstanceTypeEnums
 *
 */
@Deprecated
public class PartnerInstanceTypeEnum implements Serializable {

	private static final long serialVersionUID = 7138576667532117709L;

	public static final PartnerInstanceTypeEnum TP = new PartnerInstanceTypeEnum("TP", "村小二", PartnerInstanceType.TP);
	public static final PartnerInstanceTypeEnum TPA = new PartnerInstanceTypeEnum("TPA", "淘帮手", PartnerInstanceType.TPA);
	public static final PartnerInstanceTypeEnum TPV = new PartnerInstanceTypeEnum("TPV", "村拍档", PartnerInstanceType.TPV);
	public static final PartnerInstanceTypeEnum TPT = new PartnerInstanceTypeEnum("TPT", "镇小二", PartnerInstanceType.TPT);
	public static final PartnerInstanceTypeEnum TPS = new PartnerInstanceTypeEnum("TPS", "店小二", PartnerInstanceType.TPS);
	public static final PartnerInstanceTypeEnum UM = new PartnerInstanceTypeEnum("UM", "优盟", PartnerInstanceType.UM);
	public static final PartnerInstanceTypeEnum LX = new PartnerInstanceTypeEnum("LX", "拉新伙伴", PartnerInstanceType.LX);


	private static final Map<String, PartnerInstanceTypeEnum> mappings = new HashMap<String, PartnerInstanceTypeEnum>();
	static {
		mappings.put("TPA", TPA);
		mappings.put("TP", TP);
		mappings.put("TPV", TPV);
		mappings.put("TPT", TPT);
		mappings.put("TPS", TPS);
		mappings.put("UM", UM);
		mappings.put("LX", LX);
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

	public static boolean isTpOrTps(String code) {
		return TP.code.equals(code) || TPS.code.equals(code);
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
		if (!(obj instanceof PartnerInstanceTypeEnum)) {
            return false;
        }
		PartnerInstanceTypeEnum objType = (PartnerInstanceTypeEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public PartnerInstanceTypeEnum(String code, String desc, PartnerInstanceType type) {
		this.code = code;
		this.desc = desc;
		this.type = type;
	}

	public static PartnerInstanceTypeEnum valueof(String code) {
		if (code == null) {
            return null;
        }
		return mappings.get(code);
	}

	@SuppressWarnings("unused")
	private PartnerInstanceTypeEnum() {

	}
	
	public enum PartnerInstanceType {
		TP, TPA, TPV,TPT,TPS,UM,LX;
	}
}


