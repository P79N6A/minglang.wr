package com.taobao.cun.auge.station.service;

/**
 * 提供给sop的专属接口
 * 将查询权限通过SOP前置到咨询端，以提升小二的解答能力，保障客户体验。
 */
public interface PartnerSopService {

    /**
     * 通过淘宝账号 查询 入驻状态，村点状态，县负责人等信息。
     * @param taobaoUserId
     * @return
     */
    public String getPartnerInfo(Long taobaoUserId);
}
