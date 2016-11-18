package com.taobao.cun.auge.station.service.interfaces;

import com.alibaba.fastjson.JSONObject;

/**
 * 层级审批消息发送和监听处理服务
 * 类LevelApproveMessageHandleService.java的实现描述：TODO 类实现描述 
 * @author xujianhui 2016年11月17日 下午2:35:00
 */
public interface LevelAuditMessageService {

    /**
     * 处理内外层级审批通过的消息
     * @param partnerInstanceLevelDto:内外消息反序列化出来的
     */
    void handleApprove(JSONObject ob);
    
    /**
     * 处理内外层级审批不通过的消息
     * @param partnerInstanceLevelDto:内外消息反序列化出来的
     */
    void handleRefuse(JSONObject ob);
}
