package com.taobao.cun.auge.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aliexpress.boot.hsf.HSFGroup;
import com.aliexpress.boot.hsf.HsfConsumerAutoConfiguration;
import com.taobao.cun.ar.scene.station.service.PartnerLifecycleCallbackService;
import com.taobao.cun.ar.scene.station.service.StationLifecycleCallbackService;
import com.taobao.cun.auge.msg.service.MessageService;
import com.taobao.cun.chronus.service.TaskExecuteService;
import com.taobao.cun.crius.bpm.service.CuntaoWorkFlowService;
import com.taobao.cun.crius.uic.service.CuntaoUicReadService;
import com.taobao.cun.service.alipay.AlipayAccountTagService;
import com.taobao.cun.service.alipay.AlipayStandardBailService;
import com.taobao.cun.service.trade.TaobaoTradeOrderQueryService;
import com.taobao.cun.service.uic.PaymentAccountQueryService;
import com.taobao.hsf.app.spring.util.HSFSpringConsumerBean;

@Configuration
public class HsfConsumerConfiguration extends HsfConsumerAutoConfiguration {

	// chronus服务
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean taskExecuteService(@Value("${hsf.consumer.version.chronus.taskExecuteService}") String version) {
		return getConsumerBean(TaskExecuteService.class, HSFGroup.HSF, version, 3000);
	}

	// crius服务
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean cuntaoWorkFlowService(@Value("${hsf.consumer.version.crius.cuntaoWorkFlowService}") String version) {
		return getConsumerBean(CuntaoWorkFlowService.class, HSFGroup.HSF, version, 3000);
	}

	// cuntaocenter服务
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean taobaoTradeOrderQueryService(
			@Value("${hsf.consumer.version.cuntaocenter.taobaoTradeOrderQueryService}") String version) {
		return getConsumerBean(TaobaoTradeOrderQueryService.class, HSFGroup.HSF, version, 3000);
	}
	
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean messageService(
			@Value("${hsf.consumer.version.auge.messageService}") String version) {
		return getConsumerBean(MessageService.class, HSFGroup.HSF, version, 3000);
	}
	

	// cuntaocenter服务
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean paymentAccountQueryService(
			@Value("${hsf.consumer.version.cuntaocenter.paymentAccountQueryService}") String version) {
		return getConsumerBean(PaymentAccountQueryService.class, HSFGroup.HSF, version, 3000);
	}
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean alipayStandardBailService(
			@Value("${hsf.consumer.version.cuntaocenter.alipayStandardBailService}") String version) {
		return getConsumerBean(AlipayStandardBailService.class, HSFGroup.HSF, version, 3000);
	}
	
	
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean alipayAccountTagService(
			@Value("${hsf.consumer.version.cuntaocenter.alipayAccountTagService}") String version) {
		return getConsumerBean(AlipayAccountTagService.class, HSFGroup.HSF, version, 3000);
	}

	//crius服务
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean cuntaoUicReadService(
			@Value("${hsf.consumer.version.crius.cuntaoUicReadService}") String version) {
		return getConsumerBean(CuntaoUicReadService.class, HSFGroup.HSF, version, 3000);
	}
	
	//admin服务
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean partnerLifecycleCallbackService(
			@Value("${hsf.consumer.version.admin.partnerLifecycleCallbackService}") String version) {
		return getConsumerBean(PartnerLifecycleCallbackService.class, HSFGroup.HSF, version, 3000);
	}
	
	//admin服务
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean stationLifecycleCallbackService(
			@Value("${hsf.consumer.version.admin.stationLifecycleCallbackService}") String version) {
		return getConsumerBean(StationLifecycleCallbackService.class, HSFGroup.HSF, version, 3000);
	}
}
