package com.taobao.cun.auge.testuser;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.google.common.collect.Maps;

@Component
@EnableConfigurationProperties({TestUserProperties.class})
public class TestUserRuleMananger implements ApplicationContextAware{

	private ApplicationContext applicationContext;
	
	private static TestUserRuleMananger INSTANCE = null; 
	
	private Map<String,Map<String,DelegateTestUserRule>> rulesMap = Maps.newConcurrentMap();
	
	@Autowired
	private TestUserProperties testUserProperties;
	
	@PreDestroy
	public void destory(){
		testUserProperties.getConfigs().forEach((bizCode,configs) ->{
			configs.forEach((rule,config) -> this.unRegister(bizCode,rule));
		});
	}
	
	
	@PostConstruct
	public void init(){
		testUserProperties.getConfigs().forEach((bizCode,configs) ->{
			configs.forEach((rule,config) -> this.register(bizCode,rule));
		});
	}
	
	public void register(String bizCode,String rule){
		TestUserRule testUserRule = applicationContext.getBean(StringUtils.remove(rule, "!"),TestUserRule.class);
		Assert.notNull(testUserRule);
		Map<String,DelegateTestUserRule> rules = rulesMap.get(bizCode);
		if(rules == null){
			rules = Maps.newConcurrentMap();
			rulesMap.put(bizCode, rules);
		}
		rules.put(rule, DelegateTestUserRule.of(() -> {
			return testUserProperties.getTestUserProperty(bizCode, rule);
		},rule.startsWith("!"), testUserRule));
	}

	public boolean run(String bizCode,Long taobaoUserId,Boolean allMatch){
		Map<String,DelegateTestUserRule> rules = rulesMap.get(bizCode);
		Assert.notNull(rules);
		if(allMatch){
    		return rules.values().stream().allMatch(rule -> rule.checkTestUser(taobaoUserId));
    	}else{
    		return  rules.values().stream().anyMatch(rule -> rule.checkTestUser(taobaoUserId));
    	}
	}
	
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	
	public void unRegister(String bizCode,String rule){
		Map<String,DelegateTestUserRule> rules = rulesMap.get(bizCode);
		if(rules == null){
			return;
		}
		rules.remove(rule);
	}
	
	
	public static TestUserRuleMananger getInstance() {
		return INSTANCE;
	}
}
