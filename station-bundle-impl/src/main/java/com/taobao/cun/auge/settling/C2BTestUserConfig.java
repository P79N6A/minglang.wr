package com.taobao.cun.auge.settling;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.common.TestUserConfig;

@RefreshScope
@Component
public class C2BTestUserConfig implements TestUserConfig{

	
	@Value("#{'${c2bTestUserType}'.split(',')}")
	private List<String> testUserTypes;
	
	@Value("#{'${c2bTestTaobaoUserIds}'.split(',')}")
	private List<Long> testTaobaoUserIds;
	
	@Value("#{'${c2bTestOrgIds}'.split(',')}")
	private List<Long> testOrgIds;
	

	public List<String> getTestUserTypes() {
		return testUserTypes;
	}

	public void setTestUserTypes(List<String> testUserTypes) {
		this.testUserTypes = testUserTypes;
	}

	public List<Long> getTestTaobaoUserIds() {
		return testTaobaoUserIds;
	}

	public void setTestTaobaoUserIds(List<Long> testTaobaoUserIds) {
		this.testTaobaoUserIds = testTaobaoUserIds;
	}

	public List<Long> getTestOrgIds() {
		return testOrgIds;
	}

	public void setTestOrgIds(List<Long> testOrgIds) {
		this.testOrgIds = testOrgIds;
	}

	
	
}
