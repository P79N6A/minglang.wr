package com.taobao.cun.auge.appresource.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.cun.auge.appresource.AppResourceService;
import com.taobao.cun.auge.appresource.dto.AppResourceDto;
import com.taobao.cun.auge.dal.domain.AppResource;
import com.taobao.cun.auge.station.bo.AppResourceBO;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("appResourceService")
@HSFProvider(serviceInterface = AppResourceService.class)
public class AppResourceServiceImpl implements AppResourceService{

	@Autowired
	AppResourceBO appResourceBO;
	
	@Override
	public List<AppResourceDto> queryAppResourceList(String type) {
		List<AppResourceDto> result=new ArrayList<AppResourceDto>();
		for(AppResource resource:appResourceBO.queryAppResourceList(type)){
			result.add(convert(resource));
		}
		return result;
	}

	@Override
	public AppResourceDto queryAppResource(String type, String key) {
		return convert(appResourceBO.queryAppResource(type, key));
	}

	@Override
	public String queryAppResourceValue(String type, String key) {
		return appResourceBO.queryAppResourceValue(type, key);
	}

	@Override
	public String queryAppValueNotAllowNull(String type, String key) {
		return appResourceBO.queryAppValueNotAllowNull(type, key);
	}

	private AppResourceDto convert(AppResource app){
		if(app==null){
			return null;
		}else{
			AppResourceDto dto=new AppResourceDto();
			BeanUtils.copyProperties(app, dto);
			return dto;
		}
	}
}
