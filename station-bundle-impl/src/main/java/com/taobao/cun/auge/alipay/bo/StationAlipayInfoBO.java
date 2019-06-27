package com.taobao.cun.auge.alipay.bo;

import com.taobao.cun.auge.dal.domain.StationAlipayInfo;

public interface StationAlipayInfoBO {


    public StationAlipayInfo getStationAlipayInfo(String taobaoUserId);

    public StationAlipayInfo getCountyStationById(Long id);

    public Long addStationAlipayInfo(StationAlipayInfo stationAlipayInfo);

    void updateStationAlipayInfo(StationAlipayInfo stationAlipayInfo);

}
