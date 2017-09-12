package com.taobao.cun.auge.asset.service;

import javax.annotation.PostConstruct;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.message.MessageExt;

import com.taobao.cun.auge.asset.bo.AssetBO;
import com.taobao.cun.auge.asset.dto.AssetDto;
import com.taobao.metaq.client.MetaPushConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AssetScrapConsumer {

    private static final Logger logger = LoggerFactory.getLogger(AssetScrapConsumer.class);

    @Value("${it.scrap.assetTopic}")
    private String assetTopic;

    @Value("${it.scrap.assetTag}")
    private String assetTag;

    @Value("${it.scrap.consumerId}")
    private String consumerId;

    private MetaPushConsumer consumer;

    @Autowired
    private AssetBO assetBO;

    @PostConstruct
    public void init() throws MQClientException {
        consumer = new MetaPushConsumer(consumerId);
        consumer.subscribe(assetTopic, assetTag);
        consumer.registerMessageListener((MessageListenerConcurrently)(msgList, context) -> {
                for (MessageExt msg : msgList) {
                    receiveMessage(msg);
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        );
        consumer.start();
    }

    public void receiveMessage(MessageExt ext) {
        if (logger.isInfoEnabled()) {
            logger.info("{bizType}, receive asset message", "assetInfo");
        }
        JSONObject object = (JSONObject)JSONObject.parse(ext.getBody());
        String aliNo = object.getString("resourceCode");
        String owner = object.getString("owner");
        AssetDto assetDto = new AssetDto();
        assetDto.setAliNo(aliNo);
        assetDto.setOperator(owner);
        assetBO.confirmScrapAsset(assetDto);
    }

}
