package com.taobao.cun.auge.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.cainiao.cuntaonetwork.service.station.StationUserWriteService;
import com.alibaba.cainiao.cuntaonetwork.service.station.StationWriteService;
import com.alibaba.cainiao.cuntaonetwork.service.warehouse.CountyDomainWriteService;
import com.alibaba.masterdata.client.service.Employee360Service;
import com.aliexpress.boot.hsf.HSFGroup;
import com.aliexpress.boot.hsf.HsfConsumerAutoConfiguration;
import com.taobao.hsf.app.spring.util.HSFSpringConsumerBean;
import com.taobao.uic.common.service.userinfo.client.UicReadServiceClient;
@Configuration
public class HsfConsumer2ndPartyConfiguration extends HsfConsumerAutoConfiguration {

	// hr相关的第二方服务
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean employee360Service(@Value("${hsf.consumer.version.employeeService}") String version) {
		return getConsumerBean(Employee360Service.class, HSFGroup.HSF, version, 3000);
	}
	
	// hr相关的第二方服务
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean uicReadServiceClient(@Value("${hsf.consumer.version.uicReadServiceClient}") String version) {
		return getConsumerBean(UicReadServiceClient.class, HSFGroup.HSF, version, 3000);
	}
	
	//旺旺
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean hsf2icesrv(@Value("${hsf.consumer.version.hsf2icesrv}") String version) {
		return getConsumerBean(com.taobao.wws.hsf2icesrv.class, HSFGroup.HSF, version, 3000);
	}

	// 菜鸟服务
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean countyDomainWriteService(@Value("${hsf.consumer.version.cainiao.countyDomainWriteService}") String version) {
		return getConsumerBean(CountyDomainWriteService.class, HSFGroup.HSF, version, 3000);
	}

	@Bean(initMethod = "init")
	public HSFSpringConsumerBean stationWriteService(@Value("${hsf.consumer.version.cainiao.stationWriteService}") String version) {
		return getConsumerBean(StationWriteService.class, HSFGroup.HSF, version, 3000);
	}

	@Bean(initMethod = "init")
	public HSFSpringConsumerBean stationUserWriteService(@Value("${hsf.consumer.version.cainiao.stationUserWriteService}") String version) {
		return getConsumerBean(StationUserWriteService.class, HSFGroup.HSF, version, 3000);
	}

}
