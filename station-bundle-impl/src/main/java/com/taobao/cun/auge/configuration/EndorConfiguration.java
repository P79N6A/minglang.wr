package com.taobao.cun.auge.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aliexpress.boot.hsf.HSFGroup;
import com.aliexpress.boot.hsf.consumer.HsfConsumerContext;
import com.taobao.cun.endor.service.OrgService;

@Configuration
public class EndorConfiguration {
	
	@Bean
	public OrgService orgService(
			HsfConsumerContext context,
			@Value("${hsf.consumer.version.endor}") String version){
		return context.hsfConsumerBuilder(OrgService.class, HSFGroup.HSF.getName(), version).build();
	}
	
}
