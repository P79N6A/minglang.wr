package com.taobao.cun.auge.flow;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.taobao.diamond.client.Diamond;
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
		
		ServiceConfig serviceConfig = JSON.parseObject(config, ServiceConfig.class);
		
		for(ServiceMeta serviceMeta : serviceConfig.getServiceMetas()) {
			if(Strings.isNullOrEmpty(serviceMeta.getServiceVersion())) {
				serviceMeta.setServiceVersion(serviceConfig.getDefaultVersion());
			}
			services.put(serviceMeta.getTaskCode(), createService(serviceMeta));
			serviceMetas.put(serviceMeta.getTaskCode(), serviceMeta);
		}
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
