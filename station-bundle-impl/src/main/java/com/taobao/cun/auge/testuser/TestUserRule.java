package com.taobao.cun.auge.testuser;

public interface TestUserRule {

	public boolean checkTestUser(Long taobaoUserId, String config);
	
	public String getConfigKey();
}
