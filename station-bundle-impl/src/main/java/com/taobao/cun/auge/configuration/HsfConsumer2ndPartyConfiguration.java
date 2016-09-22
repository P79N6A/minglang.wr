package com.taobao.cun.auge.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ali.dowjones.service.portal.OrderPortalService;
import com.ali.dowjones.service.portal.ProductService;
import com.ali.dowjones.service.portal.ShoppingCartPortalService;
import com.ali.martini.biz.order.interfaces.orderitem.facade.OrderItemFacade;
import com.alibaba.buc.acl.api.service.AccessControlService;
import com.alibaba.cainiao.cuntaonetwork.service.station.StationUserWriteService;
import com.alibaba.cainiao.cuntaonetwork.service.station.StationWriteService;
import com.alibaba.cainiao.cuntaonetwork.service.warehouse.CountyDomainWriteService;
import com.alibaba.ivy.service.course.CourseServiceFacade;
import com.alibaba.ivy.service.user.TrainingRecordServiceFacade;
import com.alibaba.ivy.service.user.TrainingTicketServiceFacade;
import com.alibaba.masterdata.client.service.Employee360Service;
import com.aliexpress.boot.hsf.HSFGroup;
import com.aliexpress.boot.hsf.HsfConsumerAutoConfiguration;
import com.taobao.hsf.app.spring.util.HSFSpringConsumerBean;
import com.taobao.tc.service.TcBaseService;
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
	public HSFSpringConsumerBean orderPortalService(@Value("${dowjonesOrderService.service.version}") String version) {
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
}
