package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.station.dto.LevelExamingResult;
import com.taobao.cun.auge.station.enums.PartnerInstanceLevelEnum;

public interface LevelExamQueryService {

    /**
     * 合伙人是否通过当前level包括该level以下的所有分发的晋升考试
     * @param taobaoUserId
     * @param to 是枚举com.taobao.cun.auge.station.enums.PartnerInstanceLevelEnum.PartnerInstanceLevel 的name
     * @return
     */
    public LevelExamingResult queryLevelExamResult(Long taobaoUserId, String to);
    
    /**
     * 考试只影响升级的情形,具体逻辑是:对应层级分配了考试而且考试没通过则表示不能升级到该级别
     * 注意:如果没有给给某个level分配试卷则默认表示通过该级别
     * @param taobaoUserId
     * @param preLevel 当前级
     * @param newCurrentLevel 新评定级别
     * @return
     */
    public PartnerInstanceLevelEnum checkEvaluateLevelByExamResult(Long taobaoUserId, PartnerInstanceLevelEnum preLevel, PartnerInstanceLevelEnum newCurrentLevel);
    
    /**
     * 是否打开了考试评级的控制
     * @return
     */
    public boolean isOpenEvaluateCheckExamPass();
}
