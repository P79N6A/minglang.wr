package com.taobao.cun.diamond;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.alibaba.boot.diamond.annotation.DiamondConfigListener;
import com.alibaba.boot.diamond.annotation.DiamondListener;
import com.alibaba.boot.diamond.enums.DiamondConfigFormat;
import com.alibaba.boot.diamond.listener.MissingDataException;
import com.alibaba.boot.diamond.listener.MissingDataExceptionHandler;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.taobao.cun.auge.testuser.TestUserInfo;

@DiamondListener
public class AugeDiamondListener {

	private Map<String, List<TestUserInfo>> userInfos;

	@DiamondConfigListener(dataId = "com.taobao.cun.auge:testUserInfo.properties", missingDataExceptionHandler = IgnoreMissingDataExceptionHandler.class, format = DiamondConfigFormat.TEXT)
	public void receiveDiamondConfig(Properties properties) {
		Map<String, List<TestUserInfo>> userInfos = Maps.newHashMap();
		properties.forEach((k, v) -> {
			List<TestUserInfo> infos = JSON.parseArray((String) v, TestUserInfo.class);
			userInfos.put((String) k, infos);
		});
		this.userInfos = userInfos;
	}

	public Map<String, List<TestUserInfo>> getUserInfos() {
		return userInfos;
	}


    public class IgnoreMissingDataExceptionHandler implements MissingDataExceptionHandler{

		@Override
		public void handle(MissingDataException exception) {
			//do nothing
		}
    	
    }
}
