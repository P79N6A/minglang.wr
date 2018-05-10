package com.taobao.cun.auge.testuser;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import com.alibaba.boot.diamond.annotation.DiamondConfigListener;
import com.alibaba.boot.diamond.annotation.DiamondListener;
import com.alibaba.boot.diamond.enums.DiamondConfigFormat;
import com.alibaba.boot.diamond.listener.MissingDataException;
import com.alibaba.boot.diamond.listener.MissingDataExceptionHandler;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;

@DiamondListener
public class TestUserInfoManager {

   // private final static Logger logger = LoggerFactory.getLogger(TestUserInfoProperties.class);
    
    private Map<String,List<TestUserInfo>> userInfos;
    
    @DiamondConfigListener(dataId = "com.taobao.cun.auge:testUserInfo.properties", missingDataExceptionHandler = IgnoreMissingDataExceptionHandler.class, format = DiamondConfigFormat.TEXT)
    public void receiveJson(Properties properties) {
    	Map<String,List<TestUserInfo>> userInfos = Maps.newHashMap();
    	properties.forEach((k,v)->{
    		List<TestUserInfo> infos = JSON.parseArray((String)v,TestUserInfo.class);
    		userInfos.put((String)k, infos);
    	});
    	this.userInfos = userInfos;
    }

    
    public class IgnoreMissingDataExceptionHandler implements MissingDataExceptionHandler{

		@Override
		public void handle(MissingDataException exception) {
			//do nothing
		}
    	
    }


	public Map<String, List<TestUserInfo>> getUserInfos() {
		return userInfos;
	}

	
	public Map<String,String> getUserConfig(String bizCode,Long taobaoUserId){
		List<TestUserInfo> configs = userInfos.get(bizCode);
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
