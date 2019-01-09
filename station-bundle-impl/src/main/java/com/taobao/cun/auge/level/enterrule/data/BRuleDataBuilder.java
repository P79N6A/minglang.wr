package com.taobao.cun.auge.level.enterrule.data;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.taobao.cun.auge.dal.mapper.ext.StationLevelExtMapper;
import com.taobao.cun.auge.level.dto.TownLevelDto;

/**
 * B镇规则数据构建
 * 
 * @author chengyu.zhoucy
 *
 */
@Component("bruleDataBuilder")
public class BRuleDataBuilder implements RuleDataBuilder {
	@Resource
	private StationLevelExtMapper stationLevelExtMapper;
	
	@Override
	public Map<String, Object> build(TownLevelDto townLevelDto) {
		Map<String, Object> map = Maps.newHashMap();
		map.put("stationNum", stationLevelExtMapper.countTownStation(townLevelDto.getTownCode()));
		return map;
	}

}
