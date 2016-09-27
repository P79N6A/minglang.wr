package com.taobao.cun.auge.alilang.jingwei;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.middleware.jingwei.client.Client;
import com.alibaba.middleware.jingwei.client.ClientFactory;
import com.alibaba.middleware.jingwei.client.custom.EventMessage;
import com.alibaba.middleware.jingwei.client.custom.SimpleMessageListener;
import com.alibaba.middleware.jingwei.client.custom.UpdateEvent;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;

@Component
public class BondJingweiTask extends JingweiTask {
	@Value("${jingwei.taskid.bond}")
	private String taskId;
	
	private Client client;
	
	@Resource
	private PartnerInstanceBO partnerInstanceBO;
	
	@Override
	public void start() {
		client = ClientFactory.create(taskId);
		client.registerMessageListener(new SimpleMessageListener() {
            @Override
            public Result onReceiveMessage(List<EventMessage> messages) {
            	for(EventMessage msg : messages){
            		if(msg instanceof UpdateEvent){
            			UpdateEvent updateEvent = (UpdateEvent) msg;
            			List<String> fieldNames = updateEvent.getModifiedFieldNames();
            			//updateEvent.
            		}
            	}
            	//partnerInstanceBO.getPartnerInstanceById(instanceId)
                return Result.ACK_AND_NEXT;
            }
        });
		client.startTask();
	}

}
