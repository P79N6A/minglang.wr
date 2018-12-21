package com.taobao.cun.auge.level.bo;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.level.dto.TownLevelDto;

import jersey.repackaged.com.google.common.base.Throwables;

/**
 * 解析镇域分成等级
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class TownLevelResolver {
	private final ExpressionParser expressionParser = new SpelExpressionParser();
	
	static String X = "#coverageRate >= 3000 && #elecPredictionGmv >= 8000000";
	
	static String A = "(#coverageRate < 3000 && #elecPredictionGmv >= 8000000) || (#coverageRate == 0 && #taobaoGmv >= 4000000)";
	
	static String B = "(#elecPredictionGmv >= 4200000 && #elecPredictionGmv < 8000000) || (#coverageRate == 0 && #taobaoGmv >= 1700000 && #taobaoGmv < 4000000)";
	
	TownLevelDto levelResolve(TownLevelDto townLevelDto) {
		townLevelDto.setCoverageRate(calcCoverageRate(townLevelDto));
		StandardEvaluationContext context = new StandardEvaluationContext();
		try {
			context.setVariables(PropertyUtils.describe(townLevelDto));
		} catch (Exception e) {
			Throwables.propagateIfPossible(e);
		}
		
		if(expressionParser.parseExpression(X).getValue(context, Boolean.class)) {
			townLevelDto.setLevel("X");
		}else if(expressionParser.parseExpression(A).getValue(context, Boolean.class)) {
			townLevelDto.setLevel("A");
		}else if(expressionParser.parseExpression(B).getValue(context, Boolean.class)) {
			townLevelDto.setLevel("B");
		}else {
			townLevelDto.setLevel("C");
		}
		
		return townLevelDto;
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
		
		return Math.round(townLevelDto.getmTaobaoUserNum() * 10000 / townLevelDto.getTownPopulation());
	}
	
	public static void main(String[] argv) {
		TownLevelDto townLevelDto = new TownLevelDto();
		
		townLevelDto.setmTaobaoUserNum(3);
		townLevelDto.setTownPopulation(4L);
		townLevelDto.setElecPredictionGmv(5000000L);
		
		new TownLevelResolver().levelResolve(townLevelDto);
	}
}
