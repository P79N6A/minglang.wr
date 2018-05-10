package com.taobao.cun.auge.configuration;

import java.util.ArrayList;
import java.util.List;

import org.esb.finance.service.audit.EsbFinanceAuditAdapter;
import org.esb.finance.service.contract.EsbFinanceContractAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ali.dowjones.service.portal.OrderPortalService;
import com.ali.dowjones.service.portal.ProductService;
import com.ali.dowjones.service.portal.ShoppingCartPortalService;
import com.ali.martini.biz.order.interfaces.orderitem.facade.OrderItemFacade;
import com.alibaba.buc.acl.api.service.AccessControlService;
import com.alibaba.buc.api.EnhancedUserQueryService;
import com.alibaba.cainiao.cuntaonetwork.service.station.StationReadService;
import com.alibaba.cainiao.cuntaonetwork.service.station.StationUserWriteService;
import com.alibaba.cainiao.cuntaonetwork.service.station.StationWriteService;
import com.alibaba.cainiao.cuntaonetwork.service.warehouse.CountyDomainWriteService;
import com.alibaba.cainiao.cuntaonetwork.service.warehouse.WarehouseReadService;
import com.alibaba.cainiao.cuntaonetwork.service.warehouse.WarehouseWriteService;
import com.alibaba.ceres.commonservice.po.PoQueryService;
import com.alibaba.ceres.service.category.CategoryService;
import com.alibaba.ceres.service.pr.PrService;
import com.alibaba.china.member.service.MemberReadService;
import com.alibaba.crm.pacific.facade.refund.ApplyRefundService;
import com.alibaba.crm.pacific.facade.refund.RefundBaseCommonService;
import com.alibaba.crm.pacific.facade.refund.RefundForBizAuditService;
import com.alibaba.ivy.service.course.CourseServiceFacade;
import com.alibaba.ivy.service.user.TrainingRecordServiceFacade;
import com.alibaba.ivy.service.user.TrainingTicketServiceFacade;
import com.alibaba.masterdata.client.service.Employee360Service;
import com.alibaba.organization.api.orgstruct.service.OrgStructReadService;
import com.alibaba.organization.api.orgstruct.service.OrgStructWriteService;
import com.alibaba.pm.sc.api.quali.SellerQualiService;
import com.alibaba.pm.sc.portal.api.ScPortalService;
import com.alibaba.pm.sc.portal.api.quali.QLCAccessService;
import com.alibaba.pm.sc.portal.api.quali.QualiAccessService;
import com.alibaba.tax.api.service.ArInvoiceService;
import com.aliexpress.boot.hsf.HSFGroup;
import com.aliexpress.boot.hsf.consumer.HsfConsumerContext;
import com.alipay.baoxian.scene.facade.common.policy.service.PolicyQueryService;
import com.taobao.cun.ar.scene.station.service.PartnerTagService;
import com.taobao.cun.auge.incentive.service.IncentiveProgramQueryService;
import com.taobao.cun.auge.incentive.service.IncentiveProgramService;
import com.taobao.cun.auge.msg.service.MessageService;
import com.taobao.cun.auge.opensearch.OpenSearchManager;
import com.taobao.cun.auge.opensearch.OpenSearchParser;
import com.taobao.cun.auge.opensearch.StationQueryOpenSearchParser;
import com.taobao.cun.endor.base.client.EndorApiClient;
import com.taobao.cun.endor.base.client.impl.EndorApiClientImpl;
import com.taobao.cun.mdjxc.api.CtMdJxcWarehouseApi;
import com.taobao.cun.recruit.partner.service.PartnerApplyService;
import com.taobao.cun.settle.cae.service.SellerSignService;
import com.taobao.hsf.app.spring.util.annotation.HSFConsumer;
import com.taobao.mmp.client.permission.service.MmpAuthReadService;
import com.taobao.namelist.service.NamelistMatchService;
import com.taobao.payment.account.service.AccountManageService;
import com.taobao.payment.account.service.query.AccountQueryService;
import com.taobao.place.client.service.StoreService;
import com.taobao.place.client.service.area.StandardAreaService;
import com.taobao.refundplatform.client.read.RefundReadService;
import com.taobao.tc.service.TcBaseService;
import com.taobao.trade.platform.api.query.BuyerQueryService;
import com.taobao.uic.common.cache.UICCacheService;
import com.taobao.uic.common.service.userdata.UicDataReadService;
import com.taobao.uic.common.service.userdata.UicDataWriteService;
import com.taobao.uic.common.service.userdata.client.TagRecordServiceClient;
import com.taobao.uic.common.service.userdata.client.UicDataServiceClient;
import com.taobao.uic.common.service.userdata.client.UicTagServiceClient;
import com.taobao.uic.common.service.userinfo.TagRecordReadService;
import com.taobao.uic.common.service.userinfo.TagRecordWriteService;
import com.taobao.uic.common.service.userinfo.UicPaymentAccountReadService;
import com.taobao.uic.common.service.userinfo.UicReadService;
import com.taobao.uic.common.service.userinfo.client.UicExtraReadServiceClient;
import com.taobao.uic.common.service.userinfo.client.UicPaymentAccountReadServiceClient;
import com.taobao.uic.common.service.userinfo.client.UicTagWriteServiceClient;
import com.taobao.uic.common.util.ClientInfo;
import com.taobao.wws.hsf2icesrv;
@Configuration
public class HsfConsumer2ndPartyConfiguration  {

	@HSFConsumer(serviceVersion="${hsf.consumer.version.employeeService}",serviceGroup="HSF")
	private Employee360Service employee360Service;
	
	// hr相关的第二方服务
	//@Bean(initMethod = "init")
	//public HSFSpringConsumerBean employee360Service(@Value("${hsf.consumer.version.employeeService}") String version) {
	//	return getConsumerBean(Employee360Service.class, HSFGroup.HSF, version, 3000);
	//}
	
	@HSFConsumer(serviceVersion="${hsf.consumer.version.hsf2icesrv}",serviceGroup="HSF")
	private hsf2icesrv hsf2icesrv;
	
	//旺旺
	//@Bean(initMethod = "init")
	//public HSFSpringConsumerBean hsf2icesrv(@Value("${hsf.consumer.version.hsf2icesrv}") String version) {
	//	return getConsumerBean(com.taobao.wws.hsf2icesrv.class, HSFGroup.HSF, version, 3000);
	//}

	@HSFConsumer(serviceVersion="${hsf.consumer.version.cainiao.countyDomainWriteService}",serviceGroup="HSF")
	private CountyDomainWriteService countyDomainWriteService;
	// 菜鸟服务
	//@Bean(initMethod = "init")
	//public HSFSpringConsumerBean countyDomainWriteService(@Value("${hsf.consumer.version.cainiao.countyDomainWriteService}") String version) {
	//	return getConsumerBean(CountyDomainWriteService.class, HSFGroup.HSF, version, 3000);
	//}

	@HSFConsumer(serviceVersion="${hsf.consumer.version.cainiao.stationWriteService}",serviceGroup="HSF")
	private StationWriteService stationWriteService;
	
	//@Bean(initMethod = "init")
	//public HSFSpringConsumerBean stationWriteService(@Value("${hsf.consumer.version.cainiao.stationWriteService}") String version) {
	//	return getConsumerBean(StationWriteService.class, HSFGroup.HSF, version, 3000);
	//}

	@HSFConsumer(serviceVersion="${hsf.consumer.version.cainiao.stationUserWriteService}",serviceGroup="HSF")
	private StationUserWriteService stationUserWriteService;
	
	//@Bean(initMethod = "init")
	//public HSFSpringConsumerBean stationUserWriteService(@Value("${hsf.consumer.version.cainiao.stationUserWriteService}") String version) {
	//	return getConsumerBean(StationUserWriteService.class, HSFGroup.HSF, version, 3000);
	//}

	@HSFConsumer(serviceVersion="${partner.peixun.service.version}",serviceGroup="HSF")
	private TrainingRecordServiceFacade trainingRecordServiceFacade;
	
	//@Bean(initMethod = "init")
	//public HSFSpringConsumerBean trainingRecordServiceFacade(@Value("${partner.peixun.service.version}") String version) {
	//	return getConsumerBean(TrainingRecordServiceFacade.class, HSFGroup.HSF, version, 3000);
	//}
	
	@HSFConsumer(serviceVersion="${partner.peixun.service.version}",serviceGroup="HSF")
	private CourseServiceFacade courseServiceFacade;
	
	//@Bean(initMethod = "init")
	//public HSFSpringConsumerBean courseServiceFacade(@Value("${partner.peixun.service.version}") String version) {
	//	return getConsumerBean(CourseServiceFacade.class, HSFGroup.HSF, version, 3000);
	//}
	
	
	@HSFConsumer(serviceVersion="${partner.peixun.service.version}",serviceGroup="HSF")
	private TrainingTicketServiceFacade trainingTicketServiceFacade;
	
	
	//@Bean(initMethod = "init")
	//public HSFSpringConsumerBean trainingTicketServiceFacade(@Value("${partner.peixun.service.version}") String version) {
	//	return getConsumerBean(TrainingTicketServiceFacade.class, HSFGroup.HSF, version, 3000);
	//}
	
	@HSFConsumer(serviceVersion="${tcBaseService.service.version}",serviceGroup="HSF")
	private TcBaseService tcBaseService;
	
	//@Bean(initMethod = "init")
	//public HSFSpringConsumerBean tcBaseService (@Value("${tcBaseService.service.version}") String version) {
	//	return getConsumerBean(TcBaseService.class, HSFGroup.HSF, version, 3000);
	//}
	
	@HSFConsumer(serviceVersion="${accessControlService.service.version}",serviceGroup="HSF")
	private AccessControlService accessControlService;
	
	
	//@Bean(initMethod = "init")
	//public HSFSpringConsumerBean accessControlService(@Value("${accessControlService.service.version}") String version) {
	//	return getConsumerBean(AccessControlService.class, HSFGroup.HSF, version, 3000);
	//}
	
	@HSFConsumer(serviceVersion="${dowjonesProductService.service.version}",serviceGroup="DUBBO")
	private ProductService productService;
	
	
	//@Bean(initMethod = "init")
	//public HSFSpringConsumerBean productService(@Value("${dowjonesProductService.service.version}") String version) {
	//	return getConsumerBean(ProductService.class, HSFGroup.DUBBO, version, 3000);
	//}
	
	@HSFConsumer(serviceVersion="${dowjonesOrderService.service.portal.version}",serviceGroup="DUBBO")
	private OrderPortalService orderPortalService;
	
	//@Bean(initMethod = "init")
	//public HSFSpringConsumerBean orderPortalService(@Value("${dowjonesOrderService.service.portal.version}") String version) {
	//	return getConsumerBean(OrderPortalService.class, HSFGroup.DUBBO, version, 3000);
	//}
	
	@HSFConsumer(serviceVersion="${dowjonesProductService.service.version}",serviceGroup="HSF")
	private ShoppingCartPortalService shoppingCartPortalService;
	
	
	//@Bean(initMethod = "init")
	//public HSFSpringConsumerBean shoppingCartPortalService(@Value("${dowjonesProductService.service.version}") String version) {
	//	return getConsumerBean(ShoppingCartPortalService.class, HSFGroup.DUBBO, version, 3000);
	//}
	
	@HSFConsumer(serviceVersion="${crm.order.service.version}",serviceGroup="DUBBO")
	private OrderItemFacade orderItemFacade;
	
	
	//@Bean(initMethod = "init")
	//public HSFSpringConsumerBean orderItemFacade(@Value("${crm.order.service.version}") String version) {
	//	return getConsumerBean(OrderItemFacade.class, HSFGroup.DUBBO, version, 3000);
	//}
	
	@HSFConsumer(serviceVersion="${crm.finance.service.version}",serviceGroup="HSF")
	private EsbFinanceAuditAdapter esbFinanceAuditAdapter;
	
	
	//@Bean(initMethod = "init")
	//public HSFSpringConsumerBean esbFinanceAuditAdapter(@Value("${crm.finance.service.version}") String version) {
	//	return getConsumerBean(EsbFinanceAuditAdapter.class, HSFGroup.HSF, version, 3000);
	//}
	
	@HSFConsumer(serviceVersion="${crm.finance.service.version}",serviceGroup="HSF")
	private EsbFinanceContractAdapter esbFinanceContractAdapter;
	
	
	//@Bean(initMethod = "init")
	//public HSFSpringConsumerBean esbFinanceContractAdapter(@Value("${crm.finance.service.version}") String version) {
	//	return getConsumerBean(EsbFinanceContractAdapter.class, HSFGroup.HSF, version, 3000);
	//}
	
	@HSFConsumer(serviceVersion="${alibaba.ceres.version}",serviceGroup="HSF")
	private PrService prService;
	
	
	//@Bean(initMethod = "init")
	//public HSFSpringConsumerBean prService(@Value("${alibaba.ceres.version}") String version) {
	//	return getConsumerBean(PrService.class, HSFGroup.HSF, version, 30000);
	//}
	

	@HSFConsumer(serviceVersion="${alibaba.ceres.version}",serviceGroup="HSF")
	private com.alibaba.ceres.service.catalog.ProductService productService2;
	
	
	//@Bean(initMethod = "init")
	//public HSFSpringConsumerBean ceresProductService(@Value("${alibaba.ceres.version}") String version) {
	//	return getConsumerBean(com.alibaba.ceres.service.catalog.ProductService.class, HSFGroup.HSF, version, 3000);
	//}
	
	@HSFConsumer(serviceVersion="${alibaba.ceres.version}",serviceGroup="HSF")
	private CategoryService categoryService;
	
	//@Bean(initMethod = "init")
	//public HSFSpringConsumerBean ceresCategoryService(@Value("${alibaba.ceres.version}") String version) {
	//	return getConsumerBean(CategoryService.class, HSFGroup.HSF, version, 3000);
	//}
	
	@HSFConsumer(serviceVersion="${hsf.consumer.version.cainiao.stationReadService}",serviceGroup="HSF")
	private StationReadService stationReadService;
	
	
	//@Bean(initMethod = "init")
	//public HSFSpringConsumerBean stationReadService(@Value("${hsf.consumer.version.cainiao.stationReadService}") String version) {
	//	return getConsumerBean(StationReadService.class, HSFGroup.HSF, version, 3000);
	//}
	
	@HSFConsumer(serviceVersion="${taobao.uic.version}",serviceGroup="HSF")
	private TagRecordReadService tagRecordReadService;
	
	
	//@Bean(initMethod = "init")
	//public HSFSpringConsumerBean tagRecordReadService(@Value("${taobao.uic.version}") String version) {
	//	return getConsumerBean(TagRecordReadService.class, HSFGroup.HSF, version, 3000);
	//}
	
	@HSFConsumer(serviceVersion="${taobao.uic.version}",serviceGroup="HSF")
	private TagRecordWriteService tagRecordWriteService;
	
	
	//@Bean(initMethod = "init")
	//public HSFSpringConsumerBean tagRecordWriteService(@Value("${taobao.uic.version}") String version) {
	//	return getConsumerBean(TagRecordWriteService.class, HSFGroup.HSF, version, 3000);
	//}
	
	@HSFConsumer(serviceVersion="${taobao.uic.version}",serviceGroup="HSF")
	private UicDataReadService uicDataReadService;
	
	//@Bean(initMethod = "init")
	//public HSFSpringConsumerBean uicDataReadService(@Value("${taobao.uic.version}") String version) {
	//	return getConsumerBean(UicDataReadService.class, HSFGroup.HSF, version, 3000);
	//}
	
	@HSFConsumer(serviceVersion="${taobao.uic.version}",serviceGroup="HSF")
	private UicDataWriteService uicDataWriteService;
	
	//@Bean(initMethod = "init")
	//public HSFSpringConsumerBean uicDataWriteService(@Value("${taobao.uic.version}") String version) {
	//	return getConsumerBean(UicDataWriteService.class, HSFGroup.HSF, version, 3000);
	//}
	
	 @Bean
	  public TcBaseService archiveTcBaseService(HsfConsumerContext context,@Value("${archiveTcBaseService.version}") String version) {
	      return context.hsfConsumerBuilder(TcBaseService.class,HSFGroup.HSF.name(),version).clientTimeout(5000).build();
	  }
	 
	@Bean
	public TagRecordServiceClient tagRecordServiceClient(TagRecordReadService tagRecordReadService,
			TagRecordWriteService tagRecordWriteService,
			UICCacheService uicCacheService) {
		TagRecordServiceClient tagRecordServiceClient = new TagRecordServiceClient();
		tagRecordServiceClient.setTagRecordWriteService(tagRecordWriteService);
		tagRecordServiceClient.setTagRecordReadService(tagRecordReadService);
		tagRecordServiceClient.setUicCacheService(uicCacheService);
		return tagRecordServiceClient;
	}
	
	@Bean
	public UicDataServiceClient uicDataServiceClient(ClientInfo clientInfo,
			UicDataReadService uicDataReadService,
			UicDataWriteService uicDataWriteService,
			UICCacheService uicCacheService) {
		UicDataServiceClient uicDataServiceClient = new UicDataServiceClient();
		uicDataServiceClient.setClientInfo(clientInfo);
		uicDataServiceClient.setUicDataReadService(uicDataReadService);
		uicDataServiceClient.setUicDataWriteService(uicDataWriteService);
		uicDataServiceClient.setUicCacheService(uicCacheService);
		return uicDataServiceClient;
	}

	@Bean
	public UicTagServiceClient uicTagServiceClient(UicDataServiceClient uicDataServiceClient,
			UicExtraReadServiceClient uicExtraReadServiceClient,
			UicTagWriteServiceClient uicTagWriteServiceClient,
			TagRecordServiceClient tagRecordServiceClient) {
		UicTagServiceClient uicTagServiceClient = new UicTagServiceClient();
		uicTagServiceClient.setUicDataServiceClient(uicDataServiceClient);
		uicTagServiceClient.setUicExtraReadServiceClient(uicExtraReadServiceClient);
		uicTagServiceClient.setUicTagWriteServiceClient(uicTagWriteServiceClient);
		uicTagServiceClient.setTagRecordServiceClient(tagRecordServiceClient);
		return uicTagServiceClient;
	}
	
	
	@HSFConsumer(serviceVersion="${hsf.consumer.version.enhancedUserQueryService}",serviceGroup="HSF")
	private EnhancedUserQueryService enhancedUserQueryService;
	
	
	//@Bean(initMethod = "init")
	//public HSFSpringConsumerBean enhancedUserQueryService(@Value("${hsf.consumer.version.enhancedUserQueryService}") String version) {
	//	return getConsumerBean(EnhancedUserQueryService.class, HSFGroup.HSF, version, 3000);
	//}


	@HSFConsumer(serviceVersion="${hsf.consumer.version.incentiveProgramQueryService}",serviceGroup="HSF")
	private IncentiveProgramQueryService incentiveProgramQueryService;
	
	
	
	//@Bean(initMethod = "init")
	//public HSFSpringConsumerBean incentiveProgramQueryService(@Value("${hsf.consumer.version.incentiveProgramQueryService}") String version) {
	//	return getConsumerBean(IncentiveProgramQueryService.class, HSFGroup.HSF, version, 8000);
	//}

	@HSFConsumer(serviceVersion="${hsf.consumer.version.incentiveProgramQueryService}",serviceGroup="HSF")
	private IncentiveProgramService incentiveProgramService;
	
	
	//@Bean(initMethod = "init")
	//public HSFSpringConsumerBean incentiveProgramService(@Value("${hsf.consumer.version.incentiveProgramQueryService}") String version) {
	//	return getConsumerBean(IncentiveProgramService.class, HSFGroup.HSF, version, 8000);
	//}

	@Bean
	  public SellerQualiService sellerQualiService(HsfConsumerContext context,@Value("${sellerQualiService.version}") String version) {
	      return context.hsfConsumerBuilder(SellerQualiService.class,HSFGroup.HSF.name(),version).clientTimeout(5000).build();
	  }
	 
	@HSFConsumer(serviceVersion="1.0.0",serviceGroup="HSF")
	private MmpAuthReadService mmpAuthReadService;
	
	
	//@Bean(initMethod = "init")
	 // public HSFSpringConsumerBean mmpAuthReadService(@Value("1.0.0") String version) {
	//    return getConsumerBean(MmpAuthReadService.class, HSFGroup.HSF, version, 3000);
	//  }
	
	 @Bean
	  public QualiAccessService qualiAccessService(HsfConsumerContext context,@Value("${sellerQualiService.version}") String version) {
	      return context.hsfConsumerBuilder(QualiAccessService.class,HSFGroup.HSF.name(),version).clientTimeout(15000).build();
	  }
	 
	@HSFConsumer(serviceVersion="${hsf.consumer.version.cainiao.stationUserWriteService}",serviceGroup="HSF")
	private WarehouseReadService warehouseReadService;
		
	 
	//@Bean(initMethod = "init")
	//public HSFSpringConsumerBean warehouseReadService(
	//		@Value("${hsf.consumer.version.cainiao.stationUserWriteService}") String version) {
	//	return getConsumerBean(WarehouseReadService.class, HSFGroup.HSF,
	//			version, 3000);
	//}

	@HSFConsumer(serviceVersion="${hsf.consumer.version.cainiao.stationUserWriteService}",serviceGroup="HSF")
	private WarehouseWriteService warehouseWriteService;
	
	
	//@Bean(initMethod = "init")
	//public HSFSpringConsumerBean warehouseWriteService(
		//	@Value("${hsf.consumer.version.cainiao.stationUserWriteService}") String version) {
	//	return getConsumerBean(WarehouseWriteService.class, HSFGroup.HSF,
		//		version, 3000);
	//}
	
	@Bean
	public MessageService messageService(HsfConsumerContext context, @Value("${messageService.version}") String version) {
		return context.hsfConsumerBuilder(MessageService.class, HSFGroup.HSF.name(), version).clientTimeout(5000)
				.build();
	}
	
	
	@Bean
	public PartnerApplyService partnerApplyService(HsfConsumerContext context, @Value("${recruit.service.version}") String version) {
		return context.hsfConsumerBuilder(PartnerApplyService.class, HSFGroup.HSF.name(), version).clientTimeout(5000)
				.build();
	}
	
	@Bean
	public ApplyRefundService applyRefundService(HsfConsumerContext context, @Value("${crm.order.service.version}") String version) {
		return context.hsfConsumerBuilder(ApplyRefundService.class, HSFGroup.HSF.name(), version).clientTimeout(5000)
				.build();
	}
	
	@Bean
	public RefundBaseCommonService refundBaseCommonService(HsfConsumerContext context, @Value("${crm.order.service.version}") String version) {
		return context.hsfConsumerBuilder(RefundBaseCommonService.class, HSFGroup.HSF.name(), version).clientTimeout(5000)
				.build();
	}
	
	@Bean
	public RefundForBizAuditService refundForBizAuditService(HsfConsumerContext context, @Value("${crm.order.service.version}") String version) {
		return context.hsfConsumerBuilder(RefundForBizAuditService.class, HSFGroup.HSF.name(), version).clientTimeout(5000)
				.build();
	}
	
	@Bean
	public ArInvoiceService arInvoiceService(HsfConsumerContext context, @Value("${finance.invoice.service.version}") String version) {
		return context.hsfConsumerBuilder(ArInvoiceService.class, HSFGroup.HSF.name(), version).clientTimeout(5000)
				.build();
	}
	
	@Bean
	public BuyerQueryService buyerQueryService(HsfConsumerContext context, @Value("${buyer.query.service.version}") String version) {
		return context.hsfConsumerBuilder(BuyerQueryService.class, HSFGroup.HSF.name(), version).clientTimeout(5000)
				.build();
	}
	
	@Bean
	public RefundReadService refundReadService(HsfConsumerContext context, @Value("${refund.read.service.version}") String version) {
		return context.hsfConsumerBuilder(RefundReadService.class, HSFGroup.HSF.name(), version).clientTimeout(5000)
				.build();
	}
	
	@Bean
	public UicPaymentAccountReadService uicPaymentAccountReadService(HsfConsumerContext context, @Value("${uic.payment.read.service.version}") String version) {
		return context.hsfConsumerBuilder(UicPaymentAccountReadService.class, HSFGroup.HSF.name(), version).clientTimeout(5000)
				.build();
	}
	@Bean
	public UicPaymentAccountReadServiceClient uicPaymentAccountReadServiceClient(ClientInfo clientInfo,UICCacheService uicCacheService,
			UicPaymentAccountReadService uicPaymentAccountReadService,UicReadService uicReadService){
		UicPaymentAccountReadServiceClient uicPaymentAccountReadServiceClient = new UicPaymentAccountReadServiceClient();
		uicPaymentAccountReadServiceClient.setClientInfo(clientInfo);
		uicPaymentAccountReadServiceClient.setUicCacheService(uicCacheService);
		uicPaymentAccountReadServiceClient.setUicPaymentAccountReadService(uicPaymentAccountReadService);
		uicPaymentAccountReadServiceClient.setUicReadService(uicReadService);
		return uicPaymentAccountReadServiceClient;
	}
	
	@Bean
	public NamelistMatchService namelistMatchService(HsfConsumerContext context, @Value("${namelistMatchService.version}") String version) {
		return context.hsfConsumerBuilder(NamelistMatchService.class, HSFGroup.HSF.name(), version).clientTimeout(5000)
				.build();
	}
	
	@Bean
	public PartnerTagService partnerTagService(HsfConsumerContext context, @Value("${ar.partner.version}") String version) {
	        return context.hsfConsumerBuilder(PartnerTagService.class, HSFGroup.HSF.name(), version).clientTimeout(5000)
	                .build();
	}
	
    @Bean
    public PolicyQueryService policyQueryService(HsfConsumerContext context, @Value("${alipay.insure.version}") String version) {
           return context.hsfConsumerBuilder(PolicyQueryService.class, HSFGroup.HSF.name(), version).clientTimeout(5000)
                   .build();
    }
    
    @Bean
    public ScPortalService scPortalService(HsfConsumerContext context, @Value("${scPortalService.version}") String version) {
           return context.hsfConsumerBuilder(ScPortalService.class, HSFGroup.HSF.name(), version).clientTimeout(5000)
                   .build();
    }

    
    @Bean
    public AccountManageService accountManageService(HsfConsumerContext context, @Value("${accountManageService.version}") String version) {
           return context.hsfConsumerBuilder(AccountManageService.class, HSFGroup.HSF.name(), version).clientTimeout(5000)
                   .build();
    }
    
    @Bean
    public AccountQueryService accountQueryService(HsfConsumerContext context, @Value("${accountQueryService.version}") String version) {
           return context.hsfConsumerBuilder(AccountQueryService.class, HSFGroup.HSF.name(), version).clientTimeout(5000)
                   .build();
    }
    
    @Bean
    public SellerSignService sellerSignService(HsfConsumerContext context, @Value("${sellerSignService.version}") String version) {
           return context.hsfConsumerBuilder(SellerSignService.class, HSFGroup.HSF.name(), version).clientTimeout(5000)
                   .build();
    }
    
    @Bean
    public QLCAccessService qlcAccessService(HsfConsumerContext context, @Value("${qlcAccessService.version}") String version) {
           return context.hsfConsumerBuilder(QLCAccessService.class, HSFGroup.HSF.name(), version).clientTimeout(5000)
                   .build();
    }
    
   
    
    @Bean
    public EndorApiClient storeEndorApiClient(@Value("${endor.cuntaostore.appName}")String appName,@Value("${endor.cuntaostore.accessKey}")String accessKey,@Value("${endor.service.version}")String version){
    	EndorApiClient client = new EndorApiClientImpl(appName, accessKey, version);
    	return client;
    }
    
    
    @Bean
	public OpenSearchManager openSearchManager(@Value("${cuntao.station.search.host}") String host,@Value("${cuntao.station.search.index}") String index){
    	OpenSearchManager openSearchManager = new OpenSearchManager();
    	openSearchManager.setAccesskey("pvnIGaF0vEzBuf9i");
    	openSearchManager.setSecret("3g1RnBTeCyr8adYzX9tcufOffdR6T8");
    	openSearchManager.setHost(host);
    	List<String> indexs=new ArrayList<String>();
    	indexs.add(index);
    	openSearchManager.setIndexs(indexs);
    	OpenSearchParser parser=new StationQueryOpenSearchParser();
    	openSearchManager.setParser(parser);
    	openSearchManager.init();
    	return openSearchManager;
	}
    
    @HSFConsumer(serviceVersion="${alibaba.ceres.version}",serviceGroup="HSF")
	private PoQueryService poQueryService;
	
    
    //@Bean(initMethod = "init")
	//public HSFSpringConsumerBean poQueryService(@Value("${alibaba.ceres.version}") String version) {
	//	return getConsumerBean(PoQueryService.class, HSFGroup.HSF, version, 30000);
	//}
    
    @HSFConsumer(serviceVersion="${cbu.org.struct.version}",serviceGroup="DUBBO")
   	private OrgStructWriteService orgStructWriteService;
   	
    
    //@Bean(initMethod = "init")
   //	public HSFSpringConsumerBean orgStructWriteService(@Value("${cbu.org.struct.version}") String version) {
   	//	return getConsumerBean(OrgStructWriteService.class, HSFGroup.DUBBO, version, 30000);
   //	}
    
    @HSFConsumer(serviceVersion="${cbu.org.struct.version}",serviceGroup="DUBBO")
   	private OrgStructReadService orgStructReadService;
   
    
   // @Bean(initMethod = "init")
   //	public HSFSpringConsumerBean orgStructReadService(@Value("${cbu.org.struct.version}") String version) {
   //		return getConsumerBean(OrgStructReadService.class, HSFGroup.DUBBO, version, 30000);
   //	}
    
    @HSFConsumer(serviceVersion="${cbu.member.service.version}",serviceGroup="DUBBO")
   	private MemberReadService memberReadService;
   
    
    
   // @Bean(initMethod = "init")
   //	public HSFSpringConsumerBean memberReadService(@Value("${cbu.member.service.version}") String version) {
   //		return getConsumerBean(MemberReadService.class, HSFGroup.DUBBO, version, 30000);
   //	}

    
    @Bean
    public CtMdJxcWarehouseApi ctMdJxcWarehouseApi(HsfConsumerContext context, @Value("${ctMdJxcWarehouseApi.version}") String version) {
           return context.hsfConsumerBuilder(CtMdJxcWarehouseApi.class, HSFGroup.HSF.name(), version).clientTimeout(5000)
                   .build();
    }
    
    @Bean
    public StandardAreaService standardAreaService(HsfConsumerContext context, @Value("${standardAreaService.version}") String version) {
           return context.hsfConsumerBuilder(StandardAreaService.class, HSFGroup.HSF.name(), version).clientTimeout(5000)
                   .build();
    }
    

    @Bean
    public StoreService storeService(HsfConsumerContext context, @Value("${storeService.version}") String version) {
           return context.hsfConsumerBuilder(StoreService.class, HSFGroup.HSF.name(), version).clientTimeout(5000)
                   .build();
    }
    
}
