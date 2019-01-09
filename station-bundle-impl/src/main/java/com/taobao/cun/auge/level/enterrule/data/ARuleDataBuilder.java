package com.taobao.cun.auge.level.enterrule.data;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.taobao.cun.auge.dal.mapper.ext.StationLevelExtMapper;
import com.taobao.cun.auge.level.dto.TownLevelDto;

/**
 * A镇规则数据构建
 * 
 * @author chengyu.zhoucy
 *
 */
@Component("aruleDataBuilder")
public class ARuleDataBuilder implements RuleDataBuilder {
	@Resource
	private StationLevelExtMapper stationLevelExtMapper;
	
	@Override
	public Map<String, Object> build(TownLevelDto townLevelDto) {
		Map<String, Object> map = Maps.newHashMap();
		map.put("storeNum", stationLevelExtMapper.countTownTPS(townLevelDto.getTownCode()));
		map.put("tpElecNum", stationLevelExtMapper.countTownHZD(townLevelDto.getTownCode()));
		map.put("youpinNum", stationLevelExtMapper.countTownYoupin(townLevelDto.getTownCode()));
		map.put("population", townLevelDto.getTownPopulation());
		return map;
	}

}
