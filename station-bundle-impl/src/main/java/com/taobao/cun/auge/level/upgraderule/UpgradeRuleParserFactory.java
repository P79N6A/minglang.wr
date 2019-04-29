package com.taobao.cun.auge.level.upgraderule;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class UpgradeRuleParserFactory implements ApplicationContextAware{
	
	private ApplicationContext applicationContext;
	
	public UpgradeRuleParser getUpgradeRuleParser(String name) {
		return applicationContext.getBean(name, UpgradeRuleParser.class);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
