package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
/**
 * 村点装修类型
 * @author yi.shaoy
 *
 */
public class StationDecorateTypeEnum implements Serializable{

	private static final long serialVersionUID = 1L;
	public static final StationDecorateTypeEnum NEW = new StationDecorateTypeEnum(
			"NEW", "新村点装修");
	public static final StationDecorateTypeEnum ORIGIN = new StationDecorateTypeEnum(
			"ORIGIN", "固点免装修");
	public static final StationDecorateTypeEnum ORIGIN_UPGRADE = new StationDecorateTypeEnum(
			"ORIGIN_UPGRADE", "固点翻新");

	private static final Map<String, StationDecorateTypeEnum> mappings = new HashMap<String, StationDecorateTypeEnum>();

	static {
		mappings.put("NEW", NEW);
		mappings.put("ORIGIN", ORIGIN);
		mappings.put("ORIGIN_UPGRADE", ORIGIN_UPGRADE);
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
		if (!(obj instanceof StationDecorateTypeEnum)) {
            return false;
        }
		StationDecorateTypeEnum objType = (StationDecorateTypeEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public StationDecorateTypeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static StationDecorateTypeEnum valueof(String code) {
		if (code == null) {
            return null;
        }
		return mappings.get(code);
	}

	@SuppressWarnings("unused")
	private StationDecorateTypeEnum() {

	}
}
