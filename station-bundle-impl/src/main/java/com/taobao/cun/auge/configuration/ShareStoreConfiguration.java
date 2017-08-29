package com.taobao.cun.auge.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aliexpress.boot.hsf.HSFGroup;
import com.aliexpress.boot.hsf.consumer.HsfConsumerContext;
import com.taobao.inventory.sic.service.client.store.StoreInfoServiceClient;
import com.taobao.inventory.sic.service.store.IStoreInfoService;
import com.taobao.place.client.service.StoreCreateService;
import com.taobao.place.client.service.StoreUpdateService;

@Configuration
public class ShareStoreConfiguration {
	
	@Bean
	public StoreCreateService storeCreateService(
			HsfConsumerContext context,
			@Value("${hsf.consumer.version.sharestore}") String version){
		return context.hsfConsumerBuilder(StoreCreateService.class, HSFGroup.HSF.getName(), version).build();
	}
	
	@Bean
	public StoreUpdateService storeUpdateService(
			HsfConsumerContext context,
			@Value("${hsf.consumer.version.sharestore}") String version){
		return context.hsfConsumerBuilder(StoreUpdateService.class, HSFGroup.HSF.getName(), version).build();
	}
	
	@Bean
	public IStoreInfoService iStoreInfoService(
			HsfConsumerContext context,
			@Value("${hsf.consumer.version.istore}") String version){
		return context.hsfConsumerBuilder(IStoreInfoService.class, HSFGroup.HSF.getName(), version).build();
	}
	
	@Bean
	public StoreInfoServiceClient storeInfoServiceClient(){
		return new StoreInfoServiceClient();
	}
}
