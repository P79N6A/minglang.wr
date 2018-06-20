package com.taobao.cun.auge.station.service;

public interface PartnerAdzoneService {
    /**
     * 创建阿里妈妈拉新PID
     *
     * @param taobaoUserId
     * @return
     */
    public String createAdzone(Long taobaoUserId);

    /**
     * 获取阿里妈妈PID
     *
     * @param taobaoUserId 必选
     * @param stationId    可为空，默认使用当前绑定的村淘站点ID
     * @return
     */
    public String getUnionPid(Long taobaoUserId, Long stationId);
}
