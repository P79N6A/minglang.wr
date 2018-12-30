package com.taobao.cun.auge.level.bo;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.taobao.cun.auge.common.utils.BeanCopy;
import com.taobao.cun.auge.dal.domain.TownLevelRuleExample;
import com.taobao.cun.auge.dal.mapper.TownLevelRuleMapper;
import com.taobao.cun.auge.level.dto.TownLevelDto;
import com.taobao.cun.auge.level.dto.TownLevelRuleDto;

/**
 * 解析镇域分成等级
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class TownLevelResolver implements InitializingBean{
	@Resource
	private TownLevelRuleMapper townLevelRuleMapper;
	
	private LoadingCache<String, Map<String, List<TownLevelRuleDto>>> townLevelRuleGroupByAreaCodeCache;
	
	private final ExpressionParser expressionParser = new SpelExpressionParser();
	
	TownLevelDto levelResolve(TownLevelDto townLevelDto) {
		townLevelDto.setCoverageRate(calcCoverageRate(townLevelDto));
		StandardEvaluationContext context = new StandardEvaluationContext();
		try {
			context.setVariables(PropertyUtils.describe(townLevelDto));
		} catch (Exception e) {
			Throwables.propagateIfPossible(e);
		}
		
		List<TownLevelRuleDto> townLevelRuleDtos = getTownLevelRuleDtos(townLevelDto);
		
		String level = "C";
		for(TownLevelRuleDto rule : townLevelRuleDtos) {
			if(expressionParser.parseExpression(rule.getLevelRule()).getValue(context, Boolean.class)) {
				level = rule.getLevel();
				break;
			}
		}
		townLevelDto.setLevel(level);
		return townLevelDto;
	}
	
	/**
	 * 获取镇分层规则
	 * 首先根据区域code获取是否有匹配得到的规则，按照镇->县->市->省的优先级排列，然后是默认规则
	 * @param townLevelDto
	 * @return
	 */
	private List<TownLevelRuleDto> getTownLevelRuleDtos(TownLevelDto townLevelDto) {
		List<TownLevelRuleDto> townLevelRuleDtos = Lists.newArrayList();
		Map<String, List<TownLevelRuleDto>> townLevelRuleGroupByAreaCodeMap;
		try {
			townLevelRuleGroupByAreaCodeMap = townLevelRuleGroupByAreaCodeCache.get("RULES");
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		}
		townLevelRuleDtos.addAll(CollectionUtils.emptyIfNull(townLevelRuleGroupByAreaCodeMap.get(townLevelDto.getTownCode())));
		townLevelRuleDtos.addAll(CollectionUtils.emptyIfNull(townLevelRuleGroupByAreaCodeMap.get(townLevelDto.getCountyCode())));
		townLevelRuleDtos.addAll(CollectionUtils.emptyIfNull(townLevelRuleGroupByAreaCodeMap.get(townLevelDto.getCityCode())));
		townLevelRuleDtos.addAll(CollectionUtils.emptyIfNull(townLevelRuleGroupByAreaCodeMap.get(townLevelDto.getProvinceCode())));
		//默认规则
		townLevelRuleDtos.addAll(CollectionUtils.emptyIfNull(townLevelRuleGroupByAreaCodeMap.get("*")));
		return townLevelRuleDtos;
	}

	/**
	 * 计算手淘渗透率
	 * @param townLevelDto
	 * @return
	 */
	private int calcCoverageRate(TownLevelDto townLevelDto) {
		if(townLevelDto.getTownPopulation() == 0 || townLevelDto.getmTaobaoUserNum() == 0) {
			return 0;
		}
		
		long i = Math.round((townLevelDto.getmTaobaoUserNum() * 10000.0 / townLevelDto.getTownPopulation()));
		return Long.valueOf(i).intValue();
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		initCache();
	}
	
	private void initCache() {
		townLevelRuleGroupByAreaCodeCache = CacheBuilder
				.newBuilder()
				.expireAfterWrite(5*60, TimeUnit.SECONDS)
				.build(new CacheLoader<String, Map<String, List<TownLevelRuleDto>>>() {
            @Override
            public Map<String, List<TownLevelRuleDto>> load(String name) throws Exception {
                return loadRules();
            }
        });
	}
	
	private Map<String, List<TownLevelRuleDto>> loadRules(){
		List<TownLevelRuleDto> townLevelRuleDtos = BeanCopy.copyList(TownLevelRuleDto.class, townLevelRuleMapper.selectByExample(new TownLevelRuleExample()));
		Collections.sort(townLevelRuleDtos, new Comparator<TownLevelRuleDto>() {
			@Override
			public int compare(TownLevelRuleDto t1, TownLevelRuleDto t2) {
				return t1.getPriority() > t2.getPriority() ? 1 : -1;
			}
		});
		Map<String, List<TownLevelRuleDto>> townLevelRuleGroupByAreaCodeMap = Maps.newHashMap();
		townLevelRuleDtos.forEach(townLevelRuleDto->{
			List<TownLevelRuleDto> townLevelRuleGroupByAreaCode = null;
			if(!townLevelRuleGroupByAreaCodeMap.containsKey(townLevelRuleDto.getAreaCode())) {
				townLevelRuleGroupByAreaCode = Lists.newArrayList();
				townLevelRuleGroupByAreaCodeMap.put(townLevelRuleDto.getAreaCode(), townLevelRuleGroupByAreaCode);
			}else {
				townLevelRuleGroupByAreaCode = townLevelRuleGroupByAreaCodeMap.get(townLevelRuleDto.getAreaCode());
			}
			townLevelRuleGroupByAreaCode.add(townLevelRuleDto);
		});
		return townLevelRuleGroupByAreaCodeMap;
	}
}
