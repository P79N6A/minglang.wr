package com.taobao.cun.auge.level.upgraderule;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.taobao.cun.auge.dal.mapper.ext.StationLevelExtMapper;
import com.taobao.cun.auge.level.dto.TownLevelDto;
import com.taobao.cun.auge.level.settingrule.rule.RuleResult;
import com.taobao.cun.auge.level.utils.MessageHelper;
import com.taobao.cun.recruit.partner.enums.PartnerApplyConfirmIntentionEnum;

@Component("bupgradeRuleParser")
public class BUpgradeRuleParser extends AbstractUpgradeRuleParser {
	private static final String MESSAGE = "'该镇为B镇，' + (#storeNum>0?('已开' + #storeNum + '家天猫优品体验店;'):'') + (#tpElecNum>0?('已开' + #tpElecNum + '家天猫优品服务站(电器合作店);'):'') + (#youpinNum>0?('已开' + #youpinNum + '家天猫优品服务站;'):'') + (#transingYoupinNum>0?('有' + #transingYoupinNum + '家正在升级为天猫优品服务站的站点;'):'') + (#transingHzdNum>0?('有' + #transingHzdNum + '家正在升级为天猫优品服务站(电器合作店)的站点;'):'')";
	@Resource
	private StationLevelExtMapper stationLevelExtMapper;
	@Override
	public List<RuleResult> doParse(TownLevelDto townLevelDto, Long stationId) {
		PartnerApplyConfirmIntentionEnum type = typeConvert(stationId);
		//如果该站点是普通服务站或者天猫优品服务站，那么可以升级
		if(isStation(type)) {
			return stationNumRule(townLevelDto);
		}else if(isTPYOUPIN(type)) {
			return youpinNumRule(townLevelDto);
		}else {
			return Lists.newArrayList(new RuleResult("CLOSE", "该站点类型为：" + type.getDesc() + "不能升级"));
		}
	}

	/**
	 * 农村淘宝服务站可以升级到合作店、优品服务站
	 * 如果已经有了体验店、合作店、优品服务站或者存在转型流程中的站点，都不能升级
	 * @param townLevelDto
	 * @return
	 */
	private List<RuleResult> stationNumRule(TownLevelDto townLevelDto) {
		int storeNum = stationLevelExtMapper.countTownTPS(townLevelDto.getTownCode());
		int tpElecNum = stationLevelExtMapper.countTownHZD(townLevelDto.getTownCode());
		int youpinNum = stationLevelExtMapper.countTownYoupin(townLevelDto.getTownCode());
		int transingHzdNum = stationLevelExtMapper.countTransHZD(townLevelDto.getTownCode());
		int transingYoupinNum = stationLevelExtMapper.countTransYoupin(townLevelDto.getTownCode());
		if(storeNum + tpElecNum + youpinNum + transingHzdNum + transingYoupinNum == 0) {
			PartnerApplyConfirmIntentionEnum tpElec = PartnerApplyConfirmIntentionEnum.TP_ELEC;
			PartnerApplyConfirmIntentionEnum youpinElec = PartnerApplyConfirmIntentionEnum.TP_YOUPIN;
			return Lists.newArrayList(new RuleResult(tpElec.getCode(), tpElec.getDesc()), 
					new RuleResult(youpinElec.getCode(), youpinElec.getDesc()));
		}else {
			Map<String, Object> param = Maps.newHashMap();
			param.put("storeNum", storeNum);
			param.put("tpElecNum", tpElecNum);
			param.put("youpinNum", youpinNum);
			param.put("transingYoupinNum", transingYoupinNum);
			param.put("transingHzdNum", transingHzdNum);
			return Lists.newArrayList(new RuleResult("CLOSE", MessageHelper.rend(MESSAGE, param)));
		}
	}

	/**
	 * 优品服务站只能升级到电器合作店，如果已经存在了合作店、体验店、转型中的合作店就不能再升级了
	 * @param townLevelDto
	 * @return
	 */
	private List<RuleResult> youpinNumRule(TownLevelDto townLevelDto) {
		int storeNum = stationLevelExtMapper.countTownTPS(townLevelDto.getTownCode());
		int tpElecNum = stationLevelExtMapper.countTownHZD(townLevelDto.getTownCode());
		int transingHzdNum = stationLevelExtMapper.countTransHZD(townLevelDto.getTownCode());
		if(storeNum + tpElecNum + transingHzdNum == 0) {
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
