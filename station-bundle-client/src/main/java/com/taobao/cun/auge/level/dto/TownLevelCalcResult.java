package com.taobao.cun.auge.level.dto;

import java.io.Serializable;

public class TownLevelCalcResult implements Serializable {

	private static final long serialVersionUID = 6539379567059700421L;

	private TownLevelDto townLevel;
	
	private TownLevelRuleDto townLevelRule;

	public TownLevelCalcResult() {}
	
	public TownLevelCalcResult(TownLevelDto townLevel, TownLevelRuleDto townLevelRule) {
		this.townLevel = townLevel;
		this.townLevelRule = townLevelRule;
	}
	
	public TownLevelDto getTownLevel() {
		return townLevel;
	}

	public void setTownLevel(TownLevelDto townLevel) {
		this.townLevel = townLevel;
	}

	public TownLevelRuleDto getTownLevelRule() {
		return townLevelRule;
	}

	public void setTownLevelRule(TownLevelRuleDto townLevelRule) {
		this.townLevelRule = townLevelRule;
	}
}
