package com.taobao.cun.auge.configuration;

import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

@Configuration
public class ValidationConfiguration {

	@Bean
	public Validator validator() {
		return new org.springframework.validation.beanvalidation.LocalValidatorFactoryBean();
	}
	
	
	 @Bean
	    public MethodValidationPostProcessor methodValidationPostProcessor(@Qualifier("validatorFactory")ValidatorFactory validatorFactory){
	    	MethodValidationPostProcessor methodValidationPostProcessor =  new MethodValidationPostProcessor();
	    	methodValidationPostProcessor.setValidatorFactory(validatorFactory);
	    	return methodValidationPostProcessor;
	    }
	    
	    @Bean
		public ValidatorFactory validatorFactory(@Qualifier("exceptionMessageSource")MessageSource messageSource) {
	    	LocalValidatorFactoryBean validator = new org.springframework.validation.beanvalidation.LocalValidatorFactoryBean();
	    	validator.setValidationMessageSource(messageSource);
	    	return validator;
		}
	    
	    @Bean
	    public MessageSource exceptionMessageSource(){
	    	ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
	    	messageSource.setDefaultEncoding("UTF-8");
	    	messageSource.setBasename("exception");
	    	return messageSource;
	    }
}
