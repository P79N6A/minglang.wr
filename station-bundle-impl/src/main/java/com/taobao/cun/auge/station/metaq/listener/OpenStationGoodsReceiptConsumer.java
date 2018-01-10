package com.taobao.cun.auge.station.metaq.listener;

import javax.annotation.PostConstruct;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.message.MessageExt;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.enums.PartnerLifecycleGoodsReceiptEnum;
import com.taobao.cun.auge.store.bo.StoreReadBO;
import com.taobao.cun.auge.store.dto.StoreDto;
import com.taobao.metaq.client.MetaPushConsumer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OpenStationGoodsReceiptConsumer {

    private static final Logger logger = LoggerFactory.getLogger(OpenStationGoodsReceiptConsumer.class);

    @Value("${openStation.goodsReceipt.Topic}")
    private String topic;

    @Value("${openStation.goodsReceipt.tag}")
    private String tag;

    @Value("${openStation.goodsReceipt.consumerId}")
    private String consumerId;

    private MetaPushConsumer consumer;

    @Autowired
    PartnerLifecycleBO partnerLifecycleBO;
    
    @Autowired
	private StoreReadBO storeReadBO;
    
    @Autowired
    PartnerInstanceBO partnerInstanceBO;

    @PostConstruct
    public void init() throws MQClientException {
        consumer = new MetaPushConsumer(consumerId);
        consumer.subscribe(topic, tag);
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
            logger.info("{bizType}, receive openStationGoodsReceiptConsumer message", "goodsReceipt");
        }
        JSONObject object = (JSONObject)JSONObject.parse(ext.getBody());
        String storeId = object.getString("storeId");
        if (StringUtils.isEmpty(storeId)) {
        	logger.error("{bizType}, receive openStationGoodsReceiptConsumer storeId is null", "goodsReceipt");
        }
        StoreDto sDto = storeReadBO.getStoreBySharedStoreId(Long.parseLong(storeId));
        if (sDto == null) {
        	logger.error("{bizType}, receive openStationGoodsReceiptConsumer StoreDto is null param:"+storeId, "goodsReceipt");
        }
        PartnerStationRel psr = partnerInstanceBO.getActivePartnerInstance(sDto.getTaobaoUserId());
        
        partnerLifecycleBO.updateGoodsReceipt(psr.getId(),PartnerLifecycleGoodsReceiptEnum.Y, OperatorDto.defaultOperator());
    }

}
