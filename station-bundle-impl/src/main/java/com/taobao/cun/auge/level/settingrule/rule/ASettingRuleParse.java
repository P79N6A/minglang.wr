package com.taobao.cun.auge.level.settingrule.rule;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.taobao.cun.auge.dal.mapper.ext.StationLevelExtMapper;
import com.taobao.cun.auge.level.dto.TownLevelDto;
import com.taobao.cun.auge.level.dto.TownLevelStationRuleDto;
import com.taobao.cun.auge.level.utils.MessageHelper;
import com.taobao.cun.recruit.partner.enums.PartnerApplyConfirmIntentionEnum;

/**
 * A镇准入规则
 * 
 * @author chengyu.zhoucy
 *
 */
@Component("asettingRuleParse")
public class ASettingRuleParse implements SettingRuleParse {
	private static final String MESSAGE = "'该镇为A镇，' + (#storeNum>0?('已开' + #storeNum + '家天猫优品体验店;'):'') + (#tpElecNum>0?('已开' + #tpElecNum + '家天猫优品服务站(电器合作店);'):'') + (#transingHzdNum>0?('有' + #transingHzdNum + '家正在升级为天猫优品服务站(电器合作店)的站点;'):'')";
	@Resource
	private StationLevelExtMapper stationLevelExtMapper;
	
	@Override
	public List<RuleResult> doParse(TownLevelDto townLevelDto, TownLevelStationRuleDto townLevelStationRuleDto) {
		int storeNum = stationLevelExtMapper.countTownTPS(townLevelDto.getTownCode());
		int tpElecNum = stationLevelExtMapper.countTownHZD(townLevelDto.getTownCode());
		int transingHzdNum = stationLevelExtMapper.countTransHZD(townLevelDto.getTownCode());
		
		//如果没有体验店、合作店、转型中的合作店，那么可以开一家合作店或者体验店
		if(storeNum + tpElecNum + transingHzdNum == 0) {
			PartnerApplyConfirmIntentionEnum intention = PartnerApplyConfirmIntentionEnum.TPS_ELEC;
			return Lists.newArrayList(
					new RuleResult(intention.getCode(), intention.getDesc()),
					new RuleResult(townLevelStationRuleDto.getStationTypeCode(), townLevelStationRuleDto.getStationTypeDesc()));
		}else {
			Map<String, Object> param = Maps.newHashMap();
			param.put("storeNum", storeNum);
			param.put("tpElecNum", tpElecNum);
			param.put("transingHzdNum", transingHzdNum);
			return Lists.newArrayList(new RuleResult("CLOSE", MessageHelper.rend(MESSAGE, param)));
		}
	}
}
