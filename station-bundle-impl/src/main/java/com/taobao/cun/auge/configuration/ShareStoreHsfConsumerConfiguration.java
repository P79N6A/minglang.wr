package com.taobao.cun.auge.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.springframework.boot.util.builder.HsfConsumerBuilder;
import com.taobao.hsf.app.spring.util.HSFSpringConsumerBean;
import com.taobao.place.client.service.StoreCreateService;

@Configuration
public class ShareStoreHsfConsumerConfiguration {
	
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean storeCreateService(
			@Value("${hsf.consumer.version.sharestore}") String version, 
			@Value("${hsf.consumer.group.sharestore}") String group){
		return HsfConsumerBuilder.create(StoreCreateService.class, group, version).build();
	}
}
