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

@Component("aupgradeRuleParser")
public class AUpgradeRuleParser extends AbstractUpgradeRuleParser {
	private static final String MESSAGE = "'该镇为A镇，' + (#storeNum>0?('已开' + #storeNum + '家天猫优品体验店;'):'') + (#tpElecNum>0?('已开' + #tpElecNum + '家天猫优品服务站(电器合作店);'):'') + (#transingHzdNum>0?('有' + #transingHzdNum + '家正在升级为天猫优品服务站(电器合作店)的站点;'):'')";
	@Resource
	private StationLevelExtMapper stationLevelExtMapper;
	@Override
	public List<RuleResult> doParse(TownLevelDto townLevelDto, Long stationId) {
		PartnerApplyConfirmIntentionEnum type = typeConvert(stationId);
		//如果该站点是普通服务站或者天猫优品服务站，那么可以升级
		if(isStation(type) || isTPYOUPIN(type)) {
			return checkNum(townLevelDto);
		}else {
			return Lists.newArrayList(new RuleResult("CLOSE", "该站点类型为：" + type.getDesc() + "不能升级"));
		}
	}

	/**
	 * 检查站点数是否满足一镇一店条件
	 * @param townLevelDto
	 * @return
	 */
	private List<RuleResult> checkNum(TownLevelDto townLevelDto) {
		int storeNum = stationLevelExtMapper.countTownTPS(townLevelDto.getTownCode());
		int tpElecNum = stationLevelExtMapper.countTownHZD(townLevelDto.getTownCode());
		int transingHzdNum = stationLevelExtMapper.countTransHZD(townLevelDto.getTownCode());
		if(storeNum + tpElecNum + transingHzdNum == 0) {
			//目前只能升级到合作店，暂不支持升级到体验店
			PartnerApplyConfirmIntentionEnum tpElec = PartnerApplyConfirmIntentionEnum.TP_ELEC;
			return Lists.newArrayList(
					new RuleResult(tpElec.getCode(), tpElec.getDesc()));
		}else {
			Map<String, Object> param = Maps.newHashMap();
			param.put("storeNum", storeNum);
			param.put("tpElecNum", tpElecNum);
			param.put("transingHzdNum", transingHzdNum);
			return Lists.newArrayList(new RuleResult("CLOSE", MessageHelper.rend(MESSAGE, param)));
		}
	}
}
