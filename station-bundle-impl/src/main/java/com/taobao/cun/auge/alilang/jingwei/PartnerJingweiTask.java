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
import com.google.common.base.Strings;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.notify.message.StringMessage;
import com.taobao.notify.remotingclient.NotifyManagerBean;
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
	
	private Client client;
	@Autowired
    private NotifyManagerBean notifyPublisherManagerBean;
	@Resource
	private PartnerInstanceBO partnerInstanceBO;
	
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
            				PartnerInstanceDto partnerInstanceDto = partnerInstanceBO.getCurrentPartnerInstanceByPartnerId((long) row.get("id"));
            				String mobile = (String) modifiedRow.get("mobile"); //new mobile
            				if(!Strings.isNullOrEmpty(mobile) && partnerInstanceDto != null 
            						&& partnerInstanceDto.getType().getCode().equals(PartnerInstanceTypeEnum.TP.getCode()) 
            						&& (partnerInstanceDto.getState().getCode().equals(PartnerInstanceStateEnum.DECORATING.getCode())
            						|| partnerInstanceDto.getState().getCode().equals(PartnerInstanceStateEnum.SERVICING.getCode()))){
            					String omobile = (String) row.get("mobile");    //old mobile
            					PartnerMessage partnerMessage = new PartnerMessage();
            					partnerMessage.setTaobaoUserId((long) row.get("taobao_user_id"));
            					partnerMessage.setMobile(mobile);
            					partnerMessage.setOmobile(omobile);
            					partnerMessage.setAction("update");
            					partnerMessage.setEmail((String) row.get("email"));
            					partnerMessage.setName((String) row.get("name"));
            					partnerMessage.setAlilangUserId((String) row.get("alilang_user_id"));
            					partnerMessage.setAlilangOrgId(alilangOrgId);
            					String str = JSONObject.toJSONString(partnerMessage);
            					
            					logger.info("update alilang user:{}", str);
            					
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
                return Result.ACK_AND_NEXT;
            }
        });
		client.startTask();
	}

}
