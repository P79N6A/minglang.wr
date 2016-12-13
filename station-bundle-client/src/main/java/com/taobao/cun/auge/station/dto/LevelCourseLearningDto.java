package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

public class LevelCourseLearningDto implements Serializable {

    private static final long serialVersionUID = -8009110711371410226L;

    /**
     * 课程名称
     */
    private String            courseName;

    /**
     * 课程detail Url
     */
    private String            courseDetailUrl;

    /**
     * 课程所属分类
     */
    private String            growthIndex;

    /**
     * 课程类型：选修还是必修
     */
    private String            courseType;

    /**
     * 学习状态 已学习还是未学习
     */
    private String            status;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDetailUrl() {
        return courseDetailUrl;
    }

    public void setCourseDetailUrl(String courseDetailUrl) {
        this.courseDetailUrl = courseDetailUrl;
    }

    public String getGrowthIndex() {
        return growthIndex;
    }
    
    public void setGrowthIndex(String growthIndex) {
        this.growthIndex = growthIndex;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((courseDetailUrl == null) ? 0 : courseDetailUrl.hashCode());
        result = prime * result + ((courseName == null) ? 0 : courseName.hashCode());
        result = prime * result + ((courseType == null) ? 0 : courseType.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((growthIndex == null) ? 0 : growthIndex.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        LevelCourseLearningDto other = (LevelCourseLearningDto) obj;
        if (courseDetailUrl == null) {
            if (other.courseDetailUrl != null) return false;
        } else if (!courseDetailUrl.equals(other.courseDetailUrl)) return false;
        if (courseName == null) {
            if (other.courseName != null) return false;
        } else if (!courseName.equals(other.courseName)) return false;
        if (courseType == null) {
            if (other.courseType != null) return false;
        } else if (!courseType.equals(other.courseType)) return false;
        if (status == null) {
            if (other.status != null) return false;
        } else if (!status.equals(other.status)) return false;
        if (growthIndex == null) {
            if (other.growthIndex != null) return false;
        } else if (!growthIndex.equals(other.growthIndex)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "LevelCourseLearningDto [courseName=" + courseName + ", courseDetailUrl=" + courseDetailUrl + ", tag="
               + growthIndex + ", courseType=" + courseType + ", status=" + status + "]";
    }

}
