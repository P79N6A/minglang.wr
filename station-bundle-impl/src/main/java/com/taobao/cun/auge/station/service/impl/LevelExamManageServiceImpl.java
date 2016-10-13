package com.taobao.cun.auge.station.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
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
        LevelConfiguration configuration = new LevelConfiguration(configurationDto.isOpenEvaluate(), configurationDto.isDispatch(), configurationDto.getLevelExamMap());
        String value = JSON.toJSONString(configuration);
        return appResourceBO.configAppResource(LEVEL_EXAM_CONFIG, LEVEL_EXAM_KEY, value, false, configurePerson);
    }

    @Override
    public LevelExamConfigurationDto queryConfigure() {
        AppResource appResource =  appResourceBO.queryAppResource(LEVEL_EXAM_CONFIG, LEVEL_EXAM_KEY);
        if(appResource==null || StringUtils.isBlank(appResource.getValue())){
            return EMPTY_CONFIG_OBJECT; 
        }
        LevelConfiguration configuration =  JSON.parseObject(appResource.getValue(), LevelConfiguration.class);
        return new LevelExamConfigurationDto().setLevelExamMap(configuration.getLevelExamPaperIdMap())
                .setDispatch(configuration.isDispatch)
                .setOpenEvaluate(configuration.isOpenEvaluate());
    }
    
    public static class LevelConfiguration implements Serializable {

        private static final long serialVersionUID = 5209263287154537277L;
        /**
         * 是否打开开关 晋升时必须通过考试
         */
        private boolean isOpenEvaluate;
        /**
         * 是否分发晋升试卷
         */
        private boolean isDispatch;
        private Map<String, Long> levelExamPaperIdMap = new HashMap<String, Long>();
        
        public LevelConfiguration(){};
        
        public LevelConfiguration(boolean isOpenEvaluate, boolean isDispatch, Map<String, Long> levelExamPaperIdMap) {
            super();
            this.isOpenEvaluate = isOpenEvaluate;
            this.isDispatch = isDispatch;
            this.levelExamPaperIdMap = levelExamPaperIdMap;
        }

        public boolean isOpenEvaluate() {
            return isOpenEvaluate;
        }
        
        public void setOpenEvaluate(boolean isOpenEvaluate) {
            this.isOpenEvaluate = isOpenEvaluate;
        }
        
        public boolean isDispatch() {
            return isDispatch;
        }
        
        public void setDispatch(boolean isDispatch) {
            this.isDispatch = isDispatch;
        }
        
        public Map<String, Long> getLevelExamPaperIdMap() {
            return levelExamPaperIdMap;
        }
        
        public void setLevelExamPaperIdMap(Map<String, Long> levelExamPaperIdMap) {
            this.levelExamPaperIdMap = levelExamPaperIdMap;
        }
        
    }
}
