package com.taobao.cun.auge.station.service.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.taobao.cun.auge.dal.domain.AppResource;
import com.taobao.cun.auge.station.bo.AppResourceBO;
import com.taobao.cun.auge.station.dto.LevelExamConfigurationDto;
import com.taobao.cun.auge.station.service.LevelExamManageService;

public class LevelExamManageServiceImpl implements LevelExamManageService {

    private static final Logger logger = LoggerFactory.getLogger(LevelExamManageServiceImpl.class);

    private static final LevelExamConfigurationDto EMPTY_CONFIG_OBJECT = new LevelExamConfigurationDto(); 
    private static final String LEVEL_EXAM_CONFIG = "level_exam_config";
    private static final String LEVEL_EXAM_KEY = "level_to_exam";
    
    @Autowired
    private AppResourceBO appResourceBO;

    @Override
    public boolean configure(LevelExamConfigurationDto configurationDto, String configurePerson) {
        if(configurationDto == null || configurationDto.getLevelExamMap() == null || configurePerson == null){
            logger.error("invalid configuration, dto:{}, configurePerson:{}", configurationDto, configurePerson);
            return false;
        }
        String value = JSON.toJSONString(configurationDto.getLevelExamMap());
        return appResourceBO.configAppResource(LEVEL_EXAM_CONFIG, LEVEL_EXAM_KEY, value, configurationDto.isDeleted(), configurePerson);
    }

    @Override
    public LevelExamConfigurationDto queryConfigure() {
        AppResource appResource =  appResourceBO.queryAppResource(LEVEL_EXAM_CONFIG, LEVEL_EXAM_KEY);
        if(appResource==null || StringUtils.isBlank(appResource.getValue())){
            return EMPTY_CONFIG_OBJECT; 
        }
        Map<String,Long> levelExamMap =  JSON.parseObject(appResource.getValue(), new TypeReference<Map<String, Long>>(){});
        return new LevelExamConfigurationDto().setLevelExamMap(levelExamMap);
    }
}
