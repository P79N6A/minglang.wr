package com.taobao.cun.auge.station.rule;

import java.util.List;
import java.util.Map;

/**
 * 生命周期事项是否可执行的判断规则
 * 
 * @author linjianke
 *
 */
public class PartnerLifecycleExecutableMappingRule {
	// 判断事项，如SETTLING.settledProtocol
	private String item;
	// 描述
	private String desc;
	// 已执行的判断条件列表，只要满足其一即表示已执行
	private List<Map<String, String>> executedCondition;
	// 可执行的判断条件列表，只要满足其一即表示可执行，详见例子SETTLING.settledProtocol
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
