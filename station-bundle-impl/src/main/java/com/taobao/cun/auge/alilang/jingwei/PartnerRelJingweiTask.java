package com.taobao.cun.auge.alilang.jingwei;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.middleware.jingwei.client.Client;
import com.alibaba.middleware.jingwei.client.ClientFactory;
import com.alibaba.middleware.jingwei.client.custom.EventMessage;
import com.alibaba.middleware.jingwei.client.custom.SimpleMessageListener;

@Component
public class PartnerRelJingweiTask extends JingweiTask {
	@Value("${jingwei.taskid.partnerrel}")
	private String taskId;
	
	private Client client;
	
	@Override
	public void start() {
		client = ClientFactory.create(taskId);
		client.registerMessageListener(new SimpleMessageListener() {
            @Override
            public Result onReceiveMessage(List<EventMessage> messages) {
                return Result.ACK_AND_NEXT;
            }
        });
		client.startTask();
	}

}
