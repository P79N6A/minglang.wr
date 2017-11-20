package com.taobao.cun.auge.qualification.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.pm.sc.portal.api.quali.dto.lifecycle.BizCertLifeCycleMessage;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.taobao.metaq.client.MetaPushConsumer;
import com.taobao.tc.domain.util.JavaSerializationUtil;

@Component
public class C2BQualificationConsumer {

	@Autowired
	private CuntaoQualificationService cuntaoQualificationService;
	
	private static final Logger logger = LoggerFactory.getLogger(C2BQualificationConsumer.class);

	@Value("${qualiTopic}")
	private String qualiTopic;
	
	@Value("${qualiCID}")
	private String qualiCID;
	
	@Value("${qualiInfoId}")
	private Long qualiInfoId;
	
	private MetaPushConsumer consumer;
	 
	
	@PostConstruct
	public void init() throws MQClientException {
		 consumer = new MetaPushConsumer(qualiCID);
	        consumer.subscribe(qualiTopic, "BIZ_CERT");
	 
	        consumer.registerMessageListener(new MessageListenerConcurrently() {
	            @Override
	            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
	                for (MessageExt msg : msgs) {
	                		receiveMessage(msg);
	                    }
	                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	                }
	            }
	        );
	        consumer.start();
	}
	
	

    public void receiveMessage(MessageExt ext) {
			logger.info("recevieQualiMessage:["+ext.toString()+"]");
			BizCertLifeCycleMessage qualiLifeCycleMessage = null;
			try {
				 qualiLifeCycleMessage = JavaSerializationUtil.deSerialize(ext.getBody());
			} catch (Exception e) {
				logger.error("deSerialize qualiLifeCycleMessage error!",e);
				return;
			}
			if(qualiLifeCycleMessage == null){
				logger.error("qualiLifeCycleMessage is null!");
				return;
			}
			if(BizCertLifeCycleMessage.LIFE_CYCLE_ABNORMAL.equals(qualiLifeCycleMessage.getLifeCycleType())
			||BizCertLifeCycleMessage.LIFE_CYCLE_NEW.equals(qualiLifeCycleMessage.getLifeCycleType())
			||BizCertLifeCycleMessage.LIFE_CYCLE_AUDIT_FAIL.equals(qualiLifeCycleMessage.getLifeCycleType())
			||BizCertLifeCycleMessage.LIFE_CYCLE_UPDATE.equals(qualiLifeCycleMessage.getLifeCycleType())
			||BizCertLifeCycleMessage.LIFE_CYCLE_NORMAL.equals(qualiLifeCycleMessage.getLifeCycleType())){
				cuntaoQualificationService.syncCuntaoQulification(qualiLifeCycleMessage.getUserId());
			}
     }
    
    
}
