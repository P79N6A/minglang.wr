package com.taobao.cun.auge.jingwei.station;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.middleware.jingwei.client.Client;
import com.alibaba.middleware.jingwei.client.ClientFactory;
import com.alibaba.middleware.jingwei.client.custom.EventMessage;
import com.alibaba.middleware.jingwei.client.custom.InsertEvent;
import com.alibaba.middleware.jingwei.client.custom.SimpleMessageListener;
import com.alibaba.middleware.jingwei.client.custom.UpdateEvent;
import com.taobao.cun.endor.service.OrgService;

/**
 * Station状态为NEW的时候将他同步到ENDOR，作为一个组织
 * 当状态变化QUIT的时候，从ENDOR删除
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class StationJingweiTask implements InitializingBean{
	@Value("${jingwei.taskid.station}")
	private String taskId;
	
	private Client client;
	
	@Resource
	private OrgService orgService;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		client = ClientFactory.create(taskId);
		client.registerMessageListener(new SimpleMessageListener() {
			@Override
			public Result onReceiveMessage(List<EventMessage> messages) {
				for(EventMessage msg : messages){
            		if(msg instanceof UpdateEvent){
            			UpdateEvent updateEvent = (UpdateEvent) msg;
            			List<Map<String, Serializable>> modifiedRows = updateEvent.getModifyRowDataMaps();
            		}else if(msg instanceof InsertEvent){
            			List<Map<String, Serializable>> rowDataMaps = msg.getRowDataMaps();
            			for(int index = 0; index < rowDataMaps.size(); index++){
            				Map<String, Serializable> rowDataMap = rowDataMaps.get(index);
            			}
            		}
				}
				return null;
			}});
	}
	
}
