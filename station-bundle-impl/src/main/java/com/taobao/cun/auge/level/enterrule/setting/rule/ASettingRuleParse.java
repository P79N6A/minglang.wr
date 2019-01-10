package com.taobao.cun.auge.level.enterrule.setting.rule;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.taobao.cun.auge.dal.mapper.ext.StationLevelExtMapper;
import com.taobao.cun.auge.level.dto.TownLevelDto;
import com.taobao.cun.auge.level.dto.TownLevelStationRuleDto;
import com.taobao.cun.recruit.partner.enums.PartnerApplyConfirmIntentionEnum;

/**
 * A镇准入规则
 * 
 * @author chengyu.zhoucy
 *
 */
@Component("asettingRuleParse")
public class ASettingRuleParse implements SettingRuleParse {
	private static final String MESSAGE_1 = "'该镇为A镇，' + #storeNum>0?('已开' + #storeNum + '家天猫优品体验店;'):'' + #tpElecNum>0?('已开' + #tpElecNum + '家天猫优品体服务站(电器合作店);'):'' + #transingHzdNum>0?('有' + #transingHzdNum + '家正在升级为天猫优品体服务站(电器合作店)的站点;'):''";
	private static final String MESSAGE_2 = "'该镇为A镇(人口数大于6w)，' + #storeNum>0?('已开' + #storeNum + '家天猫优品体验店;'):'' + #tpElecNum>0?('已开' + #tpElecNum + '家天猫优品体服务站(电器合作店);'):'' + #youpinNum>0?('已开' + #youpinNum + '家天猫优品体服务站;'):'' + #transingHzdNum>0?('有' + #transingHzdNum + '家正在升级为天猫优品体服务站(电器合作店)的站点;'):'' + #youpinTransingNum>0?('有' + #youpinTransingNum + '家正在升级为天猫优品体服务站的站点;'):''";
	@Resource
	private StationLevelExtMapper stationLevelExtMapper;
	
	@Override
	public RuleResult doParse(TownLevelDto townLevelDto, TownLevelStationRuleDto townLevelStationRuleDto) {
		int storeNum = stationLevelExtMapper.countTownTPS(townLevelDto.getTownCode());
		int tpElecNum = stationLevelExtMapper.countTownHZD(townLevelDto.getTownCode());
		int transingHzdNum = stationLevelExtMapper.countTransHZD(townLevelDto.getTownCode());
		long population = townLevelDto.getTownPopulation();
		
		Map<String, Object> param = Maps.newHashMap();
		param.put("storeNum", storeNum);
		param.put("tpElecNum", tpElecNum);
		param.put("transingHzdNum", transingHzdNum);
		
		//如果没有体验店、合作店、转型中的合作店，那么可以开一家合作店
		if(storeNum + tpElecNum + transingHzdNum == 0) {
			return new RuleResult(townLevelStationRuleDto.getStationTypeCode(), townLevelStationRuleDto.getStationTypeDesc());
		}else if(storeNum + tpElecNum + transingHzdNum > 1) { //存在体验店、合作店、转型中的合作店，并且他们之和大于1了，那么就不能再开了
			return new RuleResult("CLOSE", MessageHelper.rend(MESSAGE_1, param));
		}else { //storeNum + tpElecNum + transingHzdNum == 1 如果有一家体验店、合作店、转型中的合作店，如果人口数大于6w，并且没有优品服务站或者转型中的优品服务站，那么可以再开一家优品服务站
			int youpinNum = stationLevelExtMapper.countTownYoupin(townLevelDto.getTownCode());
			int youpinTransingNum = stationLevelExtMapper.countTownYoupin(townLevelDto.getTownCode());
			if(population >= 60000) {
				if(youpinNum + youpinTransingNum == 0) {
					PartnerApplyConfirmIntentionEnum intention = PartnerApplyConfirmIntentionEnum.TP_YOUPIN;
					return new RuleResult(intention.getCode(), intention.getDesc());
				}else {
					param.put("youpinNum", youpinNum);
					param.put("youpinTransingNum", youpinTransingNum);
					return new RuleResult("CLOSE", MessageHelper.rend(MESSAGE_2, param));
				}
			}else {
				return new RuleResult("CLOSE", MessageHelper.rend(MESSAGE_1, param));
			}
		}
	}
}
