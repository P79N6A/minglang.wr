package com.taobao.cun.auge.cuncounty.tag;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.common.PageOutput;
import com.taobao.cun.auge.cuncounty.bo.CuntaoCountyQueryBo;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyCondition;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyListItem;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyStateEnum;
import com.taobao.cun.auge.event.EventDispatcherUtil;
import com.taobao.cun.auge.platform.dto.AppMsgPushInfoDto;
import com.taobao.cun.crius.event.ExtEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class CuntaoCountyAlarmBo {
    private final static String RENEW_ALARM = "您所负责的【%s】合同于%d天到期，请及时进行处理";
    private final static String NEW_ALARM = "您所负责的【%s】未签订合同，请您判断是否同政府有合作关系，若有及时到ORG补全政府协议信息";
    private final static String FILL_ALARM = "您所负责的【%s】疑似未签协议，请您及时到ORG补充政府协议编号及协议时间";

    @Resource
    private Publisher publisher;

    public void alarm(){

    }

    private void doAlarm(String textcontent){
        AppMsgPushInfoDto appMsgPushInfoDto = new AppMsgPushInfoDto();
        AppMsgPushInfoDto infoDto = new AppMsgPushInfoDto();
        infoDto.setAction("all");
        infoDto.setAppId("cuntaoCRM");
        AppMsgPushInfoDto.AppMsgPushContent content = infoDto.new AppMsgPushContent();
        content.setBizId(0L);
        content.setImageUrl(null);
        content.setTitle(textcontent);
        content.setPushTitle("请注意跟进");
        content.setContent("请注意跟进");
        infoDto.setContent(content);
        infoDto.setMsgType("cuntaoCRM" + "StoreInspection");
        infoDto.setMsgTypeDetail("StoreInspection");
        infoDto.setReceivers(Lists.newArrayList("19403"));
        infoDto.setReceiverType("EMPIDS");
        infoDto.setSender(0L);
        EventDispatcherUtil.dispatch("CUN_APP_STATION_INSPECTION_MSG_PUSH", new ExtEvent(JSON.toJSONString(infoDto)));
    }
}
