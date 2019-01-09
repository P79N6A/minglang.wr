package com.taobao.cun.auge.level.bo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
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
import com.taobao.cun.auge.level.dto.TownLevelCalcResult;
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
	private static final String[] LEVELS = new String[] {"X", "A", "B"};
	@Resource
	private TownLevelRuleMapper townLevelRuleMapper;
	
	private LoadingCache<String, List<Map<String, TownLevelRuleDto>>> townLevelRuleGroupByLevelCache;
	
	private final ExpressionParser expressionParser = new SpelExpressionParser();
	
	TownLevelCalcResult levelResolve(TownLevelDto townLevelDto) {
		townLevelDto.setCoverageRate(calcCoverageRate(townLevelDto));
		StandardEvaluationContext context = new StandardEvaluationContext();
		try {
			context.setVariables(PropertyUtils.describe(townLevelDto));
		} catch (Exception e) {
			Throwables.propagateIfPossible(e);
		}
		
		List<TownLevelRuleDto> townLevelRuleDtos = getTownLevelRuleDtos(townLevelDto);
		
		String level = null;
		TownLevelRuleDto townLevelRuleDto = null;
		for(TownLevelRuleDto rule : townLevelRuleDtos) {
			if(expressionParser.parseExpression(rule.getLevelRule()).getValue(context, Boolean.class)) {
				level = rule.getLevel();
				townLevelRuleDto = rule;
				break;
			}
		}
		if(level == null) {
			throw new RuntimeException("规则不完整，id=" + townLevelDto.getId());
		}
		townLevelDto.setLevel(level);
		return new TownLevelCalcResult(townLevelDto, townLevelRuleDto);
	}
	
	/**
	 * 获取镇分层规则
	 * 首先根据区域code获取是否有匹配得到的规则，按照镇->县->市->省的优先级排列，然后是默认规则
	 * @param townLevelDto
	 * @return
	 */
	private List<TownLevelRuleDto> getTownLevelRuleDtos(TownLevelDto townLevelDto) {
		List<TownLevelRuleDto> townLevelRuleDtos = Lists.newArrayList();
		List<Map<String, TownLevelRuleDto>> townLevelRuleGroupByLevels;
		try {
			townLevelRuleGroupByLevels = townLevelRuleGroupByLevelCache.get("RULES");
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		}
		
		for(Map<String, TownLevelRuleDto> townLevelRuleGroupByLevel : townLevelRuleGroupByLevels) {
			if(townLevelRuleGroupByLevel.containsKey(townLevelDto.getTownCode())) {
				townLevelRuleDtos.add(townLevelRuleGroupByLevel.get(townLevelDto.getTownCode()));
			}else if(townLevelRuleGroupByLevel.containsKey(townLevelDto.getCountyCode())) {
				townLevelRuleDtos.add(townLevelRuleGroupByLevel.get(townLevelDto.getCountyCode()));
			}else if(townLevelRuleGroupByLevel.containsKey(townLevelDto.getCityCode())) {
				townLevelRuleDtos.add(townLevelRuleGroupByLevel.get(townLevelDto.getCityCode()));
			}else if(townLevelRuleGroupByLevel.containsKey(townLevelDto.getProvinceCode())) {
				townLevelRuleDtos.add(townLevelRuleGroupByLevel.get(townLevelDto.getProvinceCode()));
			}else {
				townLevelRuleDtos.add(townLevelRuleGroupByLevel.get("*"));
			}
		}
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
		townLevelRuleGroupByLevelCache = CacheBuilder
				.newBuilder()
				.expireAfterWrite(5*60, TimeUnit.SECONDS)
				.build(new CacheLoader<String, List<Map<String, TownLevelRuleDto>>>() {
            @Override
            public List<Map<String, TownLevelRuleDto>> load(String name) throws Exception {
                return loadRules();
            }
        });
	}
	
	/**
	 * 缓存的List结构是有顺序的，按照{@see LEVELS}的顺序构建Map
	 * 
	 * @return
	 */
	private List<Map<String, TownLevelRuleDto>> loadRules(){
		List<TownLevelRuleDto> townLevelRuleDtos = BeanCopy.copyList(TownLevelRuleDto.class, townLevelRuleMapper.selectByExample(new TownLevelRuleExample()));
		
		List<Map<String, TownLevelRuleDto>> townLevelRuleGroupByLevels = Lists.newArrayList();
		
		for(String level : LEVELS) {
			townLevelRuleGroupByLevels.add(getTownLevelRuleGroupByAreaCode(townLevelRuleDtos, level));
		}
		
		return townLevelRuleGroupByLevels;
	}
	
	/**
	 * 把相同层级的规则按照区域CODE为key放到一个Map里
	 * @param townLevelRuleDtos
	 * @param level
	 * @return
	 */
	private Map<String, TownLevelRuleDto> getTownLevelRuleGroupByAreaCode(List<TownLevelRuleDto> townLevelRuleDtos, String level){
		Map<String, TownLevelRuleDto> townLevelRuleGroupByAreaCode = Maps.newHashMap();
		townLevelRuleDtos.forEach(townLevelRuleDto->{
			if(level.equals(townLevelRuleDto.getLevel())) {
				townLevelRuleGroupByAreaCode.put(townLevelRuleDto.getAreaCode(), townLevelRuleDto);
			}
		});
		return townLevelRuleGroupByAreaCode;
	}
	
	public static void main(String[] argv) {
		ExpressionParser expressionParser = new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext();
		context.setVariable("storeNum", 1);
		context.setVariable("tpElecNum", 2);
		context.setVariable("population", 80000);
		System.out.println(expressionParser.parseExpression("(#storeNum==0 && #tpElecNum==0)?'TP_ELEC':'NEXT'").getValue(context, String.class));
		System.out.println(expressionParser.parseExpression("(#storeNum==0 && #tpElecNum>0 && #population>=60000)?'TP_YOUPIN':'NEXT'").getValue(context, String.class));
		System.out.println(expressionParser.parseExpression("(#storeNum + #tpElecNum >1)?'CLOSE':'NEXT'").getValue(context, String.class));
		System.out.println(expressionParser.parseExpression("#tpElecNum>1?'{\"CLOSE\":\"该镇有'+#tpElecNum+'个天猫优品电器合作店\"}':'NEXT'").getValue(context, String.class));
		System.out.println(expressionParser.parseExpression("'CLOSE:该地区禁止开优品体验店'").getValue(context, String.class));
	}
}
