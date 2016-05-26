package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ProcessBusinessEnum implements Serializable {

	private static final long serialVersionUID = -3645487560971801482L;

	private static final Map<String, ProcessBusinessEnum> mappings = new HashMap<String, ProcessBusinessEnum>();

	private String code;
	private String desc;

	//FIXME 历史原因，没有办法大写，否则需要数据订正，适配已经内外已经存在的流程参数
	public static final ProcessBusinessEnum TPV_FORCED_CLOSURE = new ProcessBusinessEnum("TPV_FORCED_CLOSURE","村拍档强制停业");
	public static final ProcessBusinessEnum TPV_QUIT = new ProcessBusinessEnum("TPV_QUIT","村拍档退出");
	public static final ProcessBusinessEnum stationForcedClosure = new ProcessBusinessEnum("stationForcedClosure","村点强制清退");
	public static final ProcessBusinessEnum stationQuitRecord = new ProcessBusinessEnum("stationQuitRecord", "村点退出");

	static {
		mappings.put("stationForcedClosure", stationForcedClosure);
		mappings.put("stationQuitRecord", stationQuitRecord);
	}

	public ProcessBusinessEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public ProcessBusinessEnum() {

	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public static ProcessBusinessEnum valueof(String code) {
		if (code == null)
			return null;
		return mappings.get(code);
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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProcessBusinessEnum other = (ProcessBusinessEnum) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}
}
