package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

public class NewuserOrderInitRequest implements Serializable{

    private static final long serialVersionUID = 726204948700703546L;
    private Long pageNo;
    private Long pageSize;
    /**
     * 联盟活动id
     */
    private String activityId;
    /**
     * 统计月份
     */
    private String statDate;

    public Long getPageNo() {
        return pageNo;
    }

    public void setPageNo(Long pageNo) {
        this.pageNo = pageNo;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getStatDate() {
        return statDate;
    }

    public void setStatDate(String statDate) {
        this.statDate = statDate;
    }
}
