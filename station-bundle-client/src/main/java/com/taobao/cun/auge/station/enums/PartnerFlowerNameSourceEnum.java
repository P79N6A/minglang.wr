package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PartnerFlowerNameSourceEnum implements Serializable{
	private static final long serialVersionUID = 1L;
	public static final PartnerFlowerNameSourceEnum poetry = new PartnerFlowerNameSourceEnum("poetry", "诗词歌赋");
	public static final PartnerFlowerNameSourceEnum novel = new PartnerFlowerNameSourceEnum("novel", "武侠小说");
	public static final PartnerFlowerNameSourceEnum self = new PartnerFlowerNameSourceEnum("self", "自由指定");

	private static final Map<String, PartnerFlowerNameSourceEnum> mappings = new HashMap<String, PartnerFlowerNameSourceEnum>();

	static {
		mappings.put("poetry", poetry);
		mappings.put("novel", novel);
		mappings.put("self", self);
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
		if (!(obj instanceof OperatorTypeEnum)) {
            return false;
        }
		OperatorTypeEnum objType = (OperatorTypeEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public PartnerFlowerNameSourceEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static PartnerFlowerNameSourceEnum valueof(String code) {
		if (code == null) {
            return null;
        }
		return mappings.get(code);
	}

	public PartnerFlowerNameSourceEnum() {

	}
}
