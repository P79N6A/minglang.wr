package com.taobao.cun.auge.qualification.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.alibaba.pm.sc.api.quali.dto.EntityQuali;
import com.alibaba.pm.sc.api.quali.dto.ListHidByEidAndEidTypeResponse;
import com.alibaba.pm.sc.api.quali.dto.QualiLifeCycleMessage;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.taobao.cun.auge.station.adapter.SellerQualiServiceAdapter;
import com.taobao.metaq.client.MetaPushConsumer;
import com.taobao.tc.domain.util.JavaSerializationUtil;
import com.taobao.vipserver.client.utils.CollectionUtils;

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
	 
	@Autowired
	private SellerQualiServiceAdapter sellerQualiServiceAdapter;
	
	@PostConstruct
	public void init() throws MQClientException {
		 consumer = new MetaPushConsumer(qualiCID);
		 
	        consumer.subscribe(qualiTopic, "*");
	 
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
			QualiLifeCycleMessage qualiLifeCycleMessage = JavaSerializationUtil.deSerialize(ext.getBody());
			EntityQuali quali = sellerQualiServiceAdapter.queryQualiById(qualiLifeCycleMessage.getQid(),qualiLifeCycleMessage.getEidType()).get();
			Assert.notNull(quali);
			//不是营业执照的消息不处理
			if(quali.getQuali().getQualiInfoId() !=qualiInfoId){
				return;
			}
			ListHidByEidAndEidTypeResponse listHidByEidAndEidTypeResponse = sellerQualiServiceAdapter.queryHavanaIdByQuali(quali.getEid(), quali.getEidType()).get();
			Assert.notNull(listHidByEidAndEidTypeResponse);
			if(CollectionUtils.isNotEmpty(listHidByEidAndEidTypeResponse.getQualiBindHids())){
				listHidByEidAndEidTypeResponse.getQualiBindHids().stream().forEach(taobaoUserId -> cuntaoQualificationService.syncCuntaoQulificationFromMetaq(taobaoUserId,qualiLifeCycleMessage.getQid(),qualiLifeCycleMessage.getEidType()));
			}
     }
    
}
