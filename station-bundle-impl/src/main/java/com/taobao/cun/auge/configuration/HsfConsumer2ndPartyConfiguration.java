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
import com.taobao.cun.order.fulfillment.api.CtFulFillStockService;
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
	
	@HSFConsumer(serviceVersion="${hsf.consumer.version.hsf2icesrv}",serviceGroup="HSF")
	private hsf2icesrv hsf2icesrv;
	
	@HSFConsumer(serviceVersion="${hsf.consumer.version.cainiao.countyDomainWriteService}",serviceGroup="HSF")
	private CountyDomainWriteService countyDomainWriteService;

	@HSFConsumer(serviceVersion="${hsf.consumer.version.cainiao.stationWriteService}",serviceGroup="HSF")
	private StationWriteService stationWriteService;
	
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
	
	
	@HSFConsumer(serviceVersion="${hsf.consumer.version.enhancedUserQueryService}",serviceGroup="HSF")
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
