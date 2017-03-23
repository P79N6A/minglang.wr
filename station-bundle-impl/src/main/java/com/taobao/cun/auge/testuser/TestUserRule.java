package com.taobao.cun.auge.testuser;

import java.util.Map;

public interface TestUserRule {

	public boolean checkTestUser(Long taobaoUserId,Map<String,String> config);
	
	boolean isMatch(Map<String,String> config);
	
	public String getConfigKey();
}
