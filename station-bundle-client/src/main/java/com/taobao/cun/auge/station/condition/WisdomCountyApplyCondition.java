package com.taobao.cun.auge.station.condition;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.PageQuery;
import com.taobao.cun.auge.station.enums.WisdomCountyStateEnum;

import java.io.Serializable;

/**
 * Created by xiao on 16/10/18.
 */
public class WisdomCountyApplyCondition extends PageQuery {

    private static final long serialVersionUID = -290307014766266472L;

    private WisdomCountyStateEnum state;

    private String orgIdPath;

    public String getOrgIdPath() {
        return orgIdPath;
    }

    public void setOrgIdPath(String orgIdPath) {
        this.orgIdPath = orgIdPath;
    }

    public WisdomCountyStateEnum getState() {
        return state;
    }

    public void setState(WisdomCountyStateEnum state) {
        this.state = state;
    }
}
