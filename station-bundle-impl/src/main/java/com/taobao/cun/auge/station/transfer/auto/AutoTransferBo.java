package com.taobao.cun.auge.station.transfer.auto;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.OrderUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.dal.domain.CountyStation;
import com.taobao.cun.auge.log.bo.AppBizLogBo;
import com.taobao.cun.auge.station.bo.CountyStationBO;

/**
 * N + 75这天会自动转交
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class AutoTransferBo implements BeanPostProcessor{
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private List<AutoTransferHandler> handlers = Lists.newArrayList();
	@Resource
	private AppBizLogBo appBizLogBo;
	@Resource
	private CountyStationBO countyStationBO;
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void transfer(Long countyStationId) {
		CountyStation countyStation = countyStationBO.getCountyStationById(countyStationId);
		handlers.forEach(handler->{
			handler.transfer(countyStation);
		});
	}
	
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if(bean instanceof AutoTransferHandler) {
			logger.info("{}:{}", OrderUtils.getPriority(bean.getClass()), bean.getClass().getCanonicalName());
			handlers.add((AutoTransferHandler) bean);
			
			Collections.sort(handlers, new Comparator<AutoTransferHandler>() {
				@Override
				public int compare(AutoTransferHandler a1, AutoTransferHandler a2) {
					return OrderUtils.getPriority(a1.getClass()) > OrderUtils.getPriority(a2.getClass()) ? 1 : -1;
				}
			});
		}
		return bean;
	}
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}
}
