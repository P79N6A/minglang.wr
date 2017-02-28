package com.taobao.cun.auge.testuser;

public interface TestUserRule {

	public boolean checkTestUser(Long taobaoUserId, String bizCode);
	
	public String getConfigKey();
}
