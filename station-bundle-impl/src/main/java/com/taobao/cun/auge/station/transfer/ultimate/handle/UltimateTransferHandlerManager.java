package com.taobao.cun.auge.station.transfer.ultimate.handle;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.OrderUtils;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

/**
 * 管理转交处理器，他们处理可能存在先后顺序关系，所以会对他们做排序
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class UltimateTransferHandlerManager implements BeanPostProcessor{
	private List<AutoTransferHandler> handlers = Lists.newArrayList();
	
	public List<AutoTransferHandler> getHandlers() {
		return handlers;
	}
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if(bean instanceof AutoTransferHandler) {
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
