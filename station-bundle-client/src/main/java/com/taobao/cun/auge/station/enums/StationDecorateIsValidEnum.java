package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务站装修表，isvalid枚举
 * @author quanzhu.wangqz
 *
 */
public class StationDecorateIsValidEnum  implements Serializable {
	
	private static final long serialVersionUID = -119918219648000754L;
	public static final StationDecorateIsValidEnum Y  = new StationDecorateIsValidEnum("Y", "有效");
	public static final StationDecorateIsValidEnum N = new StationDecorateIsValidEnum("N", "无效");
	
	private static final Map<String, StationDecorateIsValidEnum> mappings = new HashMap<String, StationDecorateIsValidEnum>();
	
	static {
		mappings.put("Y", Y);
		mappings.put("N", N);
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
		if (!(obj instanceof StationDecorateIsValidEnum))
			return false;
		StationDecorateIsValidEnum objType = (StationDecorateIsValidEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public StationDecorateIsValidEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static StationDecorateIsValidEnum valueof(String code) {
		if (code == null)
			return null;
		return mappings.get(code);
	}

	
	@SuppressWarnings("unused")
	private StationDecorateIsValidEnum() {

	}
}
