package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.station.dto.LevelExamingResult;

public interface LevelExamQueryService {

    /**
     * 合伙人是否通过当前level包括该level以下的所有分发的晋升考试
     * @param taobaoUserId
     * @param to 是枚举com.taobao.cun.auge.station.enums.PartnerInstanceLevelEnum.PartnerInstanceLevel 的name
     * @return
     */
    public LevelExamingResult queryLevelExamResult(Long taobaoUserId, String to);
}
