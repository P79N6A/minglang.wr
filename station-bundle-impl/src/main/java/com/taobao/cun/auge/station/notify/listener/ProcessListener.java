package com.taobao.cun.auge.station.notify.listener;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.service.PatnerInstanceService;
import com.taobao.cun.crius.bpm.enums.NodeActionEnum;
import com.taobao.notify.message.Message;
import com.taobao.notify.message.StringMessage;
import com.taobao.notify.remotingclient.MessageListener;
import com.taobao.notify.remotingclient.MessageStatus;

public class ProcessListener implements MessageListener {

	private static final Logger logger = LoggerFactory.getLogger(ProcessListener.class);

	@Autowired
	PartnerInstanceBO partnerInstanceBO;

	@Autowired
	StationBO stationBO;

	@Autowired
	PatnerInstanceService patnerInstanceService;

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
		// FIXME FHH 枚举未写
		// 任务完成
		if ("TASK_COMPLETED".equals(msgType)) {
			String businessCode = ob.getString("businessCode");
			String taskResult = ob.getString("result");
			String approver = ob.getString("approver");
			String stationApplyId = ob.getString("objectId");
			if ("stationForcedClosure".equals(businessCode)) {
				try {
					patnerInstanceService.auditClose(Long.valueOf(stationApplyId), approver,
							NodeActionEnum.AGREE.getCode().equals(taskResult));
				} catch (Exception e) {
					logger.error("监听审批停业流程失败。stationApplyId = " + stationApplyId);
				}
			}else if("stationQuitRecord".equals(businessCode)){
				try {
					patnerInstanceService.auditQuit(Long.valueOf(stationApplyId), approver,
							NodeActionEnum.AGREE.getCode().equals(taskResult));
				} catch (Exception e) {
					logger.error("监听审批停业流程失败。stationApplyId = " + stationApplyId);
				}
			}
		} else if ("ACT_INST_START".equals(msgType)) {

		}
	}

}
