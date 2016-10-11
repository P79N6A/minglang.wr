package com.taobao.cun.auge.station.service;

import javax.validation.constraints.NotNull;

import com.taobao.cun.auge.station.dto.LevelExamConfigurationDto;

public interface LevelExamManageService {

    /**
     * 保存层级需要的考试,此方法也可以清空配置只需传一个空对象(非Null)
     */
    boolean configure(@NotNull LevelExamConfigurationDto configurationDto, @NotNull String confiurePerson);
    
    /**
     * 查询层级考试配置
     */
    LevelExamConfigurationDto queryConfigure();
    
}
