package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class LevelCourseEditDto implements Serializable {

    private static final long serialVersionUID = -7714014267792112685L;

    private Date    gmtCreate;
    /**
     * 课程名称(运营小二填写的，不是培训课程本身的)
     */
    private String            courseName;

    /**
     * 课程code
     */
    private String            courseCode;

    /**
     * 该课程必修层级(@com.taobao.cun.auge.station.enums.PartnerInstanceLevelEnum.PartnerInstanceLevel)
     */
    private List<String>      requiredLevels;

    /**
     * 课程选修层级(@com.taobao.cun.auge.station.enums.PartnerInstanceLevelEnum.PartnerInstanceLevel)
     */
    private List<String>      electiveLevels;

    private String            growthIndex;

    public LevelCourseEditDto(){}
    
    public LevelCourseEditDto(String courseName, String courseCode, String growthIndex) {
        super();
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.growthIndex = growthIndex;
    }

    public LevelCourseEditDto(String courseName, String courseCode, List<String> requiredLevels,  List<String> electiveLevels) {
        super();
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.requiredLevels = requiredLevels;
        this.electiveLevels = electiveLevels;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public List<String> getRequiredLevels() {
        return requiredLevels;
    }

    public void setRequiredLevels(List<String> requiredLevels) {
        this.requiredLevels = requiredLevels;
    }

    public List<String> getElectiveLevels() {
        return electiveLevels;
    }

    public void setElectiveLevels(List<String> electiveLevels) {
        this.electiveLevels = electiveLevels;
    }

    public String getGrowthIndex() {
        return growthIndex;
    }
    
    public void setGrowthIndex(String growthIndex) {
        this.growthIndex = growthIndex;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((courseCode == null) ? 0 : courseCode.hashCode());
        result = prime * result + ((courseName == null) ? 0 : courseName.hashCode());
        result = prime * result + ((growthIndex == null) ? 0 : growthIndex.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        LevelCourseEditDto other = (LevelCourseEditDto) obj;
        if (courseCode == null) {
            if (other.courseCode != null) return false;
        } else if (!courseCode.equals(other.courseCode)) return false;
        if (courseName == null) {
            if (other.courseName != null) return false;
        } else if (!courseName.equals(other.courseName)) return false;
        if (growthIndex == null) {
            if (other.growthIndex != null) return false;
        } else if (!growthIndex.equals(other.growthIndex)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "LevelCourseEditDto [courseName=" + courseName + ", courseCode=" + courseCode + ", requiredLevels="
               + requiredLevels + ", electiveLevels=" + electiveLevels + ", tag=" + growthIndex + "]";
    }

}
