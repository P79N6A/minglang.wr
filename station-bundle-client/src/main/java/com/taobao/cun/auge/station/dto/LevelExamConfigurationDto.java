package com.taobao.cun.auge.station.dto;

import java.util.HashMap;
import java.util.Map;

import com.taobao.cun.auge.station.enums.PartnerInstanceLevelEnum.PartnerInstanceLevel;

public class LevelExamConfigurationDto {
    
    private boolean isDeleted;
    private Map<String, Long> levelExamPaperIdMap = new HashMap<String, Long>();
    
    public LevelExamConfigurationDto configureS4(Long examPaperId){
        levelExamPaperIdMap.put(PartnerInstanceLevel.S4.name(), examPaperId);
        return this;
    }
    
    public LevelExamConfigurationDto configureS5(Long examPaperId){
        levelExamPaperIdMap.put(PartnerInstanceLevel.S5.name(), examPaperId);
        return this;
    }
    
    public LevelExamConfigurationDto configureS6(Long examPaperId){
        levelExamPaperIdMap.put(PartnerInstanceLevel.S6.name(), examPaperId);
        return this;
    }
    
    public LevelExamConfigurationDto configureS7(Long examPaperId){
        levelExamPaperIdMap.put(PartnerInstanceLevel.S7.name(), examPaperId);
        return this;
    }
    
    public LevelExamConfigurationDto configureS8(Long examPaperId){
        levelExamPaperIdMap.put(PartnerInstanceLevel.S8.name(), examPaperId);
        return this;
    }
    
    public Long getExamPaperId(PartnerInstanceLevel level){
        return levelExamPaperIdMap.get(level);
    }

    public Map<String, Long> getLevelExamMap(){
        return levelExamPaperIdMap;
    }
    
    public LevelExamConfigurationDto setLevelExamMap(Map<String, Long>configMap){
        this.levelExamPaperIdMap = configMap;
        return this;
    }
    
    public boolean isDeleted() {
        return isDeleted;
    }

    public LevelExamConfigurationDto setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
        return this;
    }

    @Override
    public String toString() {
        return "LevelExamConfigurationDto [levelExamPaperIdMap=" + levelExamPaperIdMap + "]";
    }
    
}
