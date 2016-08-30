package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.validation.constraints.NotNull;

/**
 * Station状态在流程上的枚举
 * 
 * @author jiayi.zjb
 *
 */
public class ProcessedStationStatusEnum implements Serializable {

	private static final long serialVersionUID = 1161654899406405737L;
	// 开业流程
	public static final ProcessedStationStatusEnum SUMITTED = new ProcessedStationStatusEnum("SUMITTED", "待签约人确认");
	public static final ProcessedStationStatusEnum CONFIRMED = new ProcessedStationStatusEnum("CONFIRMED", "待缴纳保证金");
	public static final ProcessedStationStatusEnum UNPAY_COURSE = new ProcessedStationStatusEnum("UNPAY_COURSE", "待购买培训基金");
	public static final ProcessedStationStatusEnum UNPAY_DECORATE = new ProcessedStationStatusEnum("UNPAY_DECORATE", "待缴纳装修基金");
	public static final ProcessedStationStatusEnum UNSIGNED = new ProcessedStationStatusEnum("UNSIGNED", "待培训签到");
	public static final ProcessedStationStatusEnum DEC_WAIT_AUDIT = new ProcessedStationStatusEnum("DEC_WAIT_AUDIT", "装修反馈待审核");
	public static final ProcessedStationStatusEnum SERVICING = new ProcessedStationStatusEnum("SERVICING", "已完成开业");
	// 退出流程
	public static final ProcessedStationStatusEnum QUIT_APPLYING = new ProcessedStationStatusEnum("QUIT_APPLYING", "申请停业中");
	public static final ProcessedStationStatusEnum QUIT_APPLY_CONFIRMED = new ProcessedStationStatusEnum("QUIT_APPLY_CONFIRMED", "已停业");
	public static final ProcessedStationStatusEnum QUITAUDITING = new ProcessedStationStatusEnum("QUITAUDITING", "退出待审批");
	public static final ProcessedStationStatusEnum CLOSED_WAIT_THAW = new ProcessedStationStatusEnum("CLOSED_WAIT_THAW", "退出待解冻");
	public static final ProcessedStationStatusEnum QUIT = new ProcessedStationStatusEnum("QUIT", "已退出");

	private static final Map<String, ProcessedStationStatusEnum> mappings = new HashMap<String, ProcessedStationStatusEnum>();
	static {
		mappings.put("SUMITTED", SUMITTED);
		mappings.put("CONFIRMED", CONFIRMED);
		mappings.put("UNPAY_COURSE", UNPAY_COURSE);
		mappings.put("UNPAY_DECORATE", UNPAY_DECORATE);
		mappings.put("UNSIGNED", UNSIGNED);
		mappings.put("DEC_WAIT_AUDIT", DEC_WAIT_AUDIT);
		mappings.put("SERVICING", SERVICING);
		mappings.put("QUIT_APPLYING", QUIT_APPLYING);
		mappings.put("QUIT_APPLY_CONFIRMED", QUIT_APPLY_CONFIRMED);
		mappings.put("QUITAUDITING", QUITAUDITING);
		mappings.put("CLOSED_WAIT_THAW", CLOSED_WAIT_THAW);
		mappings.put("QUIT", QUIT);
	}
	
	@NotNull
	private String code;
	@NotNull
	private String desc;

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
		if (!(obj instanceof ProcessedStationStatusEnum))
			return false;
		ProcessedStationStatusEnum objType = (ProcessedStationStatusEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public ProcessedStationStatusEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static ProcessedStationStatusEnum valueof(String code) {
		if (code == null)
			return null;
		return mappings.get(code);
	}

	public static void main(String[] args) {
		String code2 = ProcessedStationStatusEnum.CONFIRMED.getDesc();
		System.out.println(code2);
	}
}
