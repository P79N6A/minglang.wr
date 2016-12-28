package com.taobao.cun.auge.station.rule;

public class PartnerLifecycleRuleItem {

	private String value;
	private boolean equal;

	public PartnerLifecycleRuleItem() {
	}

	public PartnerLifecycleRuleItem(String value, boolean equal) {
		this.value = value;
		this.equal = equal;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean getEqual() {
		return equal;
	}

	public void setEqual(boolean equal) {
		this.equal = equal;
	}

}
