package com.taobao.cun.auge.configuration;

import com.aliexpress.boot.hsf.HSFGroup;
import com.aliexpress.boot.hsf.consumer.HsfConsumerContext;
import com.taobao.cun.appResource.service.AppResourceService;
import com.taobao.cun.ar.scene.station.service.PartnerLifecycleCallbackService;
import com.taobao.cun.ar.scene.station.service.StationLifecycleCallbackService;
import com.taobao.cun.attachment.service.AttachmentService;
import com.taobao.cun.auge.data.PartnerInstanceLevelDataService;
import com.taobao.cun.auge.msg.service.MessageService;
import com.taobao.cun.auge.org.service.CuntaoOrgService;
import com.taobao.cun.auge.org.service.CuntaoOrgServiceClient;
import com.taobao.cun.auge.org.service.impl.CuntaoOrgServiceClientImpl;
import com.taobao.cun.auge.platform.service.BusiWorkBaseInfoService;
import com.taobao.cun.auge.questionnaire.service.QuestionnireManageService;
import com.taobao.cun.auge.user.service.CuntaoUserRoleService;
import com.taobao.cun.auge.user.service.CuntaoUserService;
import com.taobao.cun.chronus.service.TaskSubmitService;
import com.taobao.cun.crius.bpm.service.CuntaoWorkFlowService;
import com.taobao.cun.crius.exam.service.ExamInstanceService;
import com.taobao.cun.crius.exam.service.ExamUserDispatchService;
import com.taobao.cun.recruit.partner.service.AddressInfoDecisionService;
import com.taobao.cun.recruit.partner.service.PartnerQualifyApplyService;
import com.taobao.cun.recruit.partner.service.ServiceAbilityDecisionService;
import com.taobao.cun.settle.bail.service.CuntaoNewBailService;
import com.taobao.hsf.app.spring.util.HSFSpringConsumerBean;
import com.taobao.hsf.app.spring.util.annotation.HSFConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HsfConsumerConfiguration  {

	@HSFConsumer(serviceVersion="${hsf.consumer.version.chronus.taskSubmitService}",serviceGroup="HSF")
	private TaskSubmitService taskSubmitService;
	
	// chronus服务
	//@Bean(initMethod = "init")
	//public HSFSpringConsumerBean taskSubmitService(
	//		@Value("${hsf.consumer.version.chronus.taskSubmitService}") String version) {
	//	return getConsumerBean(TaskSubmitService.class, HSFGroup.HSF, version,
	//			3000);
	//}

	@HSFConsumer(serviceVersion="${hsf.consumer.version.crius.cuntaoWorkFlowService}",serviceGroup="HSF")
	private CuntaoWorkFlowService cuntaoWorkFlowService;
	
	// crius服务
	//@Bean(initMethod = "init")
	//public HSFSpringConsumerBean cuntaoWorkFlowService(
	//		@Value("${hsf.consumer.version.crius.cuntaoWorkFlowService}") String version) {
	//	return getConsumerBean(CuntaoWorkFlowService.class, HSFGroup.HSF,
	//			version, 10000);
	//}

	@HSFConsumer(serviceVersion="${hsf.consumer.version.crius.appResourceService}",serviceGroup="HSF")
	private AppResourceService appResourceService;
	
	
	//@Bean(initMethod = "init")
	//public HSFSpringConsumerBean appResourceService(
	//		@Value("${hsf.consumer.version.crius.appResourceService}") String version) {
	//	return getConsumerBean(AppResourceService.class, HSFGroup.HSF, version,
	//			10000);
	//}

	@HSFConsumer(serviceVersion="${hsf.consumer.version.crius.appResourceService}",serviceGroup="HSF")
	private AttachmentService AttachmentService;
	
	// FIXME 配置项未换
	//@Bean(initMethod = "init")
	//public HSFSpringConsumerBean criusAttachmentService(
	//		@Value("${hsf.consumer.version.crius.appResourceService}") String version) {
	//	return getConsumerBean(AttachmentService.class, HSFGroup.HSF, version,
	//			10000);
	//}

	@HSFConsumer(serviceVersion="${hsf.consumer.version.data.partnerInstanceLevelDataService}",serviceGroup="HSF")
	private PartnerInstanceLevelDataService partnerInstanceLevelDataService;
	
	
	//@Bean(initMethod = "init")
	//public HSFSpringConsumerBean partnerInstanceLevelDataService(
	//		@Value("${hsf.consumer.version.data.partnerInstanceLevelDataService}") String version) {
	//	return getConsumerBean(PartnerInstanceLevelDataService.class,
	//			HSFGroup.HSF, version, 7000);
	//}

	@HSFConsumer(serviceVersion="${hsf.consumer.version.auge.messageService}",serviceGroup="HSF")
	private MessageService messageService;
	
	
	//@Bean(initMethod = "init")
//	public HSFSpringConsumerBean messageService(
	//		@Value("${hsf.consumer.version.auge.messageService}") String version) {
	//	return getConsumerBean(MessageService.class, HSFGroup.HSF, version,
	//			3000);
	//}

	@HSFConsumer(serviceVersion="${hsf.consumer.version.admin.partnerLifecycleCallbackService}",serviceGroup="HSF")
	private PartnerLifecycleCallbackService partnerLifecycleCallbackService;
	
	//admin服务
	//@Bean(initMethod = "init")
	//public HSFSpringConsumerBean partnerLifecycleCallbackService(
	//		@Value("${hsf.consumer.version.admin.partnerLifecycleCallbackService}") String version) {
	//	return getConsumerBean(PartnerLifecycleCallbackService.class,
	//			HSFGroup.HSF, version, 3000);
	//}

	@HSFConsumer(serviceVersion="${hsf.consumer.version.admin.stationLifecycleCallbackService}",serviceGroup="HSF")
	private StationLifecycleCallbackService stationLifecycleCallbackService;
	
	// admin服务
	//@Bean(initMethod = "init")
	//public HSFSpringConsumerBean stationLifecycleCallbackService(
	//		@Value("${hsf.consumer.version.admin.stationLifecycleCallbackService}") String version) {
	//	return getConsumerBean(StationLifecycleCallbackService.class,
	//			HSFGroup.HSF, version, 3000);
	//}

	//@HSFConsumer(serviceVersion="${hsf.consumer.version.auge.cuntaoOrgService}",serviceGroup="HSF")
	//private CuntaoOrgService cuntaoOrgService;
	
	
	//@Bean(initMethod = "init")
	//public HSFSpringConsumerBean augeCuntaoOrgService(
	//		@Value("${hsf.consumer.version.auge.cuntaoOrgService}") String version) {
	//	return getConsumerBean(CuntaoOrgService.class, HSFGroup.HSF, version,
	//			3000);
	//}

	//@HSFConsumer(serviceVersion="${hsf.consumer.version.auge.cuntaoOrgService}",serviceGroup="HSF")
//	private CuntaoUserService augeCuntaoUserService;
	
	//@Bean(initMethod = "init")
	//public HSFSpringConsumerBean augeCuntaoUserService(
	//		@Value("${hsf.consumer.version.auge.cuntaoOrgService}") String version) {
	//	return getConsumerBean(CuntaoUserService.class, HSFGroup.HSF, version,
	//			3000);
	//}

	@Bean
	public CuntaoUserService augeCuntaoUserService(HsfConsumerContext context, @Value("${hsf.consumer.version.auge.cuntaoOrgService}") String version) {
		return context.hsfConsumerBuilder(CuntaoUserService.class, HSFGroup.HSF.name(), version).clientTimeout(5000)
				.build();
	}
	
	@Bean
	public CuntaoOrgService augeCuntaoOrgService(HsfConsumerContext context, @Value("${hsf.consumer.version.auge.cuntaoOrgService}") String version) {
		return context.hsfConsumerBuilder(CuntaoOrgService.class, HSFGroup.HSF.name(), version).clientTimeout(5000)
				.build();
	}
	
	@Bean(initMethod = "init")
	public CuntaoOrgServiceClient cuntaoOrgServiceClient(
			CuntaoUserService augeCuntaoUserService,
			CuntaoOrgService augeCuntaoOrgService) {
		CuntaoOrgServiceClientImpl cuntaoOrgServiceClient = new CuntaoOrgServiceClientImpl();
		cuntaoOrgServiceClient.setAugeCuntaoOrgService(augeCuntaoOrgService);
		cuntaoOrgServiceClient.setAugeCuntaoUserService(augeCuntaoUserService);
		return cuntaoOrgServiceClient;
	}

	@HSFConsumer(serviceVersion="${hsf.consumer.version.crius.examUserDispatchService}",serviceGroup="HSF")
	private ExamUserDispatchService examUserDispatchService;
	
	
//	@Bean(initMethod = "init")
//    public HSFSpringConsumerBean partnerQualifyApplyService(
//            @Value("${hsf.consumer.version.auge.partnerQualifyApplyService}") String version) {
//        return getConsumerBean(PartnerQualifyApplyService.class, HSFGroup.HSF,
//                version, 5000);
//    }
	

    @HSFConsumer(serviceVersion="${hsf.consumer.version.auge.serviceAbilityDecisionService}",serviceGroup="HSF")
    private ServiceAbilityDecisionService serviceAbilityDecisionService;
//	@Bean(initMethod = "init")
//    public HSFSpringConsumerBean serviceAbilityDecisionService(
//            @Value("${hsf.consumer.version.auge.serviceAbilityDecisionService}") String version) {
//        return getConsumerBean(ServiceAbilityDecisionService.class, HSFGroup.HSF,
//                version, 5000);
//    }
	
    @HSFConsumer(serviceVersion="${hsf.consumer.version.auge.addressInfoDecisionService}",serviceGroup="HSF")
    private AddressInfoDecisionService addressInfoDecisionService;
//	@Bean(initMethod = "init")
//    public HSFSpringConsumerBean addressInfoDecisionService(
//            @Value("${hsf.consumer.version.auge.addressInfoDecisionService}") String version) {
//        return getConsumerBean(AddressInfoDecisionService.class, HSFGroup.HSF,
//                version, 5000);
//    }
	//@Bean(initMethod = "init")
//	public HSFSpringConsumerBean examUserDispatchService(
	//		@Value("${hsf.consumer.version.crius.examUserDispatchService}") String version) {
	//	return getConsumerBean(ExamUserDispatchService.class, HSFGroup.HSF,
	//			version, 3000);
	//}

	@HSFConsumer(serviceVersion="${hsf.consumer.version.crius.examInstanceService}",serviceGroup="HSF")
	private ExamInstanceService examInstanceService;
	
	//@Bean(initMethod = "init")
//	public HSFSpringConsumerBean examInstanceService(
	//		@Value("${hsf.consumer.version.crius.examInstanceService}") String version) {
	//	return getConsumerBean(ExamInstanceService.class, HSFGroup.HSF,
	//			version, 3000);
//	}

	@HSFConsumer(serviceVersion="${hsf.consumer.version.auge.busiWorkBaseInfoService}",serviceGroup="HSF")
	private BusiWorkBaseInfoService busiWorkBaseInfoService;
	
	
	//@Bean(initMethod = "init")
	//public HSFSpringConsumerBean busiWorkBaseInfoService(
	//		@Value("${hsf.consumer.version.auge.busiWorkBaseInfoService}") String version) {
	//	return getConsumerBean(BusiWorkBaseInfoService.class, HSFGroup.HSF,
	//			version, 3000);
	//}

	@HSFConsumer(serviceVersion="${hsf.consumer.version.cunbiz.questionnireManageService}",serviceGroup="HSF")
	private QuestionnireManageService questionnireManageService;
	
	
	//@Bean(initMethod = "init")
	//public HSFSpringConsumerBean questionnireManageService(
	//		@Value("${hsf.consumer.version.cunbiz.questionnireManageService}") String version) {
	//	return getConsumerBean(QuestionnireManageService.class, HSFGroup.HSF,
	//			version, 10000);
//	}

	@HSFConsumer(serviceVersion="${hsf.consumer.version.settle.cuntaoNewBailService}",serviceGroup="HSF")
	private CuntaoNewBailService cuntaoNewBailService;
	
	//@Bean(initMethod = "init")
	//public HSFSpringConsumerBean cuntaoNewBailService(
	//		@Value("${hsf.consumer.version.settle.cuntaoNewBailService}") String version) {
	//	return getConsumerBean(CuntaoNewBailService.class, HSFGroup.HSF,
	//			version, 5000);
	//}
	
	@HSFConsumer(serviceVersion="${hsf.consumer.version.auge.cuntaoOrgService}",serviceGroup="HSF")
	private CuntaoUserRoleService CuntaoUserRoleService;
	
	//@Bean(initMethod = "init")
   // public HSFSpringConsumerBean cuntaoUserRoleService(
   //         @Value("${hsf.consumer.version.auge.cuntaoOrgService}") String version) {
   //     return getConsumerBean(CuntaoUserRoleService.class, HSFGroup.HSF,
   //             version, 5000);
   // }
	
	@HSFConsumer(serviceVersion="${hsf.consumer.version.auge.partnerQualifyApplyService}",serviceGroup="HSF")
	private PartnerQualifyApplyService partnerQualifyApplyService;
	
	//@Bean(initMethod = "init")
   // public HSFSpringConsumerBean partnerQualifyApplyService(
    //        @Value("${hsf.consumer.version.auge.partnerQualifyApplyService}") String version) {
    //    return getConsumerBean(PartnerQualifyApplyService.class, HSFGroup.HSF,
    //            version, 5000);
   // }

}