package com.taobao.cun.auge.jingwei.station;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.middleware.jingwei.client.Client;
import com.alibaba.middleware.jingwei.client.ClientFactory;
import com.alibaba.middleware.jingwei.client.custom.EventMessage;
import com.alibaba.middleware.jingwei.client.custom.SimpleMessageListener;
import com.alibaba.middleware.jingwei.common.exception.RetryLaterException;
import com.google.common.collect.Sets;
import com.taobao.cun.endor.dto.AppInfo;
import com.taobao.cun.endor.service.AppService;
import com.taobao.diamond.client.Diamond;

@Component
public class EndorOrgUpdateJingweiTask implements InitializingBean{
	@Value("${jingwei.taskid.endor.org}")
	private String taskId;
	
	private Client client;
	@Resource
	private AppService appService;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		client = ClientFactory.create(taskId);
		client.registerMessageListener(new SimpleMessageListener() {
			@Override
			public Result onReceiveMessage(List<EventMessage> messages) {
				Set<Long> appIds = Sets.newHashSet();
				for(EventMessage msg : messages){
					List<Map<String, Serializable>> rowDataMaps = msg.getRowDataMaps();
					for(Map<String, Serializable> rowDataMap : rowDataMaps){
						appIds.add((Long) rowDataMap.get("app_id"));
					}
				}
				
				for(Long appId : appIds){
					AppInfo appInfo = appService.getAppInfoById(appId);
					try {
						String version = Diamond.getConfig("ENDOR_APP_ORG_" + appInfo.getAppName(), "ENDOR_ORG_GROUP", 5000);
						if(version == null){
							version = "1";
						}else{
							version = String.valueOf(Integer.parseInt(version) + 1);
						}
						Diamond.publishSingle("ENDOR_APP_ORG_" + appInfo.getAppName(), "ENDOR_ORG_GROUP", version);
					} catch (IOException e) {
						throw new RetryLaterException(e);
					}
				}
				return Result.ACK_AND_NEXT;
			}
		});
	}
	
}
