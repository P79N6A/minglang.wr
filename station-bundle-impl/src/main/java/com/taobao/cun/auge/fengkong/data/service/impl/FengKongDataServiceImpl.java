package com.taobao.cun.auge.fengkong.data.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.china.dw.dataopen.api.Results;
import com.alibaba.china.dw.dataopen.api.SQLIDQueryAPI;
import com.alibaba.china.dw.dataopen.api.User;
import com.taobao.cun.auge.fengkong.data.service.FengKongDataService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@HSFProvider(serviceInterface = FengKongDataService.class)
public class FengKongDataServiceImpl implements FengKongDataService {
	
	private static final Logger logger = LoggerFactory
			.getLogger(FengKongDataServiceImpl.class);
	
	@Autowired
	private SQLIDQueryAPI sqlIDQueryAPI;
	
	@Value("${fengkong.oneService.appName}")
	private String appName;
	
	@Value("${fengkong.oneService.attemp.passwd}")
	private String passwd;
	
	@Value("${fengkong.oneService.attemp.sqlId}")
	private String sqlId;
	
	@Override
	public Boolean checkXuJiaBlackList(Long taobaoUserId) {
		try {
			User user = getUser();
			Map<String, Object> conditions = new HashMap<String, Object>();
			conditions.put("taobaoUserId", taobaoUserId);
			String[] returnFields = new String[] { "taobao_user_id"};
			Results result = sqlIDQueryAPI.list(user, Integer.parseInt(sqlId), conditions,
					returnFields);
			List<Map<String, Object>> itemList = result.getResult();
			if (itemList != null && itemList.size()>0) {
				return Boolean.TRUE;
			}
			
			return Boolean.FALSE;
		} catch (Exception e) {
			logger.error("query checkXuJiaBlackList error :" + taobaoUserId, e);
			return null;
		}
	}
	
	private User getUser(){
		User user = new User();
		user.setAppName(appName);
		user.setName(appName);
		user.setPassword(passwd);
		return user;
	}

}
