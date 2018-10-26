package com.taobao.cun.auge.station.transfer.ultimate.handle;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.annotation.Tag;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.OrderUtils;
import org.springframework.stereotype.Component;

/**
 * 管理转交处理器，他们处理可能存在先后顺序关系，所以会对他们做排序
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class UltimateTransferHandlerManager implements BeanPostProcessor{
	private List<UltimateTransferHandler> handlers = Lists.newArrayList();
	
	public List<UltimateTransferHandler> getHandlers(String handlerGroup) {
		List<UltimateTransferHandler> filters = Lists.newArrayList();
		for(UltimateTransferHandler handler : handlers) {
			Tag tag = AnnotationUtils.findAnnotation(handler.getClass(), Tag.class);
			for(String v : tag.value()) {
				if(handlerGroup.equals(v)) {
					filters.add(handler);
					break;
				}
			}
		}
		return filters;
	}
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if(bean instanceof UltimateTransferHandler) {
			handlers.add((UltimateTransferHandler) bean);
			Collections.sort(handlers, new Comparator<UltimateTransferHandler>() {
				@Override
				public int compare(UltimateTransferHandler a1, UltimateTransferHandler a2) {
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
