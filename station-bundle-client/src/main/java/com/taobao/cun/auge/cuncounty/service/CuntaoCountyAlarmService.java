package com.taobao.cun.auge.cuncounty.service;

/**
 * 县点预警（协议等）
 */
public interface CuntaoCountyAlarmService {
    /**
     * 预警
     */
    void protocolAlarm();

    /**
     * 拜访预警
     */
    void visitAlarm();

    void test(Long bizId, String textContent, String msgType);
}
