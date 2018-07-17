package com.taobao.cun.auge.fence.dto;

import java.util.List;

import com.taobao.cun.auge.common.OperatorDto;

/**
 * Created by xiao on 18/7/17.
 */
public class FenceTemplateOpDto extends OperatorDto {

    /**
     * 批量覆盖(关闭站点原有相同类型围栏)
     */
    public static final String BATCH_OVERRIDE = "BATCH_OVERRIDE";

    /**
     * 批量叠加(保留站点原围栏)
     */
    public static final String BATCH_NEW = "BATCH_NEW";

    /**
     * 批量解除站点及围栏关系
     */
    public static final String BATCH_DELETE = "BATCH_DELETE";

    protected List<Long> templateIdList;

    protected String opType;

    public List<Long> getTemplateIdList() {
        return templateIdList;
    }

    public void setTemplateIdList(List<Long> templateIdList) {
        this.templateIdList = templateIdList;
    }

    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }

}
