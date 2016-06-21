package com.taobao.cun.auge.station.notify.listener;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
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
	ProcessProcessor processProcessor;

	@Autowired
	PartnerInstanceBO partnerInstanceBO;

	@Autowired
	PartnerLifecycleBO partnerLifecycleBO;

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

		try {
			handleProcessMsg(strMessage, ob);
		} catch (Exception e) {
			logger.error("Failed to handle process msg.msg= " + ob.toJSONString(), e);
		}
	}

	private void handleProcessMsg(StringMessage strMessage, JSONObject ob) throws Exception {
		String msgType = strMessage.getMessageType();
		String businessCode = ob.getString("businessCode");
		String objectId = ob.getString("objectId");
		Long stationApplyId = Long.valueOf(objectId);
		// 监听流程实例结束
		if (ProcessMsgTypeEnum.PROC_INST_FINISH.getCode().equals(msgType)) {
			JSONObject instanceStatus = ob.getJSONObject("instanceStatus");
			String resultCode = instanceStatus.getString("code");

			// 村点强制停业
			if (ProcessBusinessEnum.stationForcedClosure.getCode().equals(businessCode)) {
				processProcessor.monitorCloseApprove(stationApplyId, ProcessApproveResultEnum.valueof(resultCode));
				// 村点退出
			} else if (ProcessBusinessEnum.stationQuitRecord.getCode().equals(businessCode)) {
				processProcessor.monitorQuitApprove(stationApplyId, ProcessApproveResultEnum.valueof(resultCode));
			}
			// 节点被激活
		} else if (ProcessMsgTypeEnum.ACT_INST_START.getCode().equals(msgType)) {
			// 任务被激活
		} else if (ProcessMsgTypeEnum.TASK_ACTIVATED.getCode().equals(msgType)) {
			// 村点强制停业
			if (ProcessBusinessEnum.stationForcedClosure.getCode().equals(businessCode)) {
				processProcessor.monitorTaskStarted(stationApplyId, PartnerLifecycleBusinessTypeEnum.CLOSING);
				// 村点退出
			} else if (ProcessBusinessEnum.stationQuitRecord.getCode().equals(businessCode)) {
				processProcessor.monitorTaskStarted(stationApplyId, PartnerLifecycleBusinessTypeEnum.QUITING);
			}
		}
	}
}
