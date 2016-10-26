package com.taobao.cun.auge.station.bo;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

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
	
	public Map<String, AppResource> queryAppResourceMap(String type);
	
	public boolean configAppResource(@NotNull String type,@NotNull  String key, @NotNull String value, boolean isDelete, @NotNull String configurePerson);
	
	public String queryAppValueNotAllowNull(String type, String key);
}
