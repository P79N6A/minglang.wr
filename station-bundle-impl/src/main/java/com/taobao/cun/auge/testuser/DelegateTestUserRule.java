package com.taobao.cun.auge.testuser;

import java.util.function.Supplier;

public class DelegateTestUserRule{

	private Supplier<String> configSupplier;
	
	private boolean isReserve;
	
	private TestUserRule rule;
	
	public static DelegateTestUserRule of(Supplier<String> configSupplier,boolean isResverse,TestUserRule rule){
		 return  new DelegateTestUserRule(configSupplier,isResverse,rule);
	}
	
	public DelegateTestUserRule(Supplier<String> configSupplier,boolean isReserve,TestUserRule rule){
		this.configSupplier = configSupplier;
		this.isReserve = isReserve;
		this.rule = rule;
	}
	
	public String getConfig() {
		return configSupplier.get();
	}

	public boolean checkTestUser(Long taobaoUserId) {
		boolean result = rule.checkTestUser(taobaoUserId,this.getConfig());
		if(isReserve){
			return !result;
		}
		else{
			return result;
		}
	}
	
}
