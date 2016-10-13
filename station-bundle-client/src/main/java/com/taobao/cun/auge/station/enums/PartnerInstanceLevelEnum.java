package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PartnerInstanceLevelEnum implements Serializable {

	private static final long serialVersionUID = 8015723688747885707L;

	private PartnerInstanceLevel level;
	private String description;

	/**
	 * 依赖此枚举的ordinal 因此不需要改变枚举顺序
	 * 类PartnerInstanceLevelEnum.java的实现描述：TODO 类实现描述 
	 * @author xujianhui 2016年10月13日 下午4:03:21
	 */
	public enum PartnerInstanceLevel {
		S4, S5, S6, S7, S8,SP;
	}

	public static final PartnerInstanceLevelEnum S_4 = new PartnerInstanceLevelEnum(PartnerInstanceLevel.S4, "初级合伙人");
	public static final PartnerInstanceLevelEnum S_5 = new PartnerInstanceLevelEnum(PartnerInstanceLevel.S5, "中级合伙人");
	public static final PartnerInstanceLevelEnum S_6 = new PartnerInstanceLevelEnum(PartnerInstanceLevel.S6, "高级合伙人");
	public static final PartnerInstanceLevelEnum S_7 = new PartnerInstanceLevelEnum(PartnerInstanceLevel.S7, "优秀合伙人");
	public static final PartnerInstanceLevelEnum S_8 = new PartnerInstanceLevelEnum(PartnerInstanceLevel.S8, "明星合伙人");
	public static final PartnerInstanceLevelEnum S_P = new PartnerInstanceLevelEnum(PartnerInstanceLevel.SP, "待评定");


	private static final Map<PartnerInstanceLevel, PartnerInstanceLevelEnum> mappings = new HashMap<PartnerInstanceLevel, PartnerInstanceLevelEnum>();
	static {
		mappings.put(PartnerInstanceLevel.S4, S_4);
		mappings.put(PartnerInstanceLevel.S5, S_5);
		mappings.put(PartnerInstanceLevel.S6, S_6);
		mappings.put(PartnerInstanceLevel.S7, S_7);
		mappings.put(PartnerInstanceLevel.S8, S_8);
		mappings.put(PartnerInstanceLevel.SP, S_P);
	}

	public PartnerInstanceLevelEnum(PartnerInstanceLevel level, String description) {
		this.level = level;
		this.description = description;
	}

	public PartnerInstanceLevelEnum() {

	}

	public PartnerInstanceLevel getLevel() {
		return level;
	}

	public void setLevel(PartnerInstanceLevel level) {
		this.level = level;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((level == null) ? 0 : level.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof PartnerInstanceLevelEnum))
			return false;
		PartnerInstanceLevelEnum objType = (PartnerInstanceLevelEnum) obj;
		return objType.getLevel().equals(this.getLevel());
	}

	public static PartnerInstanceLevelEnum valueof(PartnerInstanceLevel level) {
		if (level == null)
			return null;
		return mappings.get(level);
	}

	public static PartnerInstanceLevelEnum valueof(String levelStr) {
		if (levelStr == null)
			return null;
		PartnerInstanceLevel level = PartnerInstanceLevel.valueOf(levelStr);
		if (null == level) {
			return null;
		}
		return mappings.get(level);
	}

}