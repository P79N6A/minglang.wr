package com.taobao.cun.auge.level.stationrule;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import com.taobao.cun.auge.common.utils.BeanCopy;
import com.taobao.cun.auge.dal.domain.TownLevelStationRuleExample;
import com.taobao.cun.auge.dal.mapper.TownLevelStationRuleMapper;
import com.taobao.cun.auge.level.dto.TownLevelDto;
import com.taobao.cun.auge.level.dto.TownLevelStationRuleDto;

public class DefaultGradeRuleResolver implements GradeRuleResolver, InitializingBean {
	private LoadingCache<String, Map<String, TownLevelStationRuleDto>> loadingCache;
	@Resource
	private TownLevelStationRuleMapper townLevelStationRuleMapper;
	
	protected String level;
	
	protected DefaultGradeRuleResolver(String level) {
		this.level = level;
	}
	
	/**
	 * 解析镇域开点规则
	 * 首先根据区域code获取是否有匹配得到的规则，按照镇->县->市->省的优先级排列，然后是默认规则
	 * @param townLevelDto
	 * @return
	 */
	public TownLevelStationRuleDto getTownLevelStationRule(TownLevelDto townLevelDto) {
		Map<String, TownLevelStationRuleDto> townLevelStationRuleGroupByAreaCodeMap;
		try {
			townLevelStationRuleGroupByAreaCodeMap = loadingCache.get("RULES");
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		}
		if(townLevelStationRuleGroupByAreaCodeMap.containsKey(townLevelDto.getTownCode())) {
			return townLevelStationRuleGroupByAreaCodeMap.get(townLevelDto.getTownCode());
		}
		
		if(townLevelStationRuleGroupByAreaCodeMap.containsKey(townLevelDto.getCountyCode())) {
			return townLevelStationRuleGroupByAreaCodeMap.get(townLevelDto.getCountyCode());
		}
		
		if(townLevelStationRuleGroupByAreaCodeMap.containsKey(townLevelDto.getCityCode())) {
			return townLevelStationRuleGroupByAreaCodeMap.get(townLevelDto.getCityCode());
		}
		
		if(townLevelStationRuleGroupByAreaCodeMap.containsKey(townLevelDto.getProvinceCode())) {
			return townLevelStationRuleGroupByAreaCodeMap.get(townLevelDto.getProvinceCode());
		}
		//默认规则
		return townLevelStationRuleGroupByAreaCodeMap.get("*");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		loadingCache = CacheBuilder
				.newBuilder()
				.expireAfterWrite(5*60, TimeUnit.SECONDS)
				.build(new CacheLoader<String, Map<String, TownLevelStationRuleDto>>() {
            @Override
            public Map<String, TownLevelStationRuleDto> load(String name) throws Exception {
                return loadRules();
            }
        });
	}
	
	private Map<String, TownLevelStationRuleDto> loadRules(){
		TownLevelStationRuleExample example = new TownLevelStationRuleExample();
		example.createCriteria().andLevelEqualTo(level);
		List<TownLevelStationRuleDto> townLevelStationRuleDtos = BeanCopy.copyList(TownLevelStationRuleDto.class, townLevelStationRuleMapper.selectByExample(example));
		Map<String, TownLevelStationRuleDto> townLevelStationRuleGroupByAreaCodeMap = Maps.newHashMap();
		townLevelStationRuleDtos.forEach(townLevelStationRuleDto->{
			townLevelStationRuleGroupByAreaCodeMap.put(townLevelStationRuleDto.getAreaCode(), townLevelStationRuleDto);
		});
		return townLevelStationRuleGroupByAreaCodeMap;
	}
}
