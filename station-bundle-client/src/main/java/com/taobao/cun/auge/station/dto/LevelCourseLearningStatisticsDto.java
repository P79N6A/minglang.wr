package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class LevelCourseLearningStatisticsDto implements Serializable {

    private static final long                         serialVersionUID = 7260272084970323348L;
    private static final LevelCourseLearningStatisticsDto nullDto = new LevelCourseLearningStatisticsDto();

    private Map<String, List<LevelCourseLearningDto>> growthIndexCourseMap;

    private int                                       requiredCourseToLearningCount;

    public LevelCourseLearningStatisticsDto(){}
    
    public LevelCourseLearningStatisticsDto(Map<String, List<LevelCourseLearningDto>> growthIndexCourseMap,  int requiredCourseToLearningCount) {
        super();
        this.growthIndexCourseMap = growthIndexCourseMap;
        this.requiredCourseToLearningCount = requiredCourseToLearningCount;
    }

    public Map<String, List<LevelCourseLearningDto>> getGrowthIndexCourseMap() {
        return growthIndexCourseMap;
    }

    public void setGrowthIndexCourseMap(Map<String, List<LevelCourseLearningDto>> growthIndexCourseMap) {
        this.growthIndexCourseMap = growthIndexCourseMap;
    }

    public int getRequiredCourseToLearningCount() {
        return requiredCourseToLearningCount;
    }

    public void setRequiredCourseToLearningCount(int requiredCourseToLearningCount) {
        this.requiredCourseToLearningCount = requiredCourseToLearningCount;
    }

    public static LevelCourseLearningStatisticsDto getNullDto(){
        return nullDto;
    }
    
}
