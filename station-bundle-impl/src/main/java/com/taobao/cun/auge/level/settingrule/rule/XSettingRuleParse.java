package com.taobao.cun.auge.level.settingrule.rule;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.taobao.cun.auge.station.service.UserFilterService;
import com.taobao.cun.recruit.partner.enums.PartnerApplyConfirmIntentionEnum;
import com.taobao.diamond.client.Diamond;
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
	@Resource
	private UserFilterService userFilterService;
	
	@Override
	public List<RuleResult> doParse(TownLevelDto townLevelDto, TownLevelStationRuleDto townLevelStationRuleDto) {
		//优品体验店数
		int storeNum = stationLevelExtMapper.countTownTPS(townLevelDto.getTownCode());
		//转型、升级中的优品体验店数
		int transingStoreNum = stationLevelExtMapper.countTransTPS(townLevelDto.getTownCode());

		if(storeNum + transingStoreNum > 0) {
			//已经有一家体验店
			if(storeNum + transingStoreNum == 1 && isInWhitename(townLevelDto)){
				//如果是超大城关镇
				if(isBigChengguanTown(townLevelDto)){
					PartnerApplyConfirmIntentionEnum e = PartnerApplyConfirmIntentionEnum.TPS_ELEC;
					return Lists.newArrayList(new RuleResult(e.getCode(), e.getDesc()));
				}
				//如果是超大镇
				if(isBigTown(townLevelDto)){
					PartnerApplyConfirmIntentionEnum store = PartnerApplyConfirmIntentionEnum.TPS_ELEC;
					PartnerApplyConfirmIntentionEnum hzd = PartnerApplyConfirmIntentionEnum.TP_ELEC;
					return Lists.newArrayList(new RuleResult(store.getCode(), store.getDesc()),
							new RuleResult(hzd.getCode(), hzd.getDesc()));
				}
			}
			Map<String, Object> param = Maps.newHashMap();
			param.put("storeNum", storeNum);
			param.put("transingStoreNum", transingStoreNum);
			return Lists.newArrayList(new RuleResult("CLOSE", MessageHelper.rend(MESSAGE, param)));
		}else {
			PartnerApplyConfirmIntentionEnum e = PartnerApplyConfirmIntentionEnum.TPS_ELEC;
			return Lists.newArrayList(new RuleResult(e.getCode(), e.getDesc()));
		}
	}

	private boolean isInWhitename(TownLevelDto townLevelDto){
		return userFilterService.isMatch("partner-tbid-whitename", townLevelDto.getTownCode());
	}

	private boolean isBigChengguanTown(TownLevelDto townLevelDto){
		return isBigTown(townLevelDto) && "Y".equalsIgnoreCase(townLevelDto.getChengguanTown());
	}

	private boolean isBigTown(TownLevelDto townLevelDto){
		return townLevelDto.getElecPredictionGmv() >= 1500 * 10000 || townLevelDto.getTownPopulation() >= 10 * 10000;
	}
}
