package com.taobao.cun.auge.level.enterrule.setting.rule;

import java.util.Map;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * 提示信息助手
 * 
 * @author chengyu.zhoucy
 *
 */
class MessageHelper {
	private static final ExpressionParser expressionParser = new SpelExpressionParser();
	
	static String rend(String el, Map<String, Object> param) {
		StandardEvaluationContext context = new StandardEvaluationContext();
		context.setVariables(param);
		return expressionParser.parseExpression(el).getValue(context, String.class);
	}
}
