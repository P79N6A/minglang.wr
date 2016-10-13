package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.station.enums.PartnerInstanceLevelEnum.PartnerInstanceLevel;

public interface LevelExamResultQueryService {

    /**
     * 合伙人是否通过当前level包括该level以下的所有晋升考试
     * @param taobaoUserId
     * @param nickName
     * @param leve
     * @return
     */
    public boolean isPassAllLevelExam(Long taobaoUserId, PartnerInstanceLevel leve);
}
