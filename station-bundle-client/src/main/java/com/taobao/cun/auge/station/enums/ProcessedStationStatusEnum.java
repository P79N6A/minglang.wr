package com.taobao.cun.auge.station.enums;

/**
 * Station状态在流程上的枚举
 * 
 * @author jiayi.zjb
 *
 */
public enum ProcessedStationStatusEnum {

	// 开业流程
	SUMITTED("SUMITTED", "待签约人确认"), CONFIRMED("CONFIRMED", "待缴纳保证金"), UNPAY_COURSE("UNPAY_COURSE", "待购买培训基金"), UNPAY_DECORATE("UNPAY_DECORATE", "待缴纳装修基金"), DECORATING("DECORATING","装修中"),UNSIGNED("UNSIGNED", "待培训签到"), DEC_WAIT_AUDIT("DEC_WAIT_AUDIT", "装修反馈待审核"), SERVICING("SERVICING",
			"已完成开业"),
	// // 退出流程
	QUIT_APPLYING("QUIT_APPLYING", "申请停业中"), QUIT_APPLY_CONFIRMED("QUIT_APPLY_CONFIRMED", "已停业"), QUITAUDITING("QUITAUDITING", "退出待审批"), CLOSED_WAIT_THAW("CLOSED_WAIT_THAW", "退出待解冻"), QUIT("QUIT", "已退出");

	ProcessedStationStatusEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	private String code;

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

	public static ProcessedStationStatusEnum of(String code) {
		for (ProcessedStationStatusEnum stationStatusEnum : ProcessedStationStatusEnum.values()) {
			if (code.equals(stationStatusEnum.getCode())) {
				return stationStatusEnum;
			}
		}
		return null;
	}

}
