package com.taobao.cun.auge.configuration;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.havana.oneid.client.common.HavanaCommonContext;
import com.taobao.sm.openshop.service.OpenShopService;
import org.esb.finance.service.audit.EsbFinanceAuditAdapter;
import org.esb.finance.service.contract.EsbFinanceContractAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

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
import com.alibaba.china.dw.dataopen.api.SQLIDQueryAPI;
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
import com.alipay.insopenprod.common.service.facade.api.InsPolicyApiFacade;
import com.alipay.insopenprod.common.service.facade.api.InsSceneApiFacade;
import com.cainiao.dms.sorting.api.IRailService;
import com.taobao.cun.ar.scene.station.service.PartnerTagService;
import com.taobao.cun.auge.api.service.station.NewCustomerUnitQueryService;
import com.taobao.cun.auge.incentive.service.IncentiveProgramQueryService;
import com.taobao.cun.auge.incentive.service.IncentiveProgramService;
import com.taobao.cun.auge.msg.service.MessageService;
import com.taobao.cun.auge.opensearch.OpenSearchManager;
import com.taobao.cun.auge.opensearch.OpenSearchParser;
import com.taobao.cun.auge.opensearch.StationQueryOpenSearchParser;
import com.taobao.cun.endor.base.client.EndorApiClient;
import com.taobao.cun.endor.base.client.impl.EndorApiClientImpl;
import com.taobao.cun.mdjxc.api.CtMdJxcWarehouseApi;
import com.taobao.cun.order.fulfillment.api.CtFulFillStockService;
import com.taobao.cun.recruit.ability.service.ServiceAbilityApplyService;
import com.taobao.cun.recruit.ability.service.ServiceAbilityEmployeeInfoService;
import com.taobao.cun.recruit.partner.service.PartnerApplyService;
import com.taobao.cun.settle.cae.service.SellerSignService;
import com.taobao.hsf.app.spring.util.HSFSpringConsumerBean;
import com.taobao.hsf.app.spring.util.annotation.HSFConsumer;
import com.taobao.mmp.client.permission.service.MmpAuthReadService;
import com.taobao.mtee.rmb.RmbService;
import com.taobao.namelist.service.NamelistMatchService;
import com.taobao.payment.account.service.AccountManageService;
import com.taobao.payment.account.service.query.AccountQueryService;
import com.taobao.place.client.PlaceServiceContext;
import com.taobao.place.client.service.StoreService;
import com.taobao.place.client.service.area.StandardAreaService;
import com.taobao.refundplatform.client.read.RefundReadService;
import com.taobao.ruledata.service.rightcenter.RightQueryService;
import com.taobao.sellerservice.core.client.shopmirror.ShopMirrorService;
import com.taobao.taglib.client.TagWriteServiceClient;
import com.taobao.taglib.service.UserDataWrapperService;
import com.taobao.taglib.service.impl.UserDataWrapperServiceImpl;
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
import com.taobao.union.api.client.service.EntryService;
import com.taobao.wws.hsf2icesrv;
import com.tmall.usc.channel.client.UscChannelRelationService;
@Configuration
public class HsfConsumer2ndPartyConfiguration  {
	@HSFConsumer(serviceVersion="${new.customer.unit.service.version}",serviceGroup="HSF")
	private NewCustomerUnitQueryService newCustomerUnitQueryService;

	@HSFConsumer(serviceVersion="${hsf.consumer.version.employeeService}",serviceGroup="HSF")
	private Employee360Service employee360Service;
	
	@HSFConsumer(serviceVersion="${hsf.consumer.version.hsf2icesrv}",serviceGroup="HSF")
	private hsf2icesrv hsf2icesrv;
	
	@HSFConsumer(serviceVersion="${hsf.consumer.version.cainiao.countyDomainWriteService}",serviceGroup="HSF")
	private CountyDomainWriteService countyDomainWriteService;

	@HSFConsumer(serviceVersion="${hsf.consumer.version.cainiao.stationWriteService}",serviceGroup="HSF")
	private StationWriteService stationWriteService;
	
	@HSFConsumer(serviceVersion="${hsf.consumer.version.cainiao.stationWriteService}",serviceGroup="HSF")
	private StationReadService StationReadService;
	
	
	@HSFConsumer(serviceVersion="${hsf.consumer.version.cainiao.stationUserWriteService}",serviceGroup="HSF")
	private StationUserWriteService stationUserWriteService;
	
	@HSFConsumer(serviceVersion="${partner.peixun.service.version}",serviceGroup="HSF")
	private TrainingRecordServiceFacade trainingRecordServiceFacade;
	
	@HSFConsumer(serviceVersion="${partner.peixun.service.version}",serviceGroup="HSF")
	private CourseServiceFacade courseServiceFacade;
	
	@HSFConsumer(serviceVersion="${partner.peixun.service.version}",serviceGroup="HSF")
	private TrainingTicketServiceFacade trainingTicketServiceFacade;
	
	@HSFConsumer(serviceVersion="${tcBaseService.service.version}",serviceGroup="HSF")
	private TcBaseService tcBaseService;
	
	@HSFConsumer(serviceVersion="${accessControlService.service.version}",serviceGroup="HSF")
	private AccessControlService accessControlService;
	
	@HSFConsumer(serviceVersion="${dowjonesProductService.service.version}",serviceGroup="DUBBO")
	private ProductService productService;
	
	@HSFConsumer(serviceVersion="${dowjonesOrderService.service.portal.version}",serviceGroup="DUBBO")
	private OrderPortalService orderPortalService;
	
	@HSFConsumer(serviceVersion="${dowjonesProductService.service.version}",serviceGroup="DUBBO")
	private ShoppingCartPortalService shoppingCartPortalService;
	
	@HSFConsumer(serviceVersion="${crm.order.service.version}",serviceGroup="DUBBO")
	private OrderItemFacade orderItemFacade;
	
	@HSFConsumer(serviceVersion="${crm.finance.service.version}",serviceGroup="HSF")
	private EsbFinanceAuditAdapter esbFinanceAuditAdapter;
	
	@HSFConsumer(serviceVersion="${crm.finance.service.version}",serviceGroup="HSF")
	private EsbFinanceContractAdapter esbFinanceContractAdapter;
	
	@HSFConsumer(serviceVersion="${alibaba.ceres.version}",serviceGroup="HSF")
	private PrService prService;
	
	@HSFConsumer(serviceVersion="${alibaba.ceres.version}",serviceGroup="HSF")
	private com.alibaba.ceres.service.catalog.ProductService productService2;
	
	@HSFConsumer(serviceVersion="${alibaba.ceres.version}",serviceGroup="HSF")
	private CategoryService categoryService;
	
	@HSFConsumer(serviceVersion="${hsf.consumer.version.cainiao.stationReadService}",serviceGroup="HSF")
	private StationReadService stationReadService;
	
	@HSFConsumer(serviceVersion="${taobao.uic.version}",serviceGroup="HSF")
	private TagRecordReadService tagRecordReadService;
	
	@HSFConsumer(serviceVersion="${taobao.uic.version}",serviceGroup="HSF")
	private TagRecordWriteService tagRecordWriteService;
	
	@HSFConsumer(serviceVersion="${taobao.uic.version}",serviceGroup="HSF")
	private UicDataReadService uicDataReadService;
	
	@HSFConsumer(serviceVersion="${taobao.uic.version}",serviceGroup="HSF")
	private UicDataWriteService uicDataWriteService;

	@HSFConsumer(serviceVersion="${insPolicyApiFacade.version}",serviceGroup="HSF")
	private InsPolicyApiFacade insPolicyApiFacade;

	@HSFConsumer(serviceVersion="${insPolicyApiFacade.version}",serviceGroup="HSF")
	private InsSceneApiFacade insSceneApiFacade;

	@HSFConsumer(serviceVersion="${serviceAbilityApplyService.version}",serviceGroup="HSF")
	private ServiceAbilityApplyService serviceAbilityApplyService;

	@HSFConsumer(serviceVersion="${serviceAbilityApplyService.version}",serviceGroup="HSF")
	private ServiceAbilityEmployeeInfoService serviceAbilityEmployeeInfoService;

	@HSFConsumer(serviceVersion="${hsf.consumer.version.rightQueryService}",serviceGroup="HSF")
	private RightQueryService rightQueryService;

	@Bean
	public SQLIDQueryAPI sqlIDQueryAPI(HsfConsumerContext hsfConsumerContext) {
		return hsfConsumerContext.hsfConsumerBuilder(SQLIDQueryAPI.class, HSFGroup.DUBBO.getName(), "${hsf.consumer.auge.SQLIDQueryAPI.version}").clientTimeout(8000).build();
	}
	
	@HSFConsumer(serviceVersion="${ctFulFillStockService.version}",serviceGroup="HSF")
	private CtFulFillStockService ctFulFillStockService; 
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
	
	@Bean
	public UserDataWrapperService userDataWrapperService(UicDataServiceClient uicDataServiceClient,UicExtraReadServiceClient uicExtraReadServiceClient){
		UserDataWrapperServiceImpl	userDataWrapperService = new UserDataWrapperServiceImpl();
		userDataWrapperService.setUicDataServiceClient(uicDataServiceClient);
		userDataWrapperService.setUicExtraReadServiceClient(uicExtraReadServiceClient);
		return userDataWrapperService;
	}
	
	@Bean
	public TagWriteServiceClient tagWriteServiceClient(UicDataServiceClient uicDataServiceClient,
			UicTagWriteServiceClient uicTagWriteServiceClient,UserDataWrapperService userDataWrapperService) {
		TagWriteServiceClient uicTagServiceClient = new TagWriteServiceClient();
		uicTagServiceClient.setUicDataServiceClient(uicDataServiceClient);
		uicTagServiceClient.setUicTagWriteServiceClient(uicTagWriteServiceClient);
		uicTagServiceClient.setUserDataWrapperService(userDataWrapperService);
		return uicTagServiceClient;
	}
	
	
	@HSFConsumer(serviceVersion="${hsf.consumer.version.enhancedUserQueryService}",serviceGroup="${hsf.consumer.serviceGroup.enhancedUserQueryService}")
	private EnhancedUserQueryService enhancedUserQueryService;
	

	@HSFConsumer(serviceVersion="${hsf.consumer.version.incentiveProgramQueryService}",serviceGroup="HSF")
	private IncentiveProgramQueryService incentiveProgramQueryService;
	
	
	@HSFConsumer(serviceVersion="${hsf.consumer.version.incentiveProgramQueryService}",serviceGroup="HSF")
	private IncentiveProgramService incentiveProgramService;
	
	

	@Bean
	  public SellerQualiService sellerQualiService(HsfConsumerContext context,@Value("${sellerQualiService.version}") String version) {
	      return context.hsfConsumerBuilder(SellerQualiService.class,HSFGroup.HSF.name(),version).clientTimeout(5000).build();
	  }
	 
	@HSFConsumer(serviceVersion="1.0.0",serviceGroup="HSF")
	private MmpAuthReadService mmpAuthReadService;
	
	
	 @Bean
	  public QualiAccessService qualiAccessService(HsfConsumerContext context,@Value("${sellerQualiService.version}") String version) {
	      return context.hsfConsumerBuilder(QualiAccessService.class,HSFGroup.HSF.name(),version).clientTimeout(15000).build();
	  }
	 
	@HSFConsumer(serviceVersion="${hsf.consumer.version.cainiao.stationUserWriteService}",serviceGroup="HSF")
	private WarehouseReadService warehouseReadService;
		
	 
	@HSFConsumer(serviceVersion="${hsf.consumer.version.cainiao.stationUserWriteService}",serviceGroup="HSF")
	private WarehouseWriteService warehouseWriteService;
	
	
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
	
    @HSFConsumer(serviceVersion="${cbu.org.struct.version}",serviceGroup="DUBBO")
   	private OrgStructWriteService orgStructWriteService;
   	
    @HSFConsumer(serviceVersion="${cbu.org.struct.version}",serviceGroup="DUBBO")
   	private OrgStructReadService orgStructReadService;
   
    @HSFConsumer(serviceVersion="${cbu.member.service.version}",serviceGroup="DUBBO")
   	private MemberReadService memberReadService;

	@HSFConsumer(serviceVersion="${taobao.union.entry.service.version}",serviceGroup="HSF")
	private EntryService entryService;
    
	@HSFConsumer(serviceVersion="${shopMirrorService.version}",serviceGroup="HSF")
	private ShopMirrorService shopMirrorService;
	
	@HSFConsumer(serviceVersion="${uscChannelRelationService.version}",serviceGroup="HSF")
	private UscChannelRelationService uscChannelRelationService;
    
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
    
    @Bean
    public IRailService iRailService(HsfConsumerContext context, @Value("${hsf.irailService.version}") String version) {
           return context.hsfConsumerBuilder(IRailService.class, HSFGroup.HSF.name(), version).clientTimeout(5000)
                   .build();
    }
    
    /**
     * <bean id="placeServiceContext" class="com.taobao.place.client.PlaceServiceContext">
    <property name="hsfConsumerVersion" value="1.0.0.daily" /> <!-- 线上预发1.0.0 -->
    <property name="appName" value="place"/>
		</bean>

     * @return
     */
    @Bean
    public PlaceServiceContext placeServiceContext(@Value("${storeService.version}") String version){
    	PlaceServiceContext placeServiceContext = new PlaceServiceContext();
    	placeServiceContext.setHsfConsumerVersion(version);
    	placeServiceContext.setAppName("place");
    	return placeServiceContext;
    }
    
    @Bean("requestService")
    HSFSpringConsumerBean requestService() {
        HSFSpringConsumerBean hsfBean = new HSFSpringConsumerBean();
        hsfBean.setInterfaceName("com.taobao.mtee.service.RequestService");
        // 线上版本: 1.0.0，日常版本: 1.0.0.daily, 但日常不可用
        hsfBean.setVersion("1.0.0");
        hsfBean.setGroup("HSF");
        return hsfBean;
    }

    @Bean("requestServiceForMtee3")
    HSFSpringConsumerBean requestServiceForMtee3() {
        HSFSpringConsumerBean hsfBean = new HSFSpringConsumerBean();
        hsfBean.setInterfaceName("com.alibaba.security.tenant.common.service.RequestService");
        // 线上版本: 1.0.0_ali_taobao, 日常版本: 1.0.0_ali_taobao
        hsfBean.setVersion("1.0.0_ali_taobao");
        hsfBean.setGroup("HSF");
        return hsfBean;
    }

    @Bean("threadPoolTaskExecutor")
    TaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(500);
        executor.setKeepAliveSeconds(300);
        return executor;
    }
    
    @Bean("rmbService")
    RmbService rmbService(
            @Qualifier("requestService")
                    com.taobao.mtee.service.RequestService requestService,
            @Qualifier("requestServiceForMtee3")
                    com.alibaba.security.tenant.common.service.RequestService requestServiceForMtee3,
            @Qualifier("threadPoolTaskExecutor")
                    TaskExecutor threadPoolTaskExecutor
    ) {
        String appName = "auge";
        // 0 - 通过 hsf 调用 mtee3， 1 - 通过 http 协议调用 mtee3
        Integer httpFlag = 0;

        RmbService rmbService = new RmbService();
        rmbService.setAppName(appName);
        rmbService.setHttpFlag(httpFlag);
        rmbService.setRequestService(requestService);
        rmbService.setRequestServiceForMtee3(requestServiceForMtee3);
        rmbService.setAsyncTaskExecutor(threadPoolTaskExecutor);
        return rmbService;
    }


    @Bean
	public HavanaCommonContext havanaCommonContext() {
		HavanaCommonContext havanaCommonContext = new HavanaCommonContext();
		havanaCommonContext.setAppName("auge");
		return havanaCommonContext;
	}
	@HSFConsumer(serviceVersion="${hsf.consumer.version.OpenShopService}",serviceGroup="HSF")
	private OpenShopService openShopService;

}
