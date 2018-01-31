package com.taobao.cun.auge.station.notify.listener;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;

import com.taobao.cun.auge.alilang.AlilangUserRegister;
import com.taobao.cun.auge.station.bo.PartnerExamBO;
import com.taobao.cun.auge.station.bo.PartnerPeixunBO;
import com.taobao.cun.auge.station.enums.NotifyContents;
import com.taobao.notify.message.Message;
import com.taobao.notify.message.ObjectMessage;
import com.taobao.notify.message.StringMessage;
import com.taobao.notify.remotingclient.MessageListener;
import com.taobao.notify.remotingclient.MessageStatus;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("defaultListener")
public class DefaultListener implements MessageListener {

	private static final Logger logger = LoggerFactory.getLogger(DefaultListener.class);

	@Autowired
	ProcessProcessor processProcessor;
	
	@Autowired
	PartnerPeixunBO partnerPeixunBO;
	@Resource
	private AlilangUserRegister alilangUserRegister;
	@Autowired
	PartnerExamBO partnerExamBO;
	@Override
	public void receiveMessage(Message message, MessageStatus status) {
		logger.info("DefaultListener receiveMessage start");
		if (message == null) {
            return;
        }
		if (message instanceof StringMessage){
			StringMessage strMessage = (StringMessage) message;
			if (StringUtils.isEmpty(strMessage.getBody())) {
				return;
			}
			try {
				logger.info("DefaultListener notify body:" + strMessage.getBody());
				JSONObject ob = (JSONObject) JSONObject.parse(strMessage.getBody());
				logger.info("DefaultListener notify:" + ob.toJSONString());
				handleMsg(strMessage, ob);
			} catch (Exception e) {
				logger.error("DefaultListener, " + (StringMessage)message, e);
				logger.error("DefaultListener, body=" + strMessage.getBody(), e);
				throw new RuntimeException("DefaultListener, " + (StringMessage)message, e);
			}
		}else if(message instanceof ObjectMessage){
			ObjectMessage objMessage = (ObjectMessage) message;
			try {
				logger.info("DefaultListener notify body:" + objMessage.toString());
				handleMsg(objMessage);
			} catch (Exception e) {
				logger.error("DefaultListener, " + (StringMessage)message, e);
				logger.error("DefaultListener, body=" + objMessage.toString(), e);
				throw new RuntimeException("DefaultListener, " + (StringMessage)message, e);
			}
		}else{
			return;
		}
		
	}
	
	
	private void handleMsg(ObjectMessage objMessage) throws Exception{
		if(NotifyContents.PEIXUN_REFUND_FINISH.equals(objMessage.getTopic())){
			partnerPeixunBO.handleRefundFinishSucess(objMessage);
		}else{
			logger.warn("unknow msgTopic:"+objMessage.toString());
		}
	}
	
	private void handleMsg(StringMessage strMessage,JSONObject ob) throws Exception{
		if(NotifyContents.CUNTAO_CRIUS_PROCESS.equals(strMessage.getTopic())){
			processProcessor.handleProcessMsg(strMessage,ob);
		}else if(NotifyContents.CUNXUEXI_PEIXUN_COMPLETE_TOPIC.equals(strMessage.getTopic())){
			partnerPeixunBO.handlePeixunFinishSucess(strMessage, ob);
		}else if(NotifyContents.CRM_ORDER_PAYMENT_SUCESS.equals(strMessage.getTopic())){
			partnerPeixunBO.handlePeixunPaymentProcess(strMessage, ob);
		}else if(NotifyContents.ALILANG_REGISTER_TOPIC.equals(strMessage.getTopic())){ 
			alilangUserRegister.register(ob.getString("mobile"), ob.getString("alilangUid"));
		}else if(NotifyContents.EXAM_FINISH.equals(strMessage.getTopic())){
			partnerExamBO.handleExamFinish(strMessage, ob);
		}else{
			logger.warn("unknow msgTopic:"+strMessage.getTopic());
		}
	}

}
