package com.taobao.cun.auge.notify;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;
import com.taobao.notify.message.StringMessage;
import com.taobao.notify.remotingclient.NotifyManager;

@Component("defaultNotifyPublish")
public class DefaultNotifyPublishImpl implements DefaultNotifyPublish{

	private static final Logger logger = LoggerFactory.getLogger(DefaultNotifyPublish.class);

	@Autowired
	NotifyManager notifyManager;

	public void publish(DefaultNotifyPublishVo vo){
		Assert.notNull(vo.getTopic());
		Assert.notNull(vo.getMessageType());
		try {
			executePublish(vo);
		} catch (Exception e) {
			logger.error("publish notify error "+JSON.toJSONString(vo), e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
	}
	
	private void executePublish(DefaultNotifyPublishVo vo) {
		try {
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
								notifyManager.sendMessage(msg);
							}
						});
			} else {
				notifyManager.sendMessage(msg);
			}
		} catch (Exception e) {
			logger.error("publish notify error " + JSON.toJSONString(vo), e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
	}
		
}
