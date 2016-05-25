package com.taobao.cun.auge.station.bo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.AppResource;
import com.taobao.cun.auge.dal.mapper.AppResourceMapper;
import com.taobao.cun.auge.station.bo.AppResourceBO;

@Component("appResourceBO")
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

	@Override
	public String queryAppResourceValue(String type, String key) {
		if (type == null || key == null) {
			throw new java.lang.UnsupportedOperationException("param is null");
		}
		AppResource app = this.queryAppResource(type, key);
		if (app != null) {
			return app.getValue();
		}
		return "";
	}

}
