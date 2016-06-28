package com.taobao.cun.auge.station.rule;

import java.util.List;
import java.util.Map;

public class PartnerLifecycleExecutableMappingRule {

	private String item;
	private String desc;
	private List<Map<String, String>> executedCondition;
	private List<Map<String, String>> executableCondition;

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<Map<String, String>> getExecutedCondition() {
		return executedCondition;
	}

	public void setExecutedCondition(List<Map<String, String>> executedCondition) {
		this.executedCondition = executedCondition;
	}

	public List<Map<String, String>> getExecutableCondition() {
		return executableCondition;
	}

	public void setExecutableCondition(List<Map<String, String>> executableCondition) {
		this.executableCondition = executableCondition;
	}

}
