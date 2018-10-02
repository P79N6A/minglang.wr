package com.taobao.cun.auge.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.springframework.boot.util.builder.HsfConsumerBuilder;
import com.aliexpress.boot.hsf.HSFGroup;
import com.taobao.cun.auge.org.service.CuntaoOrgServiceClient;
import com.taobao.cun.auge.org.service.CuntaoOrgTagService;
import com.taobao.cun.auge.org.service.ExtCountyService;
import com.taobao.cun.auge.org.service.ExtDeptOrgClient;
import com.taobao.cun.auge.org.service.impl.ExtDeptOrgClientImpl;
import com.taobao.cun.auge.permission.service.EndorUserPermissionService;
import com.taobao.hsf.app.spring.util.HSFSpringConsumerBean;
import com.taobao.hsf.app.spring.util.annotation.HSFConsumer;

@Configuration
public class BizBaseOrgConfiguration {
	@Value("${hsf.consumer.org.version}") 
	private String version;
	
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean menuService() {
		return HsfConsumerBuilder.create(EndorUserPermissionService.class, HSFGroup.HSF.getName(), version).build();
	}
	
	@HSFConsumer(serviceVersion="${hsf.consumer.org.version}",serviceGroup="HSF")
	private CuntaoOrgTagService cuntaoOrgTagService;
	
	@HSFConsumer(serviceVersion="${hsf.consumer.org.version}",serviceGroup="HSF")
	private ExtCountyService extCountyService;
	
	@Bean
    public ExtDeptOrgClient ossFileStoreService(
    		CuntaoOrgTagService cuntaoOrgTagService,
    		ExtCountyService extCountyService,
            CuntaoOrgServiceClient cuntaoOrgServiceClient) throws Exception {
		ExtDeptOrgClientImpl extDeptOrgClient = new ExtDeptOrgClientImpl();
		extDeptOrgClient.setCuntaoOrgServiceClient(cuntaoOrgServiceClient);
		extDeptOrgClient.setCuntaoOrgTagService(cuntaoOrgTagService);
		extDeptOrgClient.setExtCountyService(extCountyService);
		return extDeptOrgClient;
	}
}
