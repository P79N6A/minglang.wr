package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

public class LevelCourseLearningStatisticsDto implements Serializable {

    private static final long serialVersionUID = 7260272084970323348L;

    private int               requiredCourseCount;

    private int               electiveCourseCount;

    private int               learnedRequiredCount;

    private int               learnedElectiveCount;

    public LevelCourseLearningStatisticsDto(){}
    
    public LevelCourseLearningStatisticsDto(int rcc, int ecc, int lrc, int lec) {
        this.requiredCourseCount = rcc;
        this.electiveCourseCount = ecc;
        this.learnedRequiredCount = lrc;
        this.learnedElectiveCount = lec;
    }
    
    public int getRequiredCourseCount() {
        return requiredCourseCount;
    }

    public int getElectiveCourseCount() {
        return electiveCourseCount;
    }

    public int getLearnedRequiredCount() {
        return learnedRequiredCount;
    }

    public int getLearnedElectiveCount() {
        return learnedElectiveCount;
    }

    public int getUnlearnedRequiredCount(){
        return requiredCourseCount - learnedRequiredCount;
    }
    
    public int getUnlearnedElectiveCount(){
        return electiveCourseCount - learnedElectiveCount;
    }

    @Override
    public String toString() {
        return "LevelCourseLearningStatisticsDto [requiredCourseCount=" + requiredCourseCount + ", electiveCourseCount="
               + electiveCourseCount + ", learnedRequiredCount=" + learnedRequiredCount + ", learnedElectiveCount="
               + learnedElectiveCount + "]";
    }
    
}
