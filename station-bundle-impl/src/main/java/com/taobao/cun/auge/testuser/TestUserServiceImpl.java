package com.taobao.cun.auge.testuser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

/**
 * Created by zhenhuan.zhangzh on 17/2/28.
 */

@Service("testUserService")
@HSFProvider(serviceInterface= TestUserService.class)
public class TestUserServiceImpl implements TestUserService{
	
	@Autowired
	private TestUserRuleMananger testUserRuleMananger;
	
    @Override
    public boolean isTestUser(Long taobaoUserId, String bizCode,boolean allMatch) {
    	Assert.notNull(taobaoUserId);
    	Assert.notNull(bizCode);
    	return testUserRuleMananger.run(bizCode, taobaoUserId, allMatch);
    }

    
}