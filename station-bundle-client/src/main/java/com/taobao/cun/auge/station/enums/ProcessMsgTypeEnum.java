package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ProcessMsgTypeEnum implements Serializable {

	private static final long serialVersionUID = -61983678579072096L;

	private static final Map<String, ProcessMsgTypeEnum> mappings = new HashMap<String, ProcessMsgTypeEnum>();

	private String code;
	private String desc;
	
	public static final ProcessMsgTypeEnum PROC_INST_START = new ProcessMsgTypeEnum("PROC_INST_START","流程实例启动");
	public static final ProcessMsgTypeEnum PROC_INST_FINISH = new ProcessMsgTypeEnum("PROC_INST_FINISH","流程实例完成");
	public static final ProcessMsgTypeEnum PROC_INST_TERMINATE = new ProcessMsgTypeEnum("PROC_INST_TERMINATE","流程实例终止");	
	public static final ProcessMsgTypeEnum ACT_INST_START = new ProcessMsgTypeEnum("ACT_INST_START","流程节点被激活");
	public static final ProcessMsgTypeEnum TASK_ACTIVATED = new ProcessMsgTypeEnum("TASK_ACTIVATED", "任务被激活");
	public static final ProcessMsgTypeEnum TASK_COMPLETED = new ProcessMsgTypeEnum("TASK_COMPLETED", "任务完成");
	

	static {
		mappings.put("PROC_INST_START", PROC_INST_START);
		mappings.put("PROC_INST_FINISH", PROC_INST_FINISH);
		mappings.put("ACT_INST_START", ACT_INST_START);
		mappings.put("TASK_ACTIVATED", TASK_ACTIVATED);
		mappings.put("TASK_COMPLETED", TASK_COMPLETED);
	}

	public ProcessMsgTypeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public ProcessMsgTypeEnum() {

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

	public static ProcessMsgTypeEnum valueof(String code) {
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
		ProcessMsgTypeEnum other = (ProcessMsgTypeEnum) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}
}