package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import com.taobao.cun.auge.station.enums.PartnerInstanceLevelEnum.PartnerInstanceLevel;

public class LevelExamConfigurationDto implements Serializable {
    
    private static final long serialVersionUID = 5978697568719760679L;
    private Map<String, Long> levelExamPaperIdMap = new TreeMap<String, Long>();
    
    public LevelExamConfigurationDto(){super();}
    
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
        if(level == null){
            return 0L;
        }
        return levelExamPaperIdMap.get(level.name());
    }

    public Map<String, Long> getLevelExamMap(){
        return levelExamPaperIdMap;
    }
    
    public LevelExamConfigurationDto setLevelExamMap(Map<String, Long>configMap){
        this.levelExamPaperIdMap = configMap;
        return this;
    }
    
    @Override
    public String toString() {
        return ", levelExamPaperIdMap=" + levelExamPaperIdMap + "]";
    }

}
