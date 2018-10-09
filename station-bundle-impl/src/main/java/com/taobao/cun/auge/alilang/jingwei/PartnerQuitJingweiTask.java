package com.taobao.cun.auge.alilang.jingwei;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.middleware.jingwei.client.Client;
import com.alibaba.middleware.jingwei.client.ClientFactory;
import com.alibaba.middleware.jingwei.client.custom.EventMessage;
import com.alibaba.middleware.jingwei.client.custom.SimpleMessageListener;
import com.alibaba.middleware.jingwei.client.custom.UpdateEvent;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.crius.partner.dto.PartnerDto;
import com.taobao.notify.message.StringMessage;
import com.taobao.notify.remotingclient.NotifyManagerBean;
import com.taobao.notify.remotingclient.SendResult;

/**
 * 合伙人退出时通知到阿里郎
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class PartnerQuitJingweiTask extends JingweiTask {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Value("${jingwei.taskid.partnerrel}")
	private String taskId;
	
	private Client client;
	
	@Resource
	private PartnerInstanceBO partnerInstanceBO;
	@Autowired
    private NotifyManagerBean notifyPublisherManagerBean;
	
	@Override
	public void start() {
		client = ClientFactory.create(taskId);
		client.registerMessageListener(new SimpleMessageListener() {
            @Override
            public Result onReceiveMessage(List<EventMessage> messages) {
            	for(EventMessage msg : messages){
            		if(msg instanceof UpdateEvent){
            			UpdateEvent updateEvent = (UpdateEvent) msg;
            			List<Map<String, Serializable>> modifiedRows = updateEvent.getModifyRowDataMaps();
            			for(int index = 0; index < modifiedRows.size(); index++){
            				Map<String, Serializable> modifiedRow = modifiedRows.get(index);
            				Map<String, Serializable> row = updateEvent.getRowDataMaps().get(index);
            				
            				String state = (String) modifiedRow.get("state");
            				if(PartnerInstanceStateEnum.QUIT.getCode().equals(state)){
            					PartnerInstanceDto partnerInstanceDto = partnerInstanceBO.getPartnerInstanceById((Long) row.get("id"));
            					PartnerDto partnerDto = partnerInstanceDto.getPartnerDto();
            					if(PartnerInstanceTypeEnum.TP.equals(partnerInstanceDto.getType())){
	            					PartnerMessage partnerMessage = new PartnerMessage();
	            					partnerMessage.setTaobaoUserId(partnerDto.getTaobaoUserId());
	            					partnerMessage.setMobile(partnerDto.getMobile());
	            					partnerMessage.setAlilangUserId(partnerDto.getAliLangUserId());
	            					partnerMessage.setName(partnerDto.getName());
	            					partnerMessage.setAction("quit");
	            					partnerMessage.setAlilangOrgId(alilangOrgId);
	            					String str = JSONObject.toJSONString(partnerMessage);
	            					
	            					logger.info("quit alilang user:{}", str);
	            					
	            					StringMessage stringMessage = new StringMessage();
	            					stringMessage.setBody(str);
	            					stringMessage.setTopic(topic);
	            					stringMessage.setMessageType(messageType);
	            					SendResult sendResult = notifyPublisherManagerBean.sendMessage(stringMessage);
	            					if(sendResult.isSuccess()){
	            						logger.info("messageId:{}", sendResult.getMessageId());
	            					}else{
	            						logger.error("send message error.{}", sendResult.getErrorMessage());
	            					}
            					}
            				}
            			}
            		}
            	}
                return Result.ACK_AND_NEXT;
            }
        });
		client.startTask();
	}

}
