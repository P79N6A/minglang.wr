package com.taobao.cun.auge.station.notify.listener;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.taobao.cun.auge.station.enums.ProcessApproveResultEnum;
import com.taobao.cun.auge.station.enums.ProcessBusinessEnum;
import com.taobao.cun.auge.station.enums.ProcessMsgTypeEnum;
import com.taobao.notify.message.Message;
import com.taobao.notify.message.StringMessage;
import com.taobao.notify.remotingclient.MessageListener;
import com.taobao.notify.remotingclient.MessageStatus;

@Component("processListener")
public class ProcessListener implements MessageListener {

	private static final Logger logger = LoggerFactory.getLogger(ProcessListener.class);

	@Autowired
	ProcessApproveResultProcessor processApproveResultProcessor;

	@Override
	public void receiveMessage(Message message, MessageStatus status) {
		logger.info("BpmNotifyRecieveImpl receiveMessage start");
		if (message == null)
			return;

		if (!(message instanceof StringMessage))
			return;

		StringMessage strMessage = (StringMessage) message;
		if (StringUtils.isEmpty(strMessage.getBody())) {
			return;
		}
		JSONObject ob = (JSONObject) JSONObject.parse(strMessage.getBody());
		logger.info("BpmNotifyRecieveImpl notify:" + ob.toJSONString());

		String msgType = strMessage.getMessageType();
		// 监听流程实例结束
		if (ProcessMsgTypeEnum.PROC_INST_FINISH.getCode().equals(msgType)) {
			String businessCode = ob.getString("businessCode");
			String resultCode = ob.getString("instanceStatus");
			String objectId = ob.getString("objectId");
			String remarks = ob.getString("remarks");

			// 村点强制停业
			if (ProcessBusinessEnum.stationForcedClosure.getCode().equals(businessCode)
					|| ProcessBusinessEnum.TPV_FORCED_CLOSURE.getCode().equals(businessCode)) {
				processCloseApproveResult(resultCode, objectId);
				// 村点退出
			} else if (ProcessBusinessEnum.stationQuitRecord.getCode().equals(businessCode)
					|| ProcessBusinessEnum.TPV_QUIT.getCode().equals(businessCode)) {
				processQuitApproveResult(resultCode, objectId);
			}
			// 节点被激活
		} else if (ProcessMsgTypeEnum.ACT_INST_START.getCode().equals(msgType)) {
			// 任务被激活
		} else if (ProcessMsgTypeEnum.TASK_ACTIVATED.getCode().equals(msgType)) {

		}
	}

	/**
	 * 监听停业审批结果
	 * 
	 * @param resultCode
	 * @param objectId
	 */
	private void processQuitApproveResult(String resultCode, String objectId) {
		try {
			Long stationApplyId = Long.valueOf(objectId);
			processApproveResultProcessor.monitorQuitApprove(stationApplyId,
					ProcessApproveResultEnum.valueof(resultCode));
		} catch (Exception e) {
			logger.error("监听审批退出流程失败。stationApplyId = " + objectId);
		}
	}

	/**
	 * 监听退出审批结果
	 * 
	 * @param resultCode
	 * @param objectId
	 */
	private void processCloseApproveResult(String resultCode, String objectId) {
		try {
			Long stationApplyId = Long.valueOf(objectId);
			processApproveResultProcessor.monitorCloseApprove(stationApplyId,
					ProcessApproveResultEnum.valueof(resultCode));
		} catch (Exception e) {
			logger.error("监听审批停业流程失败。stationApplyId = " + objectId);
		}
	}
}
