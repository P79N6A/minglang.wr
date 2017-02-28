package com.taobao.cun.auge.testuser;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

/**
 * Created by zhenhuan.zhangzh on 17/2/28.
 */

@Service("testUserService")
@HSFProvider(serviceInterface= TestUserService.class)
@EnableConfigurationProperties({TestUserProperties.class})
public class TestUserServiceImpl implements TestUserService,ApplicationContextAware{
	
	private ApplicationContext applicationContext;
	@Autowired
	private TestUserProperties testUserProperties;
	
    @Override
    public boolean isTestUser(Long taobaoUserId, String bizCode,boolean allMatch) {
    	Assert.notNull(taobaoUserId);
    	Assert.notNull(bizCode);
    	Map<String,String> config= testUserProperties.getConfigs().get(bizCode);
    	Map<String,TestUserRule> testUserRules = applicationContext.getBeansOfType(TestUserRule.class);
    	List<TestUserRule> rules = testUserRules.values().stream().filter(rule -> config.containsKey(rule.getConfigKey()) && StringUtils.isNotEmpty(config.get(rule.getConfigKey()))).collect(Collectors.toList());
    	if(CollectionUtils.isEmpty(rules)){
    		return false;
    	}
    	if(allMatch){
    		return rules.stream().allMatch(rule -> rule.checkTestUser(taobaoUserId, config.get(rule.getConfigKey())));
    	}else{
    		return  rules.stream().anyMatch(rule -> rule.checkTestUser(taobaoUserId, config.get(rule.getConfigKey())));
    	}
    }

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		
	}
    
}
