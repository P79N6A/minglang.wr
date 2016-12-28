package com.taobao.cun.auge.appresource;

import java.util.List;

import com.taobao.cun.auge.appresource.dto.AppResourceDto;

public interface AppResourceService {

	public List<AppResourceDto> queryAppResourceList(String type);

	public AppResourceDto queryAppResource(String type, String key);
	
	public String queryAppResourceValue(String type, String key);
	
	public String queryAppValueNotAllowNull(String type, String key);
}
