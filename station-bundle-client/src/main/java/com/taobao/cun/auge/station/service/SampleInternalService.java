package com.taobao.cun.auge.station.service;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import com.taobao.cun.auge.station.dto.SampleInstance;

@MessagingGateway
//@HSFProvider(serviceInterface = SampleInternalService.class)
public interface SampleInternalService {

	public static final String ADD_SAMPLE_CHANNEL = "addSample.input";
	
	public static final String ROUTE_SAMPLE_CHANNEL = "routeSample.input";
	
	public static final String PUBLISH_SUBSCRIBE_SAMPLE_CHANNEL = "publishSubscribeChannel";
	
	@Gateway(requestChannel=ADD_SAMPLE_CHANNEL)
	public Long addSample(SampleInstance instance);
	
	
	@Gateway(requestChannel=ROUTE_SAMPLE_CHANNEL)
	public void routeSample(SampleInstance instance);
	
	@Gateway(requestChannel=PUBLISH_SUBSCRIBE_SAMPLE_CHANNEL)
	public void publishSubscribeSample(SampleInstance instance);
}
