package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.station.dto.LevelExamingResult;
import com.taobao.cun.auge.station.enums.PartnerInstanceLevelEnum.PartnerInstanceLevel;

public interface LevelExamQueryService {

    /**
     * 合伙人是否通过当前level包括该level以下的所有分发的晋升考试
     */
    public LevelExamingResult queryLevelExamResult(Long taobaoUserId, PartnerInstanceLevel to);
}
