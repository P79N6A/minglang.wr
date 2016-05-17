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
import com.taobao.cun.auge.station.service.TaobaoTradeOrderQueryService;
import com.taobao.cun.chronus.service.TaskExecuteService;
import com.taobao.cun.crius.bpm.service.CuntaoWorkFlowService;
import com.taobao.hsf.app.spring.util.HSFSpringConsumerBean;

@Configuration
public class HsfConsumerConfiguration extends HsfConsumerAutoConfiguration {

	// chronus服务
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean taskExecuteService(@Value("${hsf.version.taskExecuteService}") String version) {
		return getConsumerBean(TaskExecuteService.class, HSFGroup.HSF, version, 3000);
	}

	// crius服务
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean cuntaoWorkFlowService(@Value("${hsf.version.cuntaoWorkFlowService}") String version) {
		return getConsumerBean(CuntaoWorkFlowService.class, HSFGroup.HSF, version, 3000);
	}

	// auge服务
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean taobaoTradeOrderQueryService(@Value("${hsf.version.taobaoTradeOrderQueryService}") String version) {
		return getConsumerBean(TaobaoTradeOrderQueryService.class, HSFGroup.HSF, version, 3000);
	}

}
