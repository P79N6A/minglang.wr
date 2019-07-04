package com.taobao.cun.auge.cuncounty.bo;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.event.EventDispatcherUtil;
import com.taobao.cun.auge.platform.dto.AppMsgPushInfoDto;
import com.taobao.cun.crius.event.ExtEvent;
import org.springframework.stereotype.Component;

@Component
public class CuntaoCountyAlarmBo {
    public void alarm(){
        AppMsgPushInfoDto appMsgPushInfoDto = new AppMsgPushInfoDto();
        AppMsgPushInfoDto infoDto = new AppMsgPushInfoDto();
        infoDto.setAction("all");

        infoDto.setAppId("cuntaoCRM");

        AppMsgPushInfoDto.AppMsgPushContent content = infoDto.new AppMsgPushContent();
        content.setBizId(0L);
        content.setImageUrl(null);
        //content.setRouteUrl("https://market.m.taobao.com/app/ctm/cunStoreManage/pages/store_inspection_detail?wh_weex=true&inspectionId=" + inspectionInsId);
        content.setTitle("政府事务");

        content.setPushTitle("政府事务测试");
        content.setContent("政府事务测试");

        infoDto.setContent(content);
        infoDto.setMsgType("cuntaoCRM" + "StoreInspection");
        infoDto.setMsgTypeDetail("StoreInspection");
        infoDto.setReceivers(Lists.newArrayList("19403"));

        infoDto.setReceiverType("EMPIDS");

        infoDto.setSender(0L);

        EventDispatcherUtil.dispatch("CUN_APP_STATION_INSPECTION_MSG_PUSH", new ExtEvent(JSON.toJSONString(infoDto)));
    }
}
