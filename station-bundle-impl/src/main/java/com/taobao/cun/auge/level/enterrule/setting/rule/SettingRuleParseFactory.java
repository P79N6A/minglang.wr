package com.taobao.cun.auge.level.enterrule.setting.rule;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SettingRuleParseFactory implements ApplicationContextAware{
	
	private ApplicationContext applicationContext;
	
	public SettingRuleParse getSettingRuleParse(String name) {
		return applicationContext.getBean(name, SettingRuleParse.class);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
