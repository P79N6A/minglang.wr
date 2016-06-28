package com.taobao.cun.auge.station.rule;

import java.util.Map;

public class PartnerLifecycleStateMappingRule {
	private String stationApplyState;
	private String partnerInstanceState;
	private String desc;
	private Map<String, String> condition;

	public String getStationApplyState() {
		return stationApplyState;
	}

	public void setStationApplyState(String stationApplyState) {
		this.stationApplyState = stationApplyState;
	}

	public String getPartnerInstanceState() {
		return partnerInstanceState;
	}

	public void setPartnerInstanceState(String partnerInstanceState) {
		this.partnerInstanceState = partnerInstanceState;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Map<String, String> getCondition() {
		return condition;
	}

	public void setCondition(Map<String, String> condition) {
		this.condition = condition;
	}

}
