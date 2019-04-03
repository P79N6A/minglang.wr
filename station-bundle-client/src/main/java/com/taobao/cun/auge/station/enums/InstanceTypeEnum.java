package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;


public class InstanceTypeEnum implements Serializable {

	private static final long serialVersionUID = 7138576667532117709L;

	public static final InstanceTypeEnum TP = new InstanceTypeEnum("TP", "村小二");
	public static final InstanceTypeEnum TPA = new InstanceTypeEnum("TPA", "淘帮手");
	public static final InstanceTypeEnum TPV = new InstanceTypeEnum("TPV", "村拍档");
	public static final InstanceTypeEnum TPT = new InstanceTypeEnum("TPT", "镇小二");
	public static final InstanceTypeEnum TPS = new InstanceTypeEnum("TPS", "店小二");
	public static final InstanceTypeEnum UM = new InstanceTypeEnum("UM", "优盟");
	public static final InstanceTypeEnum LX = new InstanceTypeEnum("LX", "拉新");


	private static final Map<String, InstanceTypeEnum> mappings = new HashMap<String, InstanceTypeEnum>();
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
		if (!(obj instanceof InstanceTypeEnum)) {
            return false;
        }
		InstanceTypeEnum objType = (InstanceTypeEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public InstanceTypeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static InstanceTypeEnum valueof(String code) {
		if (code == null) {
            return null;
        }
		return mappings.get(code);
	}

	@SuppressWarnings("unused")
	private InstanceTypeEnum() {

	}
}


