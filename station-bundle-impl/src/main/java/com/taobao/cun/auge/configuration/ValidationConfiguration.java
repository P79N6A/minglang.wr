package com.taobao.cun.auge.configuration;

import javax.validation.Validator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidationConfiguration {

	@Bean
	public Validator validator() {
		return new org.springframework.validation.beanvalidation.LocalValidatorFactoryBean();
	}
}
