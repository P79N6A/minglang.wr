package com.taobao.cun.auge.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Lists;
import com.taobao.kfc.client.lite.config.ApplyConfig;
import com.taobao.kfc.client.lite.config.GlobalConfig;
import com.taobao.kfc.client.lite.service.LiteMergeSearchService;

@Configuration
public class KFCConfiguration {
	@Value("${kfc.globalConfig.environment}")
	private String environment;
	@Value("${kfc.globalConfig.projectName}")
	private String projectName;
	
	@Value("${kfc.applyConfig.applyCode}")
	private String applyCode;
	@Value("${kfc.applyConfig.decryptKey}")
	private String decryptKey;
	
	@Bean
	public LiteMergeSearchService liteMergeSearchService() {
		LiteMergeSearchService liteMergeSearchService = new LiteMergeSearchService();
		GlobalConfig globalConfig = new GlobalConfig();
		globalConfig.setEnvironment(environment);
		globalConfig.setProjectName(projectName);
		globalConfig.setGroupName("default");
		globalConfig.setLoadMode("loadMode");
		globalConfig.setSerializationType("hessian");
		globalConfig.setReloadDelay(5);
		//globalConfig.setPackageVersion(packageVersion);
		liteMergeSearchService.setGlobalConfig(globalConfig);
		
		List<ApplyConfig> applyConfigs = Lists.newArrayList();
		ApplyConfig applyConfig = new ApplyConfig();
		applyConfig.setApplyCode(applyCode);
		applyConfig.setDecryptKey(decryptKey);
		applyConfigs.add(applyConfig);
		liteMergeSearchService.setApplyConfigs(applyConfigs);
		return liteMergeSearchService;
	}
}
