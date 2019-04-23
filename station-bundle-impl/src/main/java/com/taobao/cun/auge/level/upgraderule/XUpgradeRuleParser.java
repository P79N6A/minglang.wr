package com.taobao.cun.auge.level.upgraderule;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.level.dto.TownLevelDto;
import com.taobao.cun.auge.level.settingrule.rule.RuleResult;
import com.taobao.cun.auge.station.service.PartnerInstanceQueryService;

@Component("xupgradeRuleParser")
public class XUpgradeRuleParser implements UpgradeRuleParser {
	@Resource
	private PartnerInstanceQueryService partnerInstanceQueryService;
	
	@Override
	public List<RuleResult> doParse(TownLevelDto townLevelDto, Long stationId) {
		return Lists.newArrayList(new RuleResult("CLOSE", "该镇为超标镇，暂不支持升级到天猫优品电器体验店的流程"));
	}

}
