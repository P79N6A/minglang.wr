package com.taobao.cun.auge.station.bo;

import com.taobao.cun.auge.station.dto.PartnerAdzoneInfoDto;

public interface PartnerAdzoneBO {
    String getUnionPid(Long taobaoUserId, Long stationId);

    PartnerAdzoneInfoDto getPartnerAdzoneInfoByPid(String pid);

    void addAdzone(Long taobaoUserId, Long stationId, String pid);
}
