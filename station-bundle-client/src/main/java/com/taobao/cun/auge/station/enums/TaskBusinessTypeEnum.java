package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TaskBusinessTypeEnum implements Serializable {

	private static final long serialVersionUID = -6355359207409909479L;

	private static final Map<String, TaskBusinessTypeEnum> mappings = new HashMap<String, TaskBusinessTypeEnum>();

	private String code;
	private String desc;

	public static final TaskBusinessTypeEnum STATION_APPLY_CONFIRM = new TaskBusinessTypeEnum("STATION_APPLY_CONFIRM",
			"停业流程");
	public static final TaskBusinessTypeEnum ALIPAY_ACQUIRE_TRADE_CLOSE = new TaskBusinessTypeEnum(
			"ALIPAY_ACQUIRE_TRADE_CLOSE", "退出流程");

	public static final TaskBusinessTypeEnum CUNTAO_CENTER_BPM_COMPLETE_TASK = new TaskBusinessTypeEnum(
			"CUNTAO_CENTER_BPM_COMPLETE_TASK", "停业流程");
	public static final TaskBusinessTypeEnum ALIPAY_ACQUIRE_TRADE_CANCLE = new TaskBusinessTypeEnum(
			"ALIPAY_ACQUIRE_TRADE_CANCLE", "退出流程");

	public static final TaskBusinessTypeEnum STATION_QUITE_CONFIRM = new TaskBusinessTypeEnum("STATION_QUITE_CONFIRM",
			"停业流程");
	public static final TaskBusinessTypeEnum TP_DEGRADE = new TaskBusinessTypeEnum("TP_DEGRADE", "退出流程");

	public static final TaskBusinessTypeEnum PARTNER_SMS = new TaskBusinessTypeEnum("PARTNER_SMS", "停业流程");
	public static final TaskBusinessTypeEnum STATION_QUIT_FLOW_TASK = new TaskBusinessTypeEnum("STATION_QUIT_FLOW_TASK",
			"退出流程");
	public static final TaskBusinessTypeEnum STATION_FORCE_FLOW_TASK = new TaskBusinessTypeEnum(
			"STATION_FORCE_FLOW_TASK", "退出流程");

	static {
		mappings.put("STATION_APPLY_CONFIRM", STATION_APPLY_CONFIRM);
		mappings.put("ALIPAY_ACQUIRE_TRADE_CLOSE", ALIPAY_ACQUIRE_TRADE_CLOSE);
		mappings.put("CUNTAO_CENTER_BPM_COMPLETE_TASK", CUNTAO_CENTER_BPM_COMPLETE_TASK);
		mappings.put("ALIPAY_ACQUIRE_TRADE_CANCLE", ALIPAY_ACQUIRE_TRADE_CANCLE);
		mappings.put("STATION_QUITE_CONFIRM", STATION_QUITE_CONFIRM);
		mappings.put("TP_DEGRADE", TP_DEGRADE);

		mappings.put("PARTNER_SMS", PARTNER_SMS);
		mappings.put("STATION_QUIT_FLOW_TASK", STATION_QUIT_FLOW_TASK);
		mappings.put("STATION_FORCE_FLOW_TASK", STATION_FORCE_FLOW_TASK);
	}

	public TaskBusinessTypeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public TaskBusinessTypeEnum() {

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

	public static TaskBusinessTypeEnum valueof(String code) {
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
		TaskBusinessTypeEnum other = (TaskBusinessTypeEnum) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}
}
