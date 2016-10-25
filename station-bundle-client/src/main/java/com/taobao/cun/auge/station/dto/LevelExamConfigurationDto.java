package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.taobao.cun.auge.station.enums.PartnerInstanceLevelEnum.PartnerInstanceLevel;

public class LevelExamConfigurationDto implements Serializable {
    
    private static final long serialVersionUID = 5978697568719760679L;
    /**
     * 是否打开开关 晋升时必须通过考试
     */
    private boolean isOpenEvaluate;
    /**
     * 是否分发晋升试卷
     */
    private boolean isDispatch;
    private Map<String, Long> levelExamPaperIdMap = new HashMap<String, Long>();
    
    public LevelExamConfigurationDto(){super();}
    
    public LevelExamConfigurationDto(boolean isOpenEvaluate, boolean isDispatch) {
        super();
        this.isOpenEvaluate = isOpenEvaluate;
        this.isDispatch = isDispatch;
    }

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
    
    public boolean isOpenEvaluate() {
        return isOpenEvaluate;
    }

    public LevelExamConfigurationDto setOpenEvaluate(boolean isOpenEvaluate) {
        this.isOpenEvaluate = isOpenEvaluate;
        return this;
    }
    
    public boolean isDispatch() {
        return isDispatch;
    }

    public LevelExamConfigurationDto setDispatch(boolean isDispatch) {
        this.isDispatch = isDispatch;
        return this;
    }

    @Override
    public String toString() {
        return "LevelExamConfigurationDto [isOpenEvaluate=" + isOpenEvaluate + ", isDispatch=" + isDispatch
               + ", levelExamPaperIdMap=" + levelExamPaperIdMap + "]";
    }

}
