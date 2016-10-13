package com.taobao.cun.auge.station.service;

import javax.validation.constraints.NotNull;

import com.taobao.cun.auge.station.dto.LevelExamConfigurationDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceLevelEnum.PartnerInstanceLevel;

public interface LevelExamManageService {

    /**
     * 保存层级需要的考试,此方法也可以清空配置只需传一个空对象(非Null)
     */
    boolean configure(@NotNull LevelExamConfigurationDto configurationDto, @NotNull String confiurePerson);
    
    /**
     * 查询层级考试配置
     */
    LevelExamConfigurationDto queryConfigure();
    
    /**
     * 根据当前合伙人层级,进行试卷分发,针对taobaoUserId+level 试卷分发满足幂等性要求
     * @param taobaoUserId
     * @param nickName
     * @param leve:当前预估层级:从鹊桥读取该合伙人当前指标数据计算而来
     * @return
     */
    public boolean dispatchExamPaper(Long taobaoUserId, String nickName, PartnerInstanceLevel leve);
    
}
