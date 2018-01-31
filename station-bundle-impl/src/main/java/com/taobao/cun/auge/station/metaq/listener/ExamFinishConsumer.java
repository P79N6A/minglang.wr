package com.taobao.cun.auge.station.metaq.listener;

import java.util.List;

import javax.annotation.PostConstruct;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.message.MessageExt;

import com.taobao.cun.auge.station.bo.PartnerExamBO;
import com.taobao.metaq.client.MetaPushConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ExamFinishConsumer {

	private static final Logger logger = LoggerFactory.getLogger(ExamFinishConsumer.class);

	
	@Value("${examCID}")
	private String examCID;
	
	
	
	private MetaPushConsumer consumer;
	 
	@Autowired
	private PartnerExamBO partnerExamBO;
	
	@PostConstruct
	public void init() throws MQClientException {
		 consumer = new MetaPushConsumer(examCID);
		 
	        consumer.subscribe("cuntao_exam_finish", "*");
	 
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
		 try {
			JSONObject json = JSON.parseObject(new String(ext.getBody()));
			partnerExamBO.handleExamFinish(json);
		 } catch (Exception e) {
			logger.error("receiveMessage exam error!messageId["+ext.getMsgId()+"]",e);
			throw e;
		}
     }
    
}
