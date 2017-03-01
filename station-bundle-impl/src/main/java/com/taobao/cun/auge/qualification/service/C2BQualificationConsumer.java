package com.taobao.cun.auge.qualification.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.alibaba.pm.sc.api.quali.dto.EntityQuali;
import com.alibaba.pm.sc.api.quali.dto.ListHidByEidAndEidTypeResponse;
import com.alibaba.pm.sc.api.quali.dto.QualiLifeCycleMessage;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.taobao.cun.auge.station.adapter.SellerQualiServiceAdapter;
import com.taobao.metaq.client.MetaPushConsumer;
import com.taobao.tc.domain.util.JavaSerializationUtil;
import com.taobao.vipserver.client.utils.CollectionUtils;

@Component
public class C2BQualificationConsumer implements InitializingBean{

	@Autowired
	private CuntaoQualificationService cuntaoQualificationService;
	
	private static final Logger logger = LoggerFactory.getLogger(C2BQualificationConsumer.class);

	@Autowired
	SellerQualiServiceAdapter sellerQualiServiceAdapter;
	 @ServiceActivator(inputChannel = "c2bquali")
     public void receiveMessage(MessageExt ext) {
		 try {
			QualiLifeCycleMessage qualiLifeCycleMessage = JavaSerializationUtil.deSerialize(ext.getBody());
			EntityQuali quali = sellerQualiServiceAdapter.queryValidQualiById(qualiLifeCycleMessage.getQid(),qualiLifeCycleMessage.getEidType()).get();
			Assert.notNull(quali);
			ListHidByEidAndEidTypeResponse listHidByEidAndEidTypeResponse = sellerQualiServiceAdapter.queryHavanaIdByQuali(quali.getEid(), quali.getEidType()).get();
			Assert.notNull(listHidByEidAndEidTypeResponse);
			if(CollectionUtils.isNotEmpty(listHidByEidAndEidTypeResponse.getQualiBindHids())){
				listHidByEidAndEidTypeResponse.getQualiBindHids().stream().forEach(taobaoUserId -> cuntaoQualificationService.syncCuntaoQulification(taobaoUserId));
			}
		} catch (Exception e) {
			logger.error("receiveMessage quali error!messageId["+ext.getMsgId()+"]",e);
			throw e;
		}
     }
	@Override
	public void afterPropertiesSet() throws Exception {
		  /**
	        * 一个应用创建一个Consumer，由应用来维护此对象，可以设置为全局对象或者单例
	        * 注意：ConsumerGroupName需要由应用来保证唯一
	        */
	        MetaPushConsumer consumer = new MetaPushConsumer("CID_cuntao_quali");
	 
	        /**
	        * 一个consumer对象可以订阅多个topic 注意：可以通过指定 Tag 的方式来订阅指定 Topic 下的某一种类型的消息 Tag
	        * 表达式说明: 1. * 表示订阅指定 topic 下的所有消息 2. TagA || TagC || TagD 表示订阅指定 topic
	        * 下 tags 分别等于 TagA 或 TagC 或 TagD 的消息
	        */
	        consumer.subscribe("quali-life-cycle", "*");
	 
	        consumer.registerMessageListener(new MessageListenerConcurrently() {
	            /**
	            * 1、默认msgs里只有一条消息，可以通过设置consumeMessageBatchMaxSize参数来批量接收消息
	            * 2、如果设置为批量消费方式，要么都成功，要么都失败.
	            * 3、此方法由MetaQ客户端多个线程回调，需要应用来处理并发安全问题
	            * 4、抛异常与返回ConsumeConcurrentlyStatus.RECONSUME_LATER等价
	            * 5、每条消息失败后，会尝试重试，重试5次都失败，则丢弃
	            */
	            @Override
	            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
	                System.out.println(Thread.currentThread().getName() + " Receive New Messages: " + msgs);
	                for (MessageExt msg : msgs) {
	                    /**
	                    * 对消息进行处理. 1. 如果消息处理成功, 请 return
	                    * ConsumeConcurrentlyStatus.CONSUME_SUCCESS; 2. 如果消息处理失败, 请
	                    * return
	                    * ConsumeConcurrentlyStatus.RECONSUME_LATER,服务端会再次对该消息进行投递;
	                    */
	                		 C2BQualificationConsumer.this.receiveMessage(msg);
	                    }
	                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	                }
	            }
	        );
	 
	        /**
	        * Consumer对象在使用之前必须要调用start初始化，初始化一次即可
	        */
	        consumer.start();
	 
	        System.out.println("Consumer Started.");
		
	}
}
