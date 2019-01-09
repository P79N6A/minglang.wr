package com.taobao.cun.auge.level.enterrule.data;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.taobao.cun.auge.dal.mapper.ext.StationLevelExtMapper;
import com.taobao.cun.auge.level.dto.TownLevelDto;

/**
 * 超标镇规则数据构建
 * 
 * 查询超标镇天猫优品体验店的数量
 * 
 * @author chengyu.zhoucy
 *
 */
@Component("xruleDataBuilder")
public class XRuleDataBuilder implements RuleDataBuilder {
	@Resource
	private StationLevelExtMapper stationLevelExtMapper;
	
	@Override
	public Map<String, Object> build(TownLevelDto townLevelDto) {
		Map<String, Object> map = Maps.newHashMap();
		map.put("storeNum", stationLevelExtMapper.countTownTPS(townLevelDto.getTownCode()));
		return map;
	}

}
