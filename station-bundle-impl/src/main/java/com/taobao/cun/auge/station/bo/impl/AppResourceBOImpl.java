package com.taobao.cun.auge.station.bo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.taobao.cun.auge.dal.domain.AppResource;
import com.taobao.cun.auge.dal.mapper.AppResourceMapper;
import com.taobao.cun.auge.station.bo.AppResourceBO;

public class AppResourceBOImpl implements AppResourceBO {
	
	@Autowired
	AppResourceMapper appResourceMapper;
	
	@Override
	public List<AppResource> queryAppResourceList(String type) {
		AppResource appResource = new AppResource();
		appResource.setType(type);
		return appResourceMapper.select(appResource);
	}

	@Override
	public AppResource queryAppResource(String type, String key) {
		AppResource appResource = new AppResource();
		appResource.setType(type);
		appResource.setName(key);
	    return appResourceMapper.selectOne(appResource);
	}

}
