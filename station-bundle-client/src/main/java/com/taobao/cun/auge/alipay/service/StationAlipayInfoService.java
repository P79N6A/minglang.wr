package com.taobao.cun.auge.alipay.service;

import com.taobao.cun.auge.alipay.dto.StationAlipayInfoDto;

import java.util.Map;

public interface StationAlipayInfoService {


    public StationAlipayInfoDto getStationAlipayInfoByTaobaoUserId(String taobaoUserId);

    public Long saveStationAlipayInfo(StationAlipayInfoDto stationAlipayInfoDto);

    public void updateStationAlipayInfo(StationAlipayInfoDto stationAlipayInfoDto);

    public String dealZftMessage(Map<String,String> params);


}
