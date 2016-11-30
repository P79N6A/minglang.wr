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
	public static final ProcessBusinessEnum stationForcedClosure = new ProcessBusinessEnum("stationForcedClosure","申请停业");
	public static final ProcessBusinessEnum stationQuitRecord = new ProcessBusinessEnum("stationQuitRecord", "申请退出");
	public static final ProcessBusinessEnum SHUT_DOWN_STATION = new ProcessBusinessEnum("SHUT_DOWN_STATION", "申请撤点");
	public static final ProcessBusinessEnum partnerInstanceLevelAudit = new ProcessBusinessEnum("partnerInstanceLevelAudit", "合伙人层级审批");
	public static final ProcessBusinessEnum TPV_CLOSE = new ProcessBusinessEnum("TPV_CLOSE", "申请停业");
	public static final ProcessBusinessEnum TPV_QUIT = new ProcessBusinessEnum("TPV_QUIT", "申请退出");
	
	static {
		mappings.put("stationForcedClosure", stationForcedClosure);
		mappings.put("stationQuitRecord", stationQuitRecord);
		mappings.put("SHUT_DOWN_STATION", SHUT_DOWN_STATION);
		mappings.put("partnerInstanceLevelAudit", partnerInstanceLevelAudit);
		mappings.put("TPV_CLOSE", TPV_CLOSE);
		mappings.put("TPV_QUIT", TPV_QUIT);
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
