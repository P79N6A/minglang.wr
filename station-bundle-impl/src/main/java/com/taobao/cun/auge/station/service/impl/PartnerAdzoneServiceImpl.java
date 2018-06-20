package com.taobao.cun.auge.station.service.impl;

import com.taobao.cun.auge.station.dto.PartnerAdzoneInfoDto;
import com.taobao.cun.auge.station.service.PartnerAdzoneService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@HSFProvider(serviceInterface = PartnerAdzoneService.class)
public class PartnerAdzoneServiceImpl implements  PartnerAdzoneService{
    @Override
    public String createAdzone(Long taobaoUserId) {
        return null;
    }

    @Override
    public String getUnionPid(Long taobaoUserId, Long stationId) {
        return null;
    }

    @Override
    public PartnerAdzoneInfoDto getPartnerAdzoneInfoByPid(String pid) {
        return null;
    }
}
