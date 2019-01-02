package com.taobao.cun.auge.level.bo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import com.taobao.cun.auge.common.utils.BeanCopy;
import com.taobao.cun.auge.dal.domain.TownLevelStationRuleExample;
import com.taobao.cun.auge.dal.mapper.TownLevelStationRuleMapper;
import com.taobao.cun.auge.level.dto.TownLevelDto;
import com.taobao.cun.auge.level.dto.TownLevelStationRuleDto;

/**
 * 解析镇域开点规则
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class TownLevelStationEnterResolver implements InitializingBean{
	@Resource
	private TownLevelStationRuleMapper townLevelStationRuleMapper;
	
	private LoadingCache<String, Map<String, TownLevelStationRuleDto>> townLevelStationRuleGroupByAreaCode;
	
	TownLevelStationRuleDto resolve(TownLevelDto townLevelDto) {
		return getTownLevelStationRuleDtos(townLevelDto);
	}
	
	/**
	 * 解析镇域开点规则
	 * 首先根据区域code获取是否有匹配得到的规则，按照镇->县->市->省的优先级排列，然后是默认规则
	 * @param townLevelDto
	 * @return
	 */
	private TownLevelStationRuleDto getTownLevelStationRuleDtos(TownLevelDto townLevelDto) {
		Map<String, TownLevelStationRuleDto> townLevelStationRuleGroupByAreaCodeMap;
		try {
			townLevelStationRuleGroupByAreaCodeMap = townLevelStationRuleGroupByAreaCode.get("RULES");
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
		initCache();
	}
	
	private void initCache() {
		townLevelStationRuleGroupByAreaCode = CacheBuilder
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
		List<TownLevelStationRuleDto> townLevelStationRuleDtos = BeanCopy.copyList(TownLevelStationRuleDto.class, townLevelStationRuleMapper.selectByExample(new TownLevelStationRuleExample()));
		Map<String, TownLevelStationRuleDto> townLevelStationRuleGroupByAreaCodeMap = Maps.newHashMap();
		townLevelStationRuleDtos.forEach(townLevelStationRuleDto->{
			townLevelStationRuleGroupByAreaCodeMap.put(townLevelStationRuleDto.getAreaCode(), townLevelStationRuleDto);
		});
		return townLevelStationRuleGroupByAreaCodeMap;
	}
}
