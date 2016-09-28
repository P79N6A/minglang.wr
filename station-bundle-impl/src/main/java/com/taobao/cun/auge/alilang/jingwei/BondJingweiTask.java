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

import com.alibaba.middleware.jingwei.client.Client;
import com.alibaba.middleware.jingwei.client.ClientFactory;
import com.alibaba.middleware.jingwei.client.custom.EventMessage;
import com.alibaba.middleware.jingwei.client.custom.SimpleMessageListener;
import com.alibaba.middleware.jingwei.client.custom.UpdateEvent;
import com.google.common.collect.Maps;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBondEnum;
import com.taobao.notify.message.StringMessage;
import com.taobao.notify.remotingclient.NotifyManager;
import com.taobao.notify.remotingclient.SendResult;
import com.taobao.tair.json.JSONObject;

@Component
public class BondJingweiTask extends JingweiTask {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Value("${jingwei.taskid.bond}")
	private String taskId;
	
	private Client client;
	
	@Resource
	private PartnerInstanceBO partnerInstanceBO;
	@Autowired
    private NotifyManager notifyManager;
	
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
            				
            				String bond = (String) modifiedRow.get("bond");
            				if(PartnerLifecycleBondEnum.HAS_FROZEN.getCode().equals(bond)){
            					PartnerInstanceDto partnerInstanceDto = partnerInstanceBO.getPartnerInstanceById((Long) row.get("partner_instance_id"));
            					PartnerDto partnerDto = partnerInstanceDto.getPartnerDto();
            					Map<String, Object> message = Maps.newHashMap();
            					message.put("taobaoUserId", partnerDto.getTaobaoUserId());
            					message.put("mobile", partnerDto.getMobile());
            					message.put("action", "new");
            					String str = JSONObject.toJSONString(message);
            					
            					logger.info("register alilang user:{}", str);
            					
            					StringMessage stringMessage = new StringMessage();
            					stringMessage.setBody(str);
            					stringMessage.setTopic("S-auge-alilang");
            					stringMessage.setMessageType("add-alilang-user");
            					SendResult sendResult = notifyManager.sendMessage(stringMessage);
            					if(sendResult.isSuccess()){
            						logger.info("messageId:{}", sendResult.getMessageId());
            					}else{
            						logger.error("send message error.{}", sendResult.getErrorMessage());
            					}
            				}
            			}
            		}
            	}
            	//partnerInstanceBO.getPartnerInstanceById(instanceId)
                return Result.ACK_AND_NEXT;
            }
        });
		client.startTask();
	}

}
