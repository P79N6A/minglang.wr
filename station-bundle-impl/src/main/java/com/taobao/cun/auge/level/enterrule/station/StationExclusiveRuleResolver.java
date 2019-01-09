package com.taobao.cun.auge.level.enterrule.station;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.google.common.base.Splitter;
import com.taobao.cun.auge.common.utils.BeanCopy;
import com.taobao.cun.auge.level.dto.TownLevelDto;
import com.taobao.cun.auge.level.dto.TownLevelStationRuleDto;
import com.taobao.cun.auge.level.enterrule.data.RuleDataBuilderFactory;
import com.taobao.cun.recruit.partner.enums.PartnerApplyConfirmIntentionEnum;


/**
 * 站点互斥规则，原则上是一镇一店
 * 但不同级的镇又有一些差别
 * 
 * @author chengyu.zhoucy
 *
 */
public class StationExclusiveRuleResolver {
	private static final String CLOSE = "CLOSE";
	@Resource
	private RuleDataBuilderFactory ruleDataBuilderFactory;
	
	private final ExpressionParser expressionParser = new SpelExpressionParser();
	/**
	 * 
	 * @param townLevelDto
	 * @param townLevelStationRuleDto
	 * @return
	 */
	public TownLevelStationRuleDto resolve(TownLevelDto townLevelDto, TownLevelStationRuleDto townLevelStationRuleDto) {
		StandardEvaluationContext context = new StandardEvaluationContext();
		context.setVariables(ruleDataBuilderFactory.getRuleDataBuilder(townLevelStationRuleDto.getRuleData()).build(townLevelDto));
		List<String> rules = Splitter.on("\n").omitEmptyStrings().trimResults().splitToList(townLevelStationRuleDto.getRule());
		
		String elResult = null;
		for(String rule : rules) {
			elResult = expressionParser.parseExpression(rule).getValue(context, String.class);
			if(!"NEXT".equals(elResult)) {
				break;
			}
		}
		
		TownLevelStationRuleDto result = BeanCopy.copy(TownLevelStationRuleDto.class, townLevelStationRuleDto);
		if(elResult.startsWith(CLOSE)) {
			result.setStationTypeCode(CLOSE);
			String[] array = elResult.split(":");
			result.setStationTypeDesc(array[1]);
		}else {
			PartnerApplyConfirmIntentionEnum intention = PartnerApplyConfirmIntentionEnum.valueof(elResult);
			result.setStationTypeCode(intention.getCode());
			result.setStationTypeDesc(intention.getDesc());
		}
		
		return result;
	}
}
