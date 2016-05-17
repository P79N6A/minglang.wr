package com.taobao.cun.auge.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.masterdata.client.service.Employee360Service;
import com.aliexpress.boot.hsf.HSFGroup;
import com.aliexpress.boot.hsf.HsfConsumerAutoConfiguration;
import com.aliexpress.boot.hsf.HsfProperties;
import com.taobao.hsf.app.spring.util.HSFSpringConsumerBean;

@Configuration
@EnableConfigurationProperties(HsfProperties.class)
public class HsfServiceConfiguration extends HsfConsumerAutoConfiguration {

	@Bean(initMethod = "init")
	public HSFSpringConsumerBean employee360Service() {
		return getConsumerBean(Employee360Service.class, HSFGroup.HSF, "1.0.0.daily", 3000);
	}
}
