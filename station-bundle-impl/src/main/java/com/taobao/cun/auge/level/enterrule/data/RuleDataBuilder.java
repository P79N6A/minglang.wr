package com.taobao.cun.auge.level.enterrule.data;

import java.util.Map;

import com.taobao.cun.auge.level.dto.TownLevelDto;

public interface RuleDataBuilder {
	Map<String, Object> build(TownLevelDto townLevelDto);
}
