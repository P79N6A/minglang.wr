package com.taobao.cun.auge.configuration;

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
import com.alibaba.ceres.service.category.CategoryService;
import com.alibaba.ceres.service.pr.PrService;
import com.alibaba.crm.pacific.facade.refund.ApplyRefundService;
import com.alibaba.crm.pacific.facade.refund.RefundBaseCommonService;
import com.alibaba.crm.pacific.facade.refund.RefundForBizAuditService;
import com.alibaba.ivy.service.course.CourseServiceFacade;
import com.alibaba.ivy.service.user.TrainingRecordServiceFacade;
import com.alibaba.ivy.service.user.TrainingTicketServiceFacade;
import com.alibaba.masterdata.client.service.Employee360Service;
import com.alibaba.pm.sc.api.quali.SellerQualiService;
import com.alibaba.pm.sc.portal.api.quali.QualiAccessService;
import com.alibaba.tax.api.service.ArInvoiceService;
import com.aliexpress.boot.hsf.HSFGroup;
import com.aliexpress.boot.hsf.HsfConsumerAutoConfiguration;
import com.aliexpress.boot.hsf.consumer.HsfConsumerContext;
import com.taobao.cun.auge.incentive.service.IncentiveProgramQueryService;
import com.taobao.cun.auge.incentive.service.IncentiveProgramService;
import com.taobao.cun.auge.msg.service.MessageService;
import com.taobao.cun.crius.alipay.service.AlipayRiskScanService;
import com.taobao.cun.recruit.partner.service.PartnerApplyService;
import com.taobao.hsf.app.spring.util.HSFSpringConsumerBean;
import com.taobao.namelist.service.NamelistMatchService;
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
@Configuration
public class HsfConsumer2ndPartyConfiguration extends HsfConsumerAutoConfiguration {

	// hr相关的第二方服务
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean employee360Service(@Value("${hsf.consumer.version.employeeService}") String version) {
		return getConsumerBean(Employee360Service.class, HSFGroup.HSF, version, 3000);
	}
	
	//旺旺
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean hsf2icesrv(@Value("${hsf.consumer.version.hsf2icesrv}") String version) {
		return getConsumerBean(com.taobao.wws.hsf2icesrv.class, HSFGroup.HSF, version, 3000);
	}

	// 菜鸟服务
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean countyDomainWriteService(@Value("${hsf.consumer.version.cainiao.countyDomainWriteService}") String version) {
		return getConsumerBean(CountyDomainWriteService.class, HSFGroup.HSF, version, 3000);
	}

	@Bean(initMethod = "init")
	public HSFSpringConsumerBean stationWriteService(@Value("${hsf.consumer.version.cainiao.stationWriteService}") String version) {
		return getConsumerBean(StationWriteService.class, HSFGroup.HSF, version, 3000);
	}

	@Bean(initMethod = "init")
	public HSFSpringConsumerBean stationUserWriteService(@Value("${hsf.consumer.version.cainiao.stationUserWriteService}") String version) {
		return getConsumerBean(StationUserWriteService.class, HSFGroup.HSF, version, 3000);
	}

	@Bean(initMethod = "init")
	public HSFSpringConsumerBean trainingRecordServiceFacade(@Value("${partner.peixun.service.version}") String version) {
		return getConsumerBean(TrainingRecordServiceFacade.class, HSFGroup.HSF, version, 3000);
	}
	
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean courseServiceFacade(@Value("${partner.peixun.service.version}") String version) {
		return getConsumerBean(CourseServiceFacade.class, HSFGroup.HSF, version, 3000);
	}
	
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean trainingTicketServiceFacade(@Value("${partner.peixun.service.version}") String version) {
		return getConsumerBean(TrainingTicketServiceFacade.class, HSFGroup.HSF, version, 3000);
	}
	
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean tcBaseService (@Value("${tcBaseService.service.version}") String version) {
		return getConsumerBean(TcBaseService.class, HSFGroup.HSF, version, 3000);
	}
	
	
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean accessControlService(@Value("${accessControlService.service.version}") String version) {
		return getConsumerBean(AccessControlService.class, HSFGroup.HSF, version, 3000);
	}
	
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean productService(@Value("${dowjonesProductService.service.version}") String version) {
		return getConsumerBean(ProductService.class, HSFGroup.DUBBO, version, 3000);
	}
	
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean orderPortalService(@Value("${dowjonesOrderService.service.portal.version}") String version) {
		return getConsumerBean(OrderPortalService.class, HSFGroup.DUBBO, version, 3000);
	}
	
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean shoppingCartPortalService(@Value("${dowjonesProductService.service.version}") String version) {
		return getConsumerBean(ShoppingCartPortalService.class, HSFGroup.DUBBO, version, 3000);
	}
	
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean orderItemFacade(@Value("${crm.order.service.version}") String version) {
		return getConsumerBean(OrderItemFacade.class, HSFGroup.DUBBO, version, 3000);
	}
	
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean esbFinanceAuditAdapter(@Value("${crm.finance.service.version}") String version) {
		return getConsumerBean(EsbFinanceAuditAdapter.class, HSFGroup.HSF, version, 3000);
	}
	
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean esbFinanceContractAdapter(@Value("${crm.finance.service.version}") String version) {
		return getConsumerBean(EsbFinanceContractAdapter.class, HSFGroup.HSF, version, 3000);
	}
	
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean prService(@Value("${alibaba.ceres.version}") String version) {
		return getConsumerBean(PrService.class, HSFGroup.HSF, version, 30000);
	}
	
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean ceresProductService(@Value("${alibaba.ceres.version}") String version) {
		return getConsumerBean(com.alibaba.ceres.service.catalog.ProductService.class, HSFGroup.HSF, version, 3000);
	}
	
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean ceresCategoryService(@Value("${alibaba.ceres.version}") String version) {
		return getConsumerBean(CategoryService.class, HSFGroup.HSF, version, 3000);
	}
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean stationReadService(@Value("${hsf.consumer.version.cainiao.stationReadService}") String version) {
		return getConsumerBean(StationReadService.class, HSFGroup.HSF, version, 3000);
	}
	
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean tagRecordReadService(@Value("${taobao.uic.version}") String version) {
		return getConsumerBean(TagRecordReadService.class, HSFGroup.HSF, version, 3000);
	}
	
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean tagRecordWriteService(@Value("${taobao.uic.version}") String version) {
		return getConsumerBean(TagRecordWriteService.class, HSFGroup.HSF, version, 3000);
	}
	
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean uicDataReadService(@Value("${taobao.uic.version}") String version) {
		return getConsumerBean(UicDataReadService.class, HSFGroup.HSF, version, 3000);
	}
	
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean uicDataWriteService(@Value("${taobao.uic.version}") String version) {
		return getConsumerBean(UicDataWriteService.class, HSFGroup.HSF, version, 3000);
	}
	
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
	
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean enhancedUserQueryService(@Value("${hsf.consumer.version.enhancedUserQueryService}") String version) {
		return getConsumerBean(EnhancedUserQueryService.class, HSFGroup.HSF, version, 3000);
	}

	@Bean(initMethod = "init")
	public HSFSpringConsumerBean incentiveProgramQueryService(@Value("${hsf.consumer.version.incentiveProgramQueryService}") String version) {
		return getConsumerBean(IncentiveProgramQueryService.class, HSFGroup.HSF, version, 8000);
	}

	@Bean(initMethod = "init")
	public HSFSpringConsumerBean incentiveProgramService(@Value("${hsf.consumer.version.incentiveProgramQueryService}") String version) {
		return getConsumerBean(IncentiveProgramService.class, HSFGroup.HSF, version, 8000);
	}

	@Bean
	  public SellerQualiService sellerQualiService(HsfConsumerContext context,@Value("${sellerQualiService.version}") String version) {
	      return context.hsfConsumerBuilder(SellerQualiService.class,HSFGroup.HSF.name(),version).clientTimeout(5000).build();
	  }
	 
	 
	 @Bean
	  public QualiAccessService qualiAccessService(HsfConsumerContext context,@Value("${sellerQualiService.version}") String version) {
	      return context.hsfConsumerBuilder(QualiAccessService.class,HSFGroup.HSF.name(),version).clientTimeout(15000).build();
	  }
	 
	@Bean(initMethod = "init")
	public HSFSpringConsumerBean warehouseReadService(
			@Value("${hsf.consumer.version.cainiao.stationUserWriteService}") String version) {
		return getConsumerBean(WarehouseReadService.class, HSFGroup.HSF,
				version, 3000);
	}

	@Bean(initMethod = "init")
	public HSFSpringConsumerBean warehouseWriteService(
			@Value("${hsf.consumer.version.cainiao.stationUserWriteService}") String version) {
		return getConsumerBean(WarehouseWriteService.class, HSFGroup.HSF,
				version, 3000);
	}
	@Bean
	public MessageService messageService(HsfConsumerContext context, @Value("${messageService.version}") String version) {
		return context.hsfConsumerBuilder(MessageService.class, HSFGroup.HSF.name(), version).clientTimeout(5000)
				.build();
	}
	
	
	@Bean
	public PartnerApplyService partnerApplyService(HsfConsumerContext context, @Value("${recuit.service.version}") String version) {
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
	public AlipayRiskScanService alipayRiskScanService(HsfConsumerContext context, @Value("${alipayRiskScanService.version}") String version) {
		return context.hsfConsumerBuilder(AlipayRiskScanService.class, HSFGroup.HSF.name(), version).clientTimeout(5000)
				.build();
	}
}
