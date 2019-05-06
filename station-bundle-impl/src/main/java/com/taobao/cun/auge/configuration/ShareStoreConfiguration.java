package com.taobao.cun.auge.configuration;

import com.aliexpress.boot.hsf.HSFGroup;
import com.aliexpress.boot.hsf.consumer.HsfConsumerContext;
import com.taobao.inventory.sic.service.client.store.StoreInfoServiceClient;
import com.taobao.inventory.sic.service.store.IStoreInfoService;
import com.taobao.place.client.service.GroupBindService;
import com.taobao.place.client.service.StoreCreateService;
import com.taobao.place.client.service.StoreGroupService;
import com.taobao.place.client.service.StoreUpdateService;
import com.taobao.place.client.service.v2.StoreCreateServiceV2;
import com.taobao.place.client.service.v2.StoreExtendServiceV2;
import com.taobao.place.client.service.v2.StoreUpdateServiceV2;
import com.taobao.place.client.service.v2.StoreDeleteServiceV2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

	@Bean
	public StoreCreateServiceV2 storeCreateServiceV2(
			HsfConsumerContext context,
			@Value("${hsf.consumer.version.sharestore}") String version){
		return context.hsfConsumerBuilder(StoreCreateServiceV2.class, HSFGroup.HSF.getName(), version).build();
	}

	@Bean
	public StoreUpdateServiceV2 storeUpdateServiceV2(
			HsfConsumerContext context,
			@Value("${hsf.consumer.version.sharestore}") String version){
		return context.hsfConsumerBuilder(StoreUpdateServiceV2.class, HSFGroup.HSF.getName(), version).build();
	}

	@Bean
	public StoreDeleteServiceV2 storeDeleteServiceV2(
			HsfConsumerContext context,
			@Value("${hsf.consumer.version.sharestore}") String version){
		return context.hsfConsumerBuilder(StoreDeleteServiceV2.class, HSFGroup.HSF.getName(), version).build();
	}

	@Bean
	public StoreExtendServiceV2 storeExtendServiceV2(
			HsfConsumerContext context,
			@Value("${hsf.consumer.version.sharestore}") String version){
		return context.hsfConsumerBuilder(StoreExtendServiceV2.class, HSFGroup.HSF.getName(), version).build();
	}

	@Bean
	public StoreGroupService storeGroupService(
			HsfConsumerContext context,
			@Value("${hsf.consumer.version.sharestore}") String version){
		return context.hsfConsumerBuilder(StoreGroupService.class, HSFGroup.HSF.getName(), version).build();
	}

	@Bean
	public GroupBindService groupBindService(
			HsfConsumerContext context,
			@Value("${hsf.consumer.version.sharestore}") String version){
		return context.hsfConsumerBuilder(GroupBindService.class, HSFGroup.HSF.getName(), version).build();
	}
}
