package com.taobao.cun.auge.common;

import java.util.List;

import org.springframework.util.Assert;

import com.taobao.cun.auge.org.dto.CuntaoOrgDto;
import com.taobao.cun.auge.org.service.CuntaoOrgServiceClient;

public class TestUserContext {

	private CuntaoOrgServiceClient cuntaoOrgServiceClient;
	
	private String userType;
	
	private Long orgId;
	
	private Long taobaoUserId;
	
	private TestUserConfig testUserConfig;
	
	private boolean isTestOrg;
	
    private boolean isTestUserTaobaoUser;
	
	private boolean isTestUserType;
	
	private boolean useCustomerUserExpression;
	
	private String testUserExpression="";
	
	public boolean isUseCustomerUserExpression() {
		return useCustomerUserExpression;
	}


	public void setUserCustomerUserExpression(boolean useCustomerUserExpression) {
		this.useCustomerUserExpression = useCustomerUserExpression;
	}


	public boolean isTestOrg() {
		return isTestOrg;
	}


	public void setTestOrg(boolean isTestOrg) {
		this.isTestOrg = isTestOrg;
	}


	public boolean isTestUserTaobaoUser() {
		return isTestUserTaobaoUser;
	}


	public void setTestUserTaobaoUser(boolean isTestUserTaobaoUser) {
		this.isTestUserTaobaoUser = isTestUserTaobaoUser;
	}


	public boolean isTestUserType() {
		return isTestUserType;
	}


	public void setTestUserType(boolean isTestUserType) {
		this.isTestUserType = isTestUserType;
	}



	public boolean isTestUserType(boolean include){
		if(this.userType == null) return false;
		List<String> testUserTypes = this.testUserConfig.getTestUserTypes();
		if(testUserTypes == null || testUserTypes.isEmpty()) return false;
		isTestUserType = include?this.checkUserType(this.userType):!this.checkUserType(this.userType);
		return this.isTestUserType;
	}
	
	
	public boolean isTestTaobaoUserId(boolean include){
	  if(this.taobaoUserId == null) return false;
	  List<Long> testUserTaobaoUserIds = this.testUserConfig.getTestTaobaoUserIds();
	  if(testUserTaobaoUserIds == null || testUserTaobaoUserIds.isEmpty()) return false;
	  this.isTestUserTaobaoUser = include?checkTaobaoUser(this.taobaoUserId):!checkTaobaoUser(this.taobaoUserId);
	  return isTestUserTaobaoUser;
	}
	
	public boolean isTestOrg(boolean include){
		if(this.orgId == null) return false;
		  List<Long> testUserOrgIds = this.testUserConfig.getTestOrgIds();
		if(testUserOrgIds == null || testUserOrgIds.isEmpty()) return false;
		isTestOrg = include?this.checkTestOrg(this.orgId):!this.checkTestOrg(this.orgId);
		return isTestOrg;
	}
	
	private boolean checkUserType(String userType){
		List<String> testUserTypes = this.testUserConfig.getTestUserTypes();
		return testUserTypes.contains(userType);
	}
	
	public boolean checkTaobaoUser(Long taobaoUserId){
		 List<Long> testUserTaobaoUserIds = this.testUserConfig.getTestTaobaoUserIds();
		return testUserTaobaoUserIds.contains(taobaoUserId);
	}
	
	private boolean checkTestOrg(Long orgId){
		Assert.notNull(cuntaoOrgServiceClient);
		if(orgId == null){
			return false;
		}
		List<Long> testUserOrgIds = this.testUserConfig.getTestOrgIds();
		CuntaoOrgDto cuntaoOrgDto = cuntaoOrgServiceClient.getCuntaoOrg(orgId);
		if(testUserOrgIds.contains(cuntaoOrgDto.getId())){
			return true;
		}else{
			return checkTestOrg(cuntaoOrgDto.getParentId());
		}
	}
	
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}
	
	public void setCuntaoOrgServiceClient(CuntaoOrgServiceClient cuntaoOrgServiceClient) {
		this.cuntaoOrgServiceClient = cuntaoOrgServiceClient;
	}
	
	

	public TestUserConfig getTestUserConfig() {
		return testUserConfig;
	}


	public void setTestUserConfig(TestUserConfig testUserConfig) {
		this.testUserConfig = testUserConfig;
	}


	public CuntaoOrgServiceClient getCuntaoOrgServiceClient() {
		return cuntaoOrgServiceClient;
	}


	public void setUseCustomerUserExpression(boolean useCustomerUserExpression) {
		this.useCustomerUserExpression = useCustomerUserExpression;
	}


	public String getTestUserExpression() {
		return testUserExpression;
	}

	public void setTestUserExpression(String testUserExpression) {
		this.testUserExpression = testUserExpression;
	}

	
	
}
