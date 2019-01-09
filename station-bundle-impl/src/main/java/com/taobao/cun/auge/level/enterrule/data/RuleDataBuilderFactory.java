package com.taobao.cun.auge.level.enterrule.data;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class RuleDataBuilderFactory implements ApplicationContextAware{
	
	private ApplicationContext applicationContext;
	
	public RuleDataBuilder getRuleDataBuilder(String name) {
		return applicationContext.getBean(name, RuleDataBuilder.class);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
