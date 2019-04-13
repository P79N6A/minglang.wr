package com.taobao.cun.auge.flow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.taobao.diamond.client.Diamond;
import com.taobao.diamond.manager.ManagerListener;
import com.taobao.hsf.app.api.util.HSFApiConsumerBean;
import com.taobao.hsf.model.metadata.MethodSpecial;
import com.taobao.hsf.remoting.service.GenericService;

/**
 * HSF服务工厂类，创建HSF服务实例
 *
 * @author chengyu.zhoucy
 *
 */
@Component
public class ServiceFactory implements InitializingBean {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private Map<String, GenericService> services = new HashMap<String, GenericService>();
	
	private Map<String, ServiceMeta > serviceMetas = new HashMap<String, ServiceMeta>();
	
	public GenericService getService(String taskCode){
		return services.get(taskCode);
	}
	
	public ServiceMeta getServiceMeta(String taskCode) {
		return serviceMetas.get(taskCode);
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		String config = Diamond.getConfig("flow_content_services", "DEFAULT_GROUP", 5000);
		if(Strings.isNullOrEmpty(config)) {
			throw new RuntimeException("找不到流程内容获取接口的配置:dataId=flow_content_services, group=DEFAULT_GROUP");
		}
		process(config);
		
		Diamond.addListener("flow_content_services", "DEFAULT_GROUP", new ManagerListener() {
			@Override
			public void receiveConfigInfo(String configInfo) {
				process(configInfo);
			}
			
			@Override
			public Executor getExecutor() {
				return null;
			}
		});
	}
	
	private void process(String config) {
		ServiceConfig serviceConfig = JSON.parseObject(config, ServiceConfig.class);
		
		List<ServiceMeta> newServiceMeta = Lists.newArrayList();
		for(ServiceMeta serviceMeta : serviceConfig.getServiceMetas()) {
			if(Strings.isNullOrEmpty(serviceMeta.getServiceVersion())) {
				serviceMeta.setServiceVersion(serviceConfig.getDefaultVersion());
			}
			newServiceMeta.add(serviceMeta);
			if(!isExists(serviceMeta)) {
				services.put(serviceMeta.getTaskCode(), createService(serviceMeta));
				serviceMetas.put(serviceMeta.getTaskCode(), serviceMeta);
			}
		}
		//删除
		List<String> removedKeys = Lists.newArrayList();
		for(Entry<String, ServiceMeta> entry : serviceMetas.entrySet()) {
			if(!newServiceMeta.contains(entry.getValue())) {
				removedKeys.add(entry.getKey());
			}
		}
		
		for(String key : removedKeys) {
			logger.info("remove:{}", key);
			services.remove(key);
			serviceMetas.remove(key);
		}
	}
	
	private boolean isExists(ServiceMeta serviceMeta) {
		ServiceMeta sMeta = serviceMetas.get(serviceMeta.getTaskCode());
		return sMeta != null && sMeta.equals(serviceMeta);
	}

	private GenericService createService(ServiceMeta serviceMeta) {
		HSFApiConsumerBean consumerBean = new HSFApiConsumerBean();
        consumerBean.setInterfaceName(serviceMeta.getServiceInterface());
        consumerBean.setGeneric("true");
        consumerBean.setVersion(serviceMeta.getServiceVersion());
        MethodSpecial methodSpecial = new MethodSpecial();
        methodSpecial.setClientTimeout(serviceMeta.getTimeout() == null ? 3000 : serviceMeta.getTimeout());
        methodSpecial.setMethodName(serviceMeta.getServiceMethod());
        consumerBean.setMethodSpecials(new MethodSpecial[] { methodSpecial });
        try {
			consumerBean.init();
		} catch (Exception e) {
			throw new RuntimeException("初始化泛化调用失败：" + serviceMeta.toString() + ",原因：" + e.getMessage(), e);
		}
        try {
			return (GenericService) consumerBean.getObject();
		} catch (Exception e) {
			throw new RuntimeException("初始化泛化调用失败：" + e.getMessage(), e);
		}
	}
}
