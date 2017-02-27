package com.taobao.cun.auge.common;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.org.service.CuntaoOrgServiceClient;

@Component
public class TestUserSupport{

	private static final ThreadLocal<TestUserContext> currentContext = new NamedThreadLocal<TestUserContext>("Current TestUserSupport");
	
	ExpressionParser parser = new SpelExpressionParser();
	
	@Autowired
	private CuntaoOrgServiceClient cuntaoOrgServiceClient;
	
	
	TestUserSupportOperator operator = new TestUserSupportOperator();
	private String getTestUserExpression(){
		TestUserContext context = currentContext();
		return context.getTestUserExpression();
	}
	
	private void setTestUserExpression(String expression){
		TestUserContext context = currentContext();
		context.setTestUserExpression(expression);
	}
	
	public class TestUserSupportOperator{
		
		
		public TestUserSupport and(){
			String innerExpression = getTestUserExpression();
			innerExpression = innerExpression+" &&";
			setTestUserExpression(innerExpression);
			return TestUserSupport.this;
		}
		
		public TestUserSupport or(){
			String innerExpression = getTestUserExpression();
			innerExpression = innerExpression+" ||";
			setTestUserExpression(innerExpression);
			return TestUserSupport.this;
		}
		
		public boolean getResult(){
			return TestUserSupport.this.getResult();
		}
		
	}
	

	
	
	public  TestUserSupportOperator isTestUserOrg(boolean include){
		try {
			TestUserContext context = currentContext();
			boolean result = context.isTestOrg(include);
			setExpression(result);
			return operator;
		} catch (Exception e) {
			currentContext.remove();
			throw new RuntimeException(e);
		}
		
	}
	
	public TestUserSupportOperator isTestTaobaoUser(boolean include){
		try {
			TestUserContext context = currentContext();
			boolean result =  context.isTestTaobaoUserId(include);
			setExpression(result);
			return operator;
		} catch (Exception e) {
			currentContext.remove();
			throw new RuntimeException(e);
		}
		
	}

	public TestUserSupportOperator isTestUserType(boolean include){
		try {
			TestUserContext context = currentContext();
			boolean result = context.isTestUserType(include);
			setExpression(result);
			return operator;
		} catch (Exception e) {
			currentContext.remove();
			throw new RuntimeException(e);
		}
		
	}
	
	private void setExpression(boolean result) {
		String innerExpression = getTestUserExpression();
		innerExpression = innerExpression+result;
		setTestUserExpression(innerExpression);
	}
	
	
	
	
	public  boolean getResult(){
		TestUserContext context = currentContext();
		currentContext.remove();
		String testUserExpression = context.getTestUserExpression();
		if(context.isUseCustomerUserExpression()){
			StandardEvaluationContext sc = new StandardEvaluationContext(context);
			return parser.parseExpression(testUserExpression).getValue(sc,boolean.class);
		}else{
			testUserExpression = StringUtils.removeEnd(testUserExpression, "&&");
			testUserExpression = StringUtils.removeEnd(testUserExpression, "||");
			return parser.parseExpression(testUserExpression).getValue(boolean.class);
		}
	}
	
	public  TestUserContext currentContext() throws IllegalStateException {
		TestUserContext context = currentContext.get();
		if (context == null) {
			throw new IllegalStateException(
					"Cannot find current testUserContext");
		}
		return context;
	}
	
	 public TestUserSupport setCurrentContext(TestUserContext context) {
		TestUserContext old = currentContext.get();
		if (context != null) {
			context.setCuntaoOrgServiceClient(cuntaoOrgServiceClient);
			currentContext.set(context);
		}
		else {
			currentContext.remove();
		}
		return this;
	}
}
