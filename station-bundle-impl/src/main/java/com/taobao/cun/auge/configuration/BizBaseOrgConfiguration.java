package com.taobao.cun.auge.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import com.alibaba.springframework.boot.util.builder.HsfConsumerBuilder;
import com.aliexpress.boot.hsf.HSFGroup;
import com.taobao.cun.auge.permission.service.EndorUserPermissionService;
import com.taobao.hsf.app.spring.util.HSFSpringConsumerBean;

public class BizBaseOrgConfiguration {
	@Value("${hsf.consumer.org.version}") 
	private String version;
	
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean menuService() {
		return HsfConsumerBuilder.create(EndorUserPermissionService.class, HSFGroup.HSF.getName(), version).build();
	}
}
