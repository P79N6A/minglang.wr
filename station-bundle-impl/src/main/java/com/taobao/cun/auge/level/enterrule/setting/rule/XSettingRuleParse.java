package com.taobao.cun.auge.level.enterrule.setting.rule;

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

/**
 * 超标镇准入规则
 * 
 * @author chengyu.zhoucy
 *
 */
@Component("xsettingRuleParse")
public class XSettingRuleParse implements SettingRuleParse {
	private static final String MESSAGE = "'该镇为超标镇，' + (#storeNum>0?('已开' + #storeNum + '家天猫优品电器体验店;'):'') + (#transingStoreNum>0?('有一家正在升级为天猫优品电器体验店的站点;'):'')";
	@Resource
	private StationLevelExtMapper stationLevelExtMapper;
	
	@Override
	public List<RuleResult> doParse(TownLevelDto townLevelDto, TownLevelStationRuleDto townLevelStationRuleDto) {
		//优品体验店数
		int storeNum = stationLevelExtMapper.countTownTPS(townLevelDto.getTownCode());
		//转型、升级中的优品体验店数
		int transingStoreNum = stationLevelExtMapper.countTransTPS(townLevelDto.getTownCode());
		
		if(storeNum + transingStoreNum > 0) {
			Map<String, Object> param = Maps.newHashMap();
			param.put("storeNum", storeNum);
			param.put("transingStoreNum", transingStoreNum);
			return Lists.newArrayList(new RuleResult("CLOSE", MessageHelper.rend(MESSAGE, param)));
		}else {
			return Lists.newArrayList(new RuleResult(townLevelStationRuleDto.getStationTypeCode(), townLevelStationRuleDto.getStationTypeDesc()));
		}
	}
}
