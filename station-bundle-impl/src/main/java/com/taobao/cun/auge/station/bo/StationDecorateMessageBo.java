package com.taobao.cun.auge.station.bo;

/**
 * @Description:
 * @author: qingwu.yhp
 * @Date: 2018-12-17 10:42
 */
public interface StationDecorateMessageBo {

    /**
     * 装修设计审核通过  推送消息给店掌柜
     */
    public void pushStationDecorateDesignPassMessage(Long taobaoUserId);

    /**
     * 装修设计审核未通过  推送消息给店掌柜
     */
    public void pushStationDecorateDesignNotPassMessage(Long taobaoUserId, String auditOption);

    /**
     * 装修反馈审核通过  推送消息给店掌柜
     */
    public void pushStationDecorateFeedBackPassMessage(Long taobaoUserId);

    /**
     * 装修反馈审核未通过  推送消息给店掌柜
     */
    public void pushStationDecorateFeedBackNotPassMessage(Long taobaoUserId, String auditOption);
}
