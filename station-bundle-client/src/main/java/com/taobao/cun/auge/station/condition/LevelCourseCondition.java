package com.taobao.cun.auge.station.condition;

import java.io.Serializable;

import com.taobao.cun.auge.station.enums.LevelCourseTypeEnum;

public class LevelCourseCondition implements Serializable {

    private static final long   serialVersionUID = -2099914015955210345L;

    /**
     * 合伙人userId
     */
    private Long                userId;

    /**
     * 合伙人层级
     */
    private String              userLevel;

    /**
     * 课程分类标示：如果按照tag查找，那么不要跟层级挂钩
     */
    private String              tag;

    /**
     * 课程类型
     */
    private LevelCourseTypeEnum courseType;

    /**
     * 按照指标查询课程
     */
    public static LevelCourseCondition getTagQueryCondition(Long userId, String tag){
        LevelCourseCondition lcc = new LevelCourseCondition();
        lcc.setTag(tag);
        lcc.setCourseType(LevelCourseTypeEnum.ELECTIVE);
        lcc.setUserId(userId);
        return lcc;
    }
    
    /**
     * 按照用户层级查询课程
     */
    public static LevelCourseCondition getUserCourseCondition(Long userId, String userLevel, LevelCourseTypeEnum courseType){
        LevelCourseCondition lcc = new LevelCourseCondition();
        lcc.setUserId(userId);
        lcc.setUserLevel(userLevel);
        lcc.setCourseType(courseType);
        return lcc;
    }
    
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public LevelCourseTypeEnum getCourseType() {
        return courseType;
    }

    public void setCourseType(LevelCourseTypeEnum courseType) {
        this.courseType = courseType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((courseType == null) ? 0 : courseType.hashCode());
        result = prime * result + ((tag == null) ? 0 : tag.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        result = prime * result + ((userLevel == null) ? 0 : userLevel.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        LevelCourseCondition other = (LevelCourseCondition) obj;
        if (courseType != other.courseType) return false;
        if (tag == null) {
            if (other.tag != null) return false;
        } else if (!tag.equals(other.tag)) return false;
        if (userId == null) {
            if (other.userId != null) return false;
        } else if (!userId.equals(other.userId)) return false;
        if (userLevel == null) {
            if (other.userLevel != null) return false;
        } else if (!userLevel.equals(other.userLevel)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "LevelCourseCondition [userId=" + userId + ", userLevel=" + userLevel + ", tag=" + tag + ", courseType="
               + courseType + "]";
    }

}
