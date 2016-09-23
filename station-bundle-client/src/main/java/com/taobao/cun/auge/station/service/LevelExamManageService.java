package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.station.dto.LevelExamConfigurationDto;

public interface LevelExamManageService {

    /**
     * 保存层级需要的考试
     */
    boolean configure(LevelExamConfigurationDto configurationDto);
    
    /**
     * 查询层级考试配置
     */
    LevelExamConfigurationDto queryConfigure();
}
