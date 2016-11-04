package com.taobao.cun.auge.event.enums;

public enum SyncStationApplyEnum {
	ADD("新增"), UPDATE_BASE("只更新instance、partner、station对应的信息"), UPDATE_STATE("只更新状态"), UPDATE_ALL("全部更新,包括钱和协议等"), DELETE("删除");

	private String desc;

	private SyncStationApplyEnum(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
