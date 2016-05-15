package com.taobao.cun.auge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.taobao.cun.auge.station.dto.SampleInstance;
import com.taobao.cun.auge.station.service.SampleInternalService;
import com.taobao.hsf.app.spring.util.annotation.EnableHSFProvider;

@SpringBootApplication
@EnableHSFProvider
@EnableTransactionManagement
@EnableIntegration
@IntegrationComponentScan
public class Application extends SpringBootServletInitializer{

    public static void main(String[] args) {
    	ConfigurableApplicationContext ctx =
				SpringApplication.run(Application.class, args);

		SampleInternalService stationInternalService = ctx.getBean(SampleInternalService.class);
		SampleInstance instance = new SampleInstance();
		Long id = stationInternalService.addSample(instance);
		System.out.println(id);
		instance.setType("tp");
		stationInternalService.routeSample(instance);
		instance.setType("tpa");
		stationInternalService.routeSample(instance);
		instance.setType("tpv");
		stationInternalService.routeSample(instance);
		
		stationInternalService.publishSubscribeSample(instance);
    	
    }

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(Application.class);
	}


}
