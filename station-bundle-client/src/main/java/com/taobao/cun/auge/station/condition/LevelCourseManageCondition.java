package com.taobao.cun.auge.station.condition;

import com.taobao.cun.auge.station.enums.LevelCourseTypeEnum;
import org.apache.commons.lang3.StringUtils;

/**
 * 内部课程管理查询对象
 * @author xujianhui 2016年9月30日 上午10:17:48
 */
public class LevelCourseManageCondition extends LevelCourseCondition {

    private static final long     serialVersionUID = -1739785588189407608L;

    private String                courseName;
    private String                courseCode;
    /**
     * 课程类型
     */
    private LevelCourseTypeEnum courseType;

    
    
    public String getCourseName() {
        return courseName;
    }

    public LevelCourseManageCondition setCourseName(String courseName) {
        this.courseName = courseName;
        return this;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public LevelCourseManageCondition setCourseCode(String courseCode) {
        this.courseCode = courseCode;
        return this;
    }

    public LevelCourseTypeEnum getCourseType() {
        return courseType;
    }

    public void setCourseType(LevelCourseTypeEnum courseType) {
        this.courseType = courseType;
    }

    public boolean isSearchByLevel(){
        return StringUtils.isEmpty(courseName) 
                && StringUtils.isEmpty(courseCode) 
                && StringUtils.isEmpty(tag)
                && StringUtils.isNotBlank(userLevel);
    }
    
    public static boolean isValidManageCondition(LevelCourseManageCondition condition){
        return condition!=null && 
                !(StringUtils.isBlank(condition.userLevel) 
                        && StringUtils.isBlank(condition.tag)
                        && StringUtils.isEmpty(condition.courseCode)
                        && StringUtils.isEmpty(condition.courseName));
    }
}
