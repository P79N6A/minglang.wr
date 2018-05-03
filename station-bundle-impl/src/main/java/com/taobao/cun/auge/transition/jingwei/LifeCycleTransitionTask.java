package com.taobao.cun.auge.transition.jingwei;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.middleware.jingwei.client.Client;
import com.alibaba.middleware.jingwei.client.ClientFactory;
import com.alibaba.middleware.jingwei.client.custom.EventMessage;
import com.alibaba.middleware.jingwei.client.custom.SimpleMessageListener;
import com.taobao.cun.auge.transition.record.LifecycleTransitionRecorder;

@Component
public class LifeCycleTransitionTask {

	@Value("${jingwei.lifecycle.transition.taskId}")
	private String taskId;
	
	private Client client;
	
	@Autowired
	private LifecycleTransitionRecorder lifecycleTransitionRecorder;
	
	private static final Logger logger = LoggerFactory.getLogger(LifeCycleTransitionTask.class);
	@PostConstruct
	public void start() {
		client = ClientFactory.create(taskId);
		client.registerMessageListener(new SimpleMessageListener() {
            @Override
            public Result onReceiveMessage(List<EventMessage> messages) {
            	for(EventMessage msg : messages){
            		try {
            			lifecycleTransitionRecorder.record(msg);
					} catch (Exception e) {
						logger.error("lifecycleTransitionRecorder error!["+msg.toJsonString()+"]",e);
					}
            		
            	}
                return Result.ACK_AND_NEXT;
            }
        });
		client.startTask();
	}

	
}
