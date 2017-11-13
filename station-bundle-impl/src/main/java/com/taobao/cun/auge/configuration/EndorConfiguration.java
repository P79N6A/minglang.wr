package com.taobao.cun.auge.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aliexpress.boot.hsf.HSFGroup;
import com.aliexpress.boot.hsf.consumer.HsfConsumerContext;
import com.taobao.cun.endor.service.AppService;
import com.taobao.cun.endor.service.OrgService;
import com.taobao.cun.endor.service.UserRoleService;
import com.taobao.cun.endor.service.UserService;

@Configuration
public class EndorConfiguration {
	
	@Bean
	public OrgService orgService(
			HsfConsumerContext context,
			@Value("${hsf.consumer.version.endor}") String version){
		return context.hsfConsumerBuilder(OrgService.class, HSFGroup.HSF.getName(), version).build();
	}
	
	@Bean
	public UserRoleService userRoleService(
			HsfConsumerContext context,
			@Value("${hsf.consumer.version.endor}") String version){
		return context.hsfConsumerBuilder(UserRoleService.class, HSFGroup.HSF.getName(), version).build();
	}
	
	@Bean
	public UserService userService(
			HsfConsumerContext context,
			@Value("${hsf.consumer.version.endor}") String version){
		return context.hsfConsumerBuilder(UserService.class, HSFGroup.HSF.getName(), version).build();
	}
	
	@Bean
	public AppService appService(
			HsfConsumerContext context,
			@Value("${hsf.consumer.version.endor}") String version){
		return context.hsfConsumerBuilder(AppService.class, HSFGroup.HSF.getName(), version).build();
	}
	
}
