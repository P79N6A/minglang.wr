package com.taobao.cun.auge.fence.dto;

import com.taobao.cun.auge.common.PageQuery;

/**
 * Created by xiao on 18/6/15.
 */
public class FenceTemplateQueryCondition extends PageQuery {

    private static final long serialVersionUID = -4255912219469186362L;

    private String name;

    private String type;

    private String limitCommodity;

    private String status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLimitCommodity() {
        return limitCommodity;
    }

    public void setLimitCommodity(String limitCommodity) {
        this.limitCommodity = limitCommodity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
