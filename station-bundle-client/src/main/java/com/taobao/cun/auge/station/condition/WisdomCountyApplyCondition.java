package com.taobao.cun.auge.station.condition;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.PageQuery;
import com.taobao.cun.auge.station.enums.WisdomCountyStateEnum;

import java.io.Serializable;

/**
 * Created by xiao on 16/10/18.
 */
public class WisdomCountyApplyCondition extends OperatorDto implements Serializable {

    private static final long serialVersionUID = -290307014766266472L;

    private WisdomCountyStateEnum state;

    private String orgIdPath;

    private Integer pageStart; // 分页参数

    private Integer pageSize;

    public String getOrgIdPath() {
        return orgIdPath;
    }

    public void setOrgIdPath(String orgIdPath) {
        this.orgIdPath = orgIdPath;
    }

    public Integer getPageStart() {
        return pageStart;
    }

    public void setPageStart(Integer pageStart) {
        this.pageStart = pageStart;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public WisdomCountyStateEnum getState() {
        return state;
    }

    public void setState(WisdomCountyStateEnum state) {
        this.state = state;
    }
}
