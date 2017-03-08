package com.taobao.cun.auge.station.enums;

public enum LevelCourseTypeEnum {
    REQUIRED,
    ELECTIVE;
    
    public static boolean isRequiredCourse(String name){
        return REQUIRED.name().equals(name);
    }
    
    public static boolean isElectiveCourse(String name){
        return ELECTIVE.name().equals(name);
    }
}
