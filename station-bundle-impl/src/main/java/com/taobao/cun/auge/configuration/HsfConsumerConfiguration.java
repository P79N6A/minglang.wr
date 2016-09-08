package com.taobao.cun.auge.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aliexpress.boot.hsf.HSFGroup;
import com.aliexpress.boot.hsf.HsfConsumerAutoConfiguration;
import com.taobao.cun.ar.scene.station.service.PartnerLifecycleCallbackService;
import com.taobao.cun.ar.scene.station.service.StationLifecycleCallbackService;
import com.taobao.cun.auge.msg.service.MessageService;
import com.taobao.cun.auge.org.service.CuntaoOrgService;
import com.taobao.cun.auge.user.service.CuntaoUserService;
import com.taobao.cun.chronus.service.TaskSubmitService;
import com.taobao.cun.crius.bpm.service.CuntaoWorkFlowService;
import com.taobao.cun.crius.data.service.PartnerInstanceLevelDataService;
import com.taobao.cun.crius.exam.service.ExamInstanceService;
import com.taobao.cun.crius.exam.service.ExamUserDispatchService;
import com.taobao.cun.service.alipay.AlipayAccountTagService;
import com.taobao.cun.service.alipay.AlipayStandardBailService;
import com.taobao.cun.service.asset.CuntaoAssetService;
import com.taobao.cun.service.trade.TaobaoTradeOrderQueryService;
import com.taobao.cun.service.uic.PaymentAccountQueryService;
import com.taobao.hsf.app.spring.util.HSFSpringConsumerBean;

@Configuration
public class HsfConsumerConfiguration extends HsfConsumerAutoConfiguration {

	// chronus服务
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean taskSubmitService(@Value("${hsf.consumer.version.chronus.taskSubmitService}") String version) {
		return getConsumerBean(TaskSubmitService.class, HSFGroup.HSF, version, 3000);
	}

	// crius服务
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean cuntaoWorkFlowService(@Value("${hsf.consumer.version.crius.cuntaoWorkFlowService}") String version) {
		return getConsumerBean(CuntaoWorkFlowService.class, HSFGroup.HSF, version, 10000);
	}
	
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean partnerInstanceLevelDataService(@Value("${hsf.consumer.version.crius.partnerInstanceLevelDataService}") String version) {
		return getConsumerBean(PartnerInstanceLevelDataService.class, HSFGroup.HSF, version, 3000);
	}

	// cuntaocenter服务
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean taobaoTradeOrderQueryService(
			@Value("${hsf.consumer.version.cuntaocenter.taobaoTradeOrderQueryService}") String version) {
		return getConsumerBean(TaobaoTradeOrderQueryService.class, HSFGroup.HSF, version, 3000);
	}
	
	// cuntaocenter服务
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean cuntaoAssetMService (
			@Value("${hsf.consumer.version.cuntaocenter.cuntaoAssetService}") String version) {
		return getConsumerBean(CuntaoAssetService.class, HSFGroup.HSF, version, 3000);
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
	
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean augeCuntaoOrgService(
			@Value("${hsf.consumer.version.auge.cuntaoOrgService}") String version) {
		return getConsumerBean(CuntaoOrgService.class, HSFGroup.HSF, version, 3000);
	}
	
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean augeCuntaoUserService(
			@Value("${hsf.consumer.version.auge.cuntaoOrgService}") String version) {
		return getConsumerBean(CuntaoUserService.class, HSFGroup.HSF, version, 3000);
	}
	
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean examUserDispatchService(
			@Value("${hsf.consumer.version.crius.examUserDispatchService}") String version) {
		return getConsumerBean(ExamUserDispatchService.class, HSFGroup.HSF, version, 3000);
	}
	
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean examInstanceService(
			@Value("${hsf.consumer.version.crius.examInstanceService}") String version) {
		return getConsumerBean(ExamInstanceService.class, HSFGroup.HSF, version, 3000);
	}
}
