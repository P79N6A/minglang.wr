package com.taobao.cun.auge.alilang.jingwei;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

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
import com.google.common.base.Strings;
import com.taobao.notify.message.StringMessage;
import com.taobao.notify.remotingclient.NotifyManager;
import com.taobao.notify.remotingclient.SendResult;

/**
 * 更换手机号
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class PartnerJingweiTask extends JingweiTask {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Value("${jingwei.taskid.partner}")
	private String taskId;
	
	@Value("${notify.alilang.topic}")
	private String topic;
	
	private Client client;
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
            				
            				String mobile = (String) modifiedRow.get("mobile");
            				String alilangUserId = (String) row.get("alilang_user_id");
            				if(!Strings.isNullOrEmpty(mobile) && !Strings.isNullOrEmpty(alilangUserId)){
            					PartnerMessage partnerMessage = new PartnerMessage();
            					partnerMessage.setTaobaoUserId((long) row.get("taobao_user_id"));
            					partnerMessage.setMobile(mobile);
            					partnerMessage.setAction("update");
            					partnerMessage.setName((String) row.get("name"));
            					partnerMessage.setAlilangUserId((String) row.get("alilang_user_id"));
            					String str = JSONObject.toJSONString(partnerMessage);
            					
            					logger.info("update alilang user:{}", str);
            					
            					StringMessage stringMessage = new StringMessage();
            					stringMessage.setBody(str);
            					stringMessage.setTopic(topic);
            					stringMessage.setMessageType("sync-alilang-user");
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
                return Result.ACK_AND_NEXT;
            }
        });
		client.startTask();
	}

}
