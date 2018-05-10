package com.taobao.cun.auge.testuser;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.taobao.cun.diamond.AugeDiamondListener;

@Component
public class TestUserInfoManager {


	@Autowired
	private AugeDiamondListener diamondListener;

	
	public Map<String,String> getUserConfig(String bizCode,Long taobaoUserId){
		List<TestUserInfo> configs = diamondListener.getUserInfos().get(bizCode);
		Map<String,String> userConfig = Maps.newHashMap();
		if(configs != null){
			List<TestUserInfo> validUserInfos = configs.stream().filter(config -> config !=null && config.getTaobaoUserIds()!=null && config.getTaobaoUserIds().contains(taobaoUserId)).collect(Collectors.toList());
			validUserInfos.forEach(info -> {
				if(info.getConfig() !=null){
					userConfig.putAll(info.getConfig());
				}
			});
		}
		return userConfig;
	}

}
