package com.taobao.cun.auge.permission.operation.impl;

import java.util.Map;

import com.taobao.cun.auge.permission.operation.Operation;
import com.taobao.cun.auge.permission.operation.OperationData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class DataConditionMatcher implements OperationMatcher{
	
	private final ExpressionParser parser = new SpelExpressionParser();  

	@Override
	public boolean match(OperationData operationData, Operation operation) {
		if(StringUtils.isEmpty(operation.getCondition())) {
            return false;
        }
		StandardEvaluationContext context = new StandardEvaluationContext(operationData);
		Map<String,Object> variables = operationData.getProperties();
		if(variables !=null){
			context.setVariables(variables);
		}
		return parser.parseExpression(operation.getCondition()).getValue(context, Boolean.class); 
	}


}
