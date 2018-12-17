package com.taobao.cun.auge.station.bo.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.event.EventDispatcherUtil;
import com.taobao.cun.auge.platform.dto.AppMsgPushInfoDto;
import com.taobao.cun.auge.station.bo.StationDecorateMessageBo;
import com.taobao.cun.crius.event.ExtEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @author: qingwu.yhp
 * @Date: 2018-12-17 10:46
 */
@Component
public class StationDecorateMessageBoImpl implements StationDecorateMessageBo {

    private static final Logger logger = LoggerFactory.getLogger(StationDecorateMessageBoImpl.class);

    @Autowired
    private DiamondConfiguredProperties diamondConfiguredProperties;

    @Override
    public void pushStationDecorateDesignPassMessage(Long taobaoUserId) {
        AppMsgPushInfoDto appMsgPushInfoDto = buildAppMsgPushInfoDto(taobaoUserId);
        appMsgPushInfoDto.getContent().setContent("服务站装修效果图审批已经通过，请开始准备线下的门店装修。如果装修已经完成，点击此处可以上传装修完工图。");
        appMsgPushInfoDto.getContent().setRouteUrl(diamondConfiguredProperties.getDecorateFeedbackRouteUrl());
        pushStationDecorateAuditMessage(appMsgPushInfoDto);
    }

    @Override
    public void pushStationDecorateDesignNotPassMessage(Long taobaoUserId,String auditOption) {
        AppMsgPushInfoDto appMsgPushInfoDto = buildAppMsgPushInfoDto(taobaoUserId);
        appMsgPushInfoDto.getContent().setContent("服务站装修效果图审批不通过，审批不通过的原因："+auditOption+"，请到爱村淘后台重新上传效果图。");
        pushStationDecorateAuditMessage(appMsgPushInfoDto);
    }

    @Override
    public void pushStationDecorateFeedBackPassMessage(Long taobaoUserId) {
        AppMsgPushInfoDto appMsgPushInfoDto = buildAppMsgPushInfoDto(taobaoUserId);
        appMsgPushInfoDto.getContent().setContent("服务站完工图审批已经通过，预祝开业生意兴隆！");
        pushStationDecorateAuditMessage(appMsgPushInfoDto);
    }

    @Override
    public void pushStationDecorateFeedBackNotPassMessage(Long taobaoUserId,String auditOption) {
        AppMsgPushInfoDto appMsgPushInfoDto = buildAppMsgPushInfoDto(taobaoUserId);
        appMsgPushInfoDto.getContent().setContent("服务站完工图审批不通过，审批不通过的原因："+auditOption+"，点击此处重新上传完工图");
        appMsgPushInfoDto.getContent().setRouteUrl(diamondConfiguredProperties.getDecorateFeedbackRouteUrl());
        pushStationDecorateAuditMessage(appMsgPushInfoDto);
    }

    private AppMsgPushInfoDto buildAppMsgPushInfoDto(Long taobaoUserId){
        AppMsgPushInfoDto infoDto = new AppMsgPushInfoDto();
        infoDto.setAppId("cunpartner");
        infoDto.setSender(0L);
        infoDto.setReceivers(Lists.newArrayList(taobaoUserId.toString()));
        infoDto.setReceiverType(AppMsgPushInfoDto.RECEIVER_TYPE_TAOBAO_ID);
        infoDto.setMsgType("cunpartnerSystem");
        infoDto.setMsgTypeDetail("StationDecorateAudit");
        infoDto.setAction("all");

        AppMsgPushInfoDto.AppMsgPushContent content = infoDto.new AppMsgPushContent();
        content.setBizId( System.currentTimeMillis());
        content.setPublishTime(System.currentTimeMillis());
        content.setTitle("装修审核消息提示");//推送及消息的标题
        infoDto.setContent(content);
        return infoDto;
    }



    private void pushStationDecorateAuditMessage(AppMsgPushInfoDto appMsgPushInfoDto){
        try {
            EventDispatcherUtil.dispatch("CUN_PARTNER_DECORATE_AUDIT_MSG_PUSH", JSON.toJSONString(appMsgPushInfoDto));
        } catch (Exception e) {
            logger.error("pushStationDecorateDesignPassMessage,{taobaoUserId},{bizId}",appMsgPushInfoDto.getReceivers(),appMsgPushInfoDto.getContent().getBizId() ,e);
        }
    }
}
