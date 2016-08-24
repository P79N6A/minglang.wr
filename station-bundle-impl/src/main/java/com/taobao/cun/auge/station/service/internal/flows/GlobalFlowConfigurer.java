package com.taobao.cun.auge.station.service.internal.flows;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.GlobalChannelInterceptor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptorAdapter;

//@Configuration
public class GlobalFlowConfigurer {
	@Autowired
	private Validator validator ;
	 @Bean
     @GlobalChannelInterceptor(patterns="*Channel")
     public ChannelInterceptorAdapter validationInterceptor() {
		return  new ChannelInterceptorAdapter(){

			@Override
			public Message<?> preSend(Message<?> message, MessageChannel channel) {
				//Object object = message.getPayload();
				//validator.validate(object);
				//JSR3
				//throw BizException();
				//do Validation
				System.out.println("message:"+message);
				return super.preSend(message, channel);
			}
			
		};
	 }
	 
	
}
