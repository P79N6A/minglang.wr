package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.station.dto.NewuserOrderInitRequest;
import com.taobao.cun.auge.station.dto.NewuserOrderInitResponse;
import com.taobao.cun.auge.station.dto.PartnerAdzoneInfoDto;

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

    /**
     * 根据阿里妈妈PID获取村淘信息
     *
     * @param pid
     * @return
     */
    public PartnerAdzoneInfoDto getPartnerAdzoneInfoByPid(String pid);

    /**
     * 分页初始化拉新订单数据
     * @param request
     * @return
     */
    public NewuserOrderInitResponse initNewUserOrder(NewuserOrderInitRequest request);

    public void initAllNewUserOrder(NewuserOrderInitRequest request);

    public void deleteNewuserOrder(String activityId, String statDate);


}
