package com.taobao.cun.auge.level.enterrule.setting.rule;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.taobao.cun.auge.dal.mapper.ext.StationLevelExtMapper;
import com.taobao.cun.auge.level.dto.TownLevelDto;
import com.taobao.cun.auge.level.dto.TownLevelStationRuleDto;

/**
 * B镇准入规则
 * 
 * @author chengyu.zhoucy
 *
 */
@Component("bsettingRuleParse")
public class BSettingRuleParse implements SettingRuleParse {
	private static final String MESSAGE = "'该镇为B镇，' + #stationNum>0?('已开' + #stationNum + '家天猫优品服务站;'):'' + #transingYoupinNum>0?('有一家正在升级为天猫优品服务站的站点;'):''";
	@Resource
	private StationLevelExtMapper stationLevelExtMapper;
	
	@Override
	public RuleResult doParse(TownLevelDto townLevelDto, TownLevelStationRuleDto townLevelStationRuleDto) {
		//优品体验店数 + 合作店 + 优品服务站的总数
		int stationNum = stationLevelExtMapper.countTownStation(townLevelDto.getTownCode());
		//转型、升级中的优品服务站数
		int transingYoupinNum = stationLevelExtMapper.countTransYoupin(townLevelDto.getTownCode());
		
		if(stationNum + transingYoupinNum > 0) {
			Map<String, Object> param = Maps.newHashMap();
			param.put("stationNum", stationNum);
			param.put("transingYoupinNum", transingYoupinNum);
			return new RuleResult("CLOSE", MessageHelper.rend(MESSAGE, param));
		}else {
			return new RuleResult(townLevelStationRuleDto.getStationTypeCode(), townLevelStationRuleDto.getStationTypeDesc());
		}
	}
}
