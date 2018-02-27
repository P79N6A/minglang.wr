package com.taobao.cun.auge.notify;

import com.alibaba.fastjson.JSON;

import com.taobao.notify.message.StringMessage;
import com.taobao.notify.remotingclient.NotifyManagerBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

@Component("defaultNotifyPublish")
public class DefaultNotifyPublishImpl implements DefaultNotifyPublish{

	private static final Logger logger = LoggerFactory.getLogger(DefaultNotifyPublish.class);

	@Autowired
	NotifyManagerBean notifyPublisherManagerBean;

	@Override
    public void publish(DefaultNotifyPublishVo vo){
		Assert.notNull(vo.getTopic());
		Assert.notNull(vo.getMessageType());
			executePublish(vo);
	}
	
	private void executePublish(DefaultNotifyPublishVo vo) {
			StringMessage msg = new StringMessage();
			msg.setBody(JSON.toJSONString(vo));
			msg.setTopic(vo.getTopic());
			msg.setMessageType(vo.getMessageType());
			if (TransactionSynchronizationManager.isSynchronizationActive()) {
				//若有事务，等事务提交后发送
				TransactionSynchronizationManager
						.registerSynchronization(new TransactionSynchronizationAdapter() {
							@Override
							public void afterCommit() {
								notifyPublisherManagerBean.sendMessage(msg);
							}
						});
			} else {
				notifyPublisherManagerBean.sendMessage(msg);
			}
	}
		
}
