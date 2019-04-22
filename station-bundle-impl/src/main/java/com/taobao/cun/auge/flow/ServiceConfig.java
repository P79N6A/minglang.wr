package com.taobao.cun.auge.flow;

import java.util.List;
/**
 * 服务配置
 * 
 * @author chengyu.zhoucy
 *
 */
public class ServiceConfig {
	private String defaultVersion;
	
	private List<ServiceMeta> serviceMetas;

	public String getDefaultVersion() {
		return defaultVersion;
	}

	public void setDefaultVersion(String defaultVersion) {
		this.defaultVersion = defaultVersion;
	}

	public List<ServiceMeta> getServiceMetas() {
		return serviceMetas;
	}

	public void setServiceMetas(List<ServiceMeta> serviceMetas) {
		this.serviceMetas = serviceMetas;
	}
}
