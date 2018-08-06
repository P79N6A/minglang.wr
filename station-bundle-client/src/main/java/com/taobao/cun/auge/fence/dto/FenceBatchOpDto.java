package com.taobao.cun.auge.fence.dto;

import java.io.Serializable;

/**
 * Created by xiao on 18/7/4.
 */
public class FenceBatchOpDto extends FenceTemplateOpDto implements Serializable {

    private static final long serialVersionUID = -3463099685148507292L;

    private String condition;

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

}
