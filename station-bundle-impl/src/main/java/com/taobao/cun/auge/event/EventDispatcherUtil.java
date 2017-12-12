package com.taobao.cun.auge.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.AugeSystemException;
import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;
import com.taobao.cun.crius.event.client.EventDispatcher;

/**
 * 事务提交后再发送事件
 * 
 * @author linjianke
 *
 */
public class EventDispatcherUtil {
	private static String ERROR_MSG = "EVENT_DISPATCH_ERROR";
	private static final Logger logger = LoggerFactory.getLogger(EventDispatcherUtil.class);

	public static String dispatch(final String eventName, final Object obj) {
		try {
			// TransactionSynchronizationManager.isActualTransactionActive()暂时只支持事务开启时使用
			boolean isActualTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();
			return dispatctEvent(eventName, obj, isActualTransactionActive);
		} catch (Exception e) {
			String msg = getErrorMessage("dispatch", JSON.toJSONString(obj), e.getMessage());
			logger.error(msg, e);
			throw new AugeSystemException(CommonExceptionEnum.SYSTEM_ERROR);
		}

	}

	public static String dispatctEvent(final String eventName, final Object obj, boolean isActualTransactionActive) {
		if (!isActualTransactionActive) {
			logger.info("start dispatch event : eventName = {}, obj = {}", eventName, JSON.toJSONString(obj));
			return EventDispatcher.getInstance().dispatch(eventName, obj);
		}
		
		StringBuffer buffer = new StringBuffer();
		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
			@Override
			public void afterCommit() {
				try {
					logger.info("start dispatch event : eventName = {}, obj = {}", eventName, JSON.toJSONString(obj));
					buffer.append(EventDispatcher.getInstance().dispatch(eventName, obj));
				} catch (Exception e) {
					String msg = getErrorMessage("dispatch", JSON.toJSONString(obj), e.getMessage());
					logger.error(msg, e);
					throw new AugeSystemException(CommonExceptionEnum.SYSTEM_ERROR);
				}
			}
		});
		return buffer.toString();
	}

	private static final String getErrorMessage(String methodName, String param, String error) {
		StringBuilder sb = new StringBuilder();
		sb.append(ERROR_MSG).append("|").append(methodName).append("(.param=").append(param).append(").").append("errorMessage:")
				.append(error);
		return sb.toString();
	}

}
