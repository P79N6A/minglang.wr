package com.taobao.cun.auge.cuncounty.alarm;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.event.EventDispatcherUtil;
import com.taobao.cun.auge.platform.dto.AppMsgPushInfoDto;
import com.taobao.cun.crius.event.ExtEvent;
import org.springframework.stereotype.Component;

@Component
public class AlarmTest {
    public void alarm(Long bizId, String textContent, String msgType){
        AppMsgPushInfoDto appMsgPushInfoDto = new AppMsgPushInfoDto();
        AppMsgPushInfoDto infoDto = new AppMsgPushInfoDto();
        infoDto.setAction("all");
        infoDto.setAppId("cuntaoCRM");
        AppMsgPushInfoDto.AppMsgPushContent content = infoDto.new AppMsgPushContent();
        content.setBizId(bizId);
        content.setTitle(textContent);
        content.setPushTitle("请注意跟进");
        content.setContent("请注意跟进");
        infoDto.setContent(content);
        infoDto.setMsgType("cuntaoCRM" + msgType);
        infoDto.setMsgTypeDetail(msgType);
        infoDto.setReceivers(Lists.newArrayList("19403"));
        infoDto.setReceiverType("EMPIDS");
        infoDto.setSender(0L);
        EventDispatcherUtil.dispatch("CUN_APP_STATION_INSPECTION_MSG_PUSH", new ExtEvent(JSON.toJSONString(appMsgPushInfoDto)));
    }
}
