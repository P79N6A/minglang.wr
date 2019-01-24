package com.taobao.cun.auge.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.cuntao.ctsm.client.service.read.StoreSReadService;
import com.alibaba.cuntao.ctsm.client.service.write.StoreSWriteService;
import com.aliexpress.boot.hsf.HSFGroup;
import com.aliexpress.boot.hsf.consumer.HsfConsumerContext;
import com.taobao.cun.appResource.service.AppResourceService;
import com.taobao.cun.ar.scene.station.service.PartnerLifecycleCallbackService;
import com.taobao.cun.ar.scene.station.service.StationLifecycleCallbackService;
import com.taobao.cun.attachment.service.AttachmentService;
import com.taobao.cun.auge.data.PartnerInstanceLevelDataService;
import com.taobao.cun.auge.ddy.service.DdyLbsQueryService;
import com.taobao.cun.auge.msg.service.MessageService;
import com.taobao.cun.auge.org.service.CuntaoOrgService;
import com.taobao.cun.auge.org.service.CuntaoOrgServiceClient;
import com.taobao.cun.auge.org.service.impl.CuntaoOrgServiceClientImpl;
import com.taobao.cun.auge.platform.service.BusiWorkBaseInfoService;
import com.taobao.cun.auge.questionnaire.service.QuestionnireManageService;
import com.taobao.cun.auge.sop.inspection.service.InspectionService;
import com.taobao.cun.auge.task.service.TaskElementService;
import com.taobao.cun.auge.user.service.CuntaoUserRoleService;
import com.taobao.cun.auge.user.service.CuntaoUserService;
import com.taobao.cun.chronus.service.TaskSubmitService;
import com.taobao.cun.crius.bpm.service.CuntaoWorkFlowService;
import com.taobao.cun.crius.exam.service.ExamInstanceService;
import com.taobao.cun.crius.exam.service.ExamUserDispatchService;
import com.taobao.cun.recruit.partner.service.AddressInfoDecisionService;
import com.taobao.cun.recruit.partner.service.PartnerQualifyApplyService;
import com.taobao.cun.settle.bail.service.CuntaoNewBailService;
import com.taobao.hsf.app.spring.util.annotation.HSFConsumer;

@Configuration
public class HsfConsumerConfiguration  {

	@HSFConsumer(serviceVersion="${hsf.consumer.version.chronus.taskSubmitService}",serviceGroup="HSF")
	private TaskSubmitService taskSubmitService;
	
	@HSFConsumer(serviceVersion="${hsf.consumer.version.crius.cuntaoWorkFlowService}",serviceGroup="HSF", clientTimeout = 10000)
	private CuntaoWorkFlowService cuntaoWorkFlowService;
	
	@HSFConsumer(serviceVersion="${hsf.consumer.version.crius.appResourceService}",serviceGroup="HSF")
	private AppResourceService appResourceService;
	
	@HSFConsumer(serviceVersion="${hsf.consumer.version.crius.appResourceService}",serviceGroup="HSF")
	private AttachmentService AttachmentService;
	
	@HSFConsumer(serviceVersion="${hsf.consumer.version.data.partnerInstanceLevelDataService}",serviceGroup="HSF")
	private PartnerInstanceLevelDataService partnerInstanceLevelDataService;
	
	@HSFConsumer(serviceVersion="${hsf.consumer.version.auge.messageService}",serviceGroup="HSF")
	private MessageService messageService;
	

	@HSFConsumer(serviceVersion="${hsf.consumer.version.admin.partnerLifecycleCallbackService}",serviceGroup="HSF")
	private PartnerLifecycleCallbackService partnerLifecycleCallbackService;
	

	@HSFConsumer(serviceVersion="${hsf.consumer.version.admin.stationLifecycleCallbackService}",serviceGroup="HSF")
	private StationLifecycleCallbackService stationLifecycleCallbackService;
	
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
	
    @HSFConsumer(serviceVersion="${hsf.consumer.version.auge.addressInfoDecisionService}",serviceGroup="HSF")
    private AddressInfoDecisionService addressInfoDecisionService;

	@HSFConsumer(serviceVersion="${hsf.consumer.version.crius.examInstanceService}",serviceGroup="HSF")
	private ExamInstanceService examInstanceService;

	@HSFConsumer(serviceVersion="${hsf.consumer.version.auge.busiWorkBaseInfoService}",serviceGroup="HSF")
	private BusiWorkBaseInfoService busiWorkBaseInfoService;

	@HSFConsumer(serviceVersion="${hsf.consumer.version.cunbiz.questionnireManageService}",serviceGroup="HSF")
	private QuestionnireManageService questionnireManageService;
	
	@HSFConsumer(serviceVersion="${hsf.consumer.version.settle.cuntaoNewBailService}",serviceGroup="HSF")
	private CuntaoNewBailService cuntaoNewBailService;
	
	@HSFConsumer(serviceVersion="${hsf.consumer.version.auge.cuntaoOrgService}",serviceGroup="HSF")
	private CuntaoUserRoleService CuntaoUserRoleService;
	
	@HSFConsumer(serviceVersion="${hsf.consumer.version.auge.partnerQualifyApplyService}",serviceGroup="HSF")
	private PartnerQualifyApplyService partnerQualifyApplyService;
	
	@HSFConsumer(serviceVersion="${hsf.consumer.version.auge.inspectionService}",serviceGroup="HSF")
	private InspectionService inspectionService;
	
	
	@HSFConsumer(serviceVersion = "${spring.hsf.version}", serviceGroup = "HSF", clientTimeout = 10000)
	private DdyLbsQueryService ddyLbsQueryService;



	@HSFConsumer(serviceVersion="${hsf.task.element.taskElementService.version}",serviceGroup="HSF")
	private TaskElementService taskElementService;
	
	@HSFConsumer(serviceVersion = "${hsf.storeSReadService.version}", serviceGroup = "HSF", clientTimeout = 10000)
	private StoreSReadService storeSReadService;
	@HSFConsumer(serviceVersion = "${hsf.storeSReadService.version}", serviceGroup = "HSF", clientTimeout = 10000)
	private StoreSWriteService storeSWriteService;
	

}