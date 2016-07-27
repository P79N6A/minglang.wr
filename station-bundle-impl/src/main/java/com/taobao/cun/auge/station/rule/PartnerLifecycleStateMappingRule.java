package com.taobao.cun.auge.station.rule;

import java.util.Map;

/**
 * 
 * @author linjianke
 *
 */
public class PartnerLifecycleStateMappingRule {
	// 老模型station_apply状态
	private String stationApplyState;
	// 规则描述
	private String desc;
	// 新模型合伙人实例状态
	private String partnerInstanceState;
	// 新模型生命周期item条件，e.g. "roleApprove":"!TO_AUDIT"
	// 表示roleApprove不等于待审批(!开头表示不等于)
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
