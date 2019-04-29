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
		long population = townLevelDto.getTownPopulation();
		
		//如果没有体验店、合作店、转型中的合作店，那么可以开一家合作店或者体验店
		if(storeNum + tpElecNum + transingHzdNum == 0) {
			PartnerApplyConfirmIntentionEnum tpsElec = PartnerApplyConfirmIntentionEnum.TPS_ELEC;
			PartnerApplyConfirmIntentionEnum tpElec = PartnerApplyConfirmIntentionEnum.TP_ELEC;
			return Lists.newArrayList(
					new RuleResult(tpsElec.getCode(), tpsElec.getDesc()),
					new RuleResult(tpElec.getCode(), tpElec.getDesc()));
		}else if(storeNum + tpElecNum + transingHzdNum == 1 && population >= 60000){ //已经开了一家，但是人口数超过6w，可以再开一家电器合作店
			PartnerApplyConfirmIntentionEnum tpElec = PartnerApplyConfirmIntentionEnum.TP_ELEC;
			return Lists.newArrayList(new RuleResult(tpElec.getCode(), tpElec.getDesc()));
		}else {
			Map<String, Object> param = Maps.newHashMap();
			param.put("storeNum", storeNum);
			param.put("tpElecNum", tpElecNum);
			param.put("transingHzdNum", transingHzdNum);
			return Lists.newArrayList(new RuleResult("CLOSE", MessageHelper.rend(MESSAGE, param)));
		}
	}
}
