package com.taobao.cun.auge.station.condition;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;

/**
 * 提供给外部查询课程query对象
 * @author xujianhui 2016年9月29日 下午4:10:24
 */
public class LevelCourseCondition implements Serializable {

    private static final long   serialVersionUID = -2099914015955210345L;

    /**
     * 合伙人userId
     */
    @NotNull
    protected Long                userId;

    /**
     * 合伙人层级 com.taobao.cun.auge.station.enums.PartnerInstanceLevelEnum.PartnerInstanceLevel
     */
    @NotNull
    protected String              userLevel;

    /**
     * 课程分类查找,注意课程分类和层级两个查询维度都设置的话是取并集而不是交集.
     */
    protected String              tag;
    
    /**
     * 指标分类展示前多少个课程
     */
    protected int                    groupCount = 3;

    /**
     * 按照用户层级查询课程
     */
    public static LevelCourseCondition getUserLevelQueryCondition(Long userId, String userLevel){
        LevelCourseCondition lcc = new LevelCourseCondition();
        lcc.userLevel = userLevel;
        lcc.userId = userId;
        return lcc;
    }
    
    public Long getUserId() {
        return userId;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public String getTag() {
        return tag;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }
    
    public LevelCourseCondition setTag(String tag) {
        this.tag = tag;
        return this;
    }
    
    public int getGroupCount() {
        return groupCount;
    }
    
    public LevelCourseCondition setGroupCount(int groupCount) {
        this.groupCount = groupCount;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
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

    /**
     * condtion不为空且usrId userLevel tag不全为空
     */
    public static boolean isValid(LevelCourseCondition condition){
        return condition!=null && null != condition.userId && StringUtils.isNotBlank(condition.userLevel);
    }
    
    @Override
    public String toString() {
        return "LevelCourseCondition [userId=" + userId + ", userLevel=" + userLevel + ", tag=" + tag +  "]";
    }

}
