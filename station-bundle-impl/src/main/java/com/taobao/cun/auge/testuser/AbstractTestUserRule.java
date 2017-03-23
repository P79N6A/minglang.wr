package com.taobao.cun.auge.testuser;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public abstract class AbstractTestUserRule implements TestUserRule{

	private static final String REVERSE_CHAR = "!";

	@Override
	public boolean checkTestUser(Long taobaoUserId,Map<String,String> config) {
		if(isReverseKey(getMatchKey(config))){
			return !doCheckTestUser(taobaoUserId,config.get(getMatchKey(config)));
		}
		return doCheckTestUser(taobaoUserId,config.get(getMatchKey(config)));
	}

	public abstract boolean doCheckTestUser(Long taobaoUserId,String config);

	@Override
	public boolean isMatch(Map<String,String> config){
		if(StringUtils.isNotEmpty(getMatchKey(config))){
			return StringUtils.isNotEmpty(config.get(getConfigKey()))||StringUtils.isNotEmpty(REVERSE_CHAR+this.getConfigKey());
		}
		return false;
	}

	private boolean isMatchKey(String configKey) {
		return configKey.equals(getConfigKey())||configKey.equals(REVERSE_CHAR+this.getConfigKey());
	}
	
	private String getMatchKey(Map<String,String> config){
		return config.keySet().stream().filter(this::isMatchKey).findFirst().get();
	}
	
	public boolean isReverseKey(String configKey){
		return configKey.startsWith(REVERSE_CHAR);
	}

}
