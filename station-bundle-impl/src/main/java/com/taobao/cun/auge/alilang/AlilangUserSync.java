package com.taobao.cun.auge.alilang;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.middleware.jingwei.client.Client;
import com.alibaba.middleware.jingwei.client.ClientFactory;
import com.alibaba.middleware.jingwei.client.custom.EventMessage;
import com.alibaba.middleware.jingwei.client.custom.SimpleMessageListener;

@Component
public class AlilangUserSync implements InitializingBean {

	@Value("${jingwei.taskid.partner}")
	private String taskId;
	
	private Client client;
	
	@Override
	public void afterPropertiesSet() throws Exception {
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
