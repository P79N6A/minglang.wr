package com.taobao.cun.auge.logistics.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class LogisticsStationStateEnum implements Serializable {

	private static final long serialVersionUID = -1197977282708260567L;

	public static final LogisticsStationStateEnum TO_AUDIT = new LogisticsStationStateEnum("TO_AUDIT", "待审核");
	public static final LogisticsStationStateEnum SERVICING = new LogisticsStationStateEnum("SERVICING", "服务中");
	public static final LogisticsStationStateEnum AUDIT_FAIL = new LogisticsStationStateEnum("AUDIT_FAIL", "审核不通过");
	public static final LogisticsStationStateEnum QUIT = new LogisticsStationStateEnum("QUIT", "已退出");
	private static final Map<String, LogisticsStationStateEnum> mappings = new HashMap<String, LogisticsStationStateEnum>();
	static {
		mappings.put("TO_AUDIT", TO_AUDIT);
		mappings.put("SERVICING", SERVICING);
		mappings.put("AUDIT_FAIL", AUDIT_FAIL);
		mappings.put("QUIT", QUIT);
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
		if (!(obj instanceof LogisticsStationStateEnum)) {
            return false;
        }
		LogisticsStationStateEnum objType = (LogisticsStationStateEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public LogisticsStationStateEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static LogisticsStationStateEnum valueof(String code) {
		if (code == null) {
            return null;
        }
		return mappings.get(code);
	}

	@SuppressWarnings("unused")
	private LogisticsStationStateEnum() {

	}
}