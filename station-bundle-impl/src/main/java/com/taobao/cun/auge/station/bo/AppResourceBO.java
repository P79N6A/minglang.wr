package com.taobao.cun.auge.station.bo;

import java.util.List;

import com.taobao.cun.auge.dal.domain.AppResource;

/**
 * 资源表服务
 * @author quanzhu.wangqz
 *
 */
public interface AppResourceBO {

	public List<AppResource> queryAppResourceList(String type);

	public AppResource queryAppResource(String type, String key);
	
	public String queryAppResourceValue(String type, String key);
	
}
