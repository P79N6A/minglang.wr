package com.taobao.cun.auge.station.sync;

public enum SyncStationApplyEnum {
	ADD("新增"), BASE("只更新instance、partner、station对应的信息"), STATE("只更新状态"), ALL("全部更新,包括钱和协议等");

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
