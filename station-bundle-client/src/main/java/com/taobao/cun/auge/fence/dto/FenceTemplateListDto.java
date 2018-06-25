package com.taobao.cun.auge.fence.dto;

import java.io.Serializable;
import java.util.Date;

import com.taobao.cun.auge.fence.enums.FenceTypeEnum;

/**
 * Created by xiao on 18/6/15.
 */
public class FenceTemplateListDto extends FenceTemplateDto implements Serializable {

    private static final long serialVersionUID = 4684664316899136843L;

    /**
     * 应用实体数量
     */
    private Integer entityCount;

    public Integer getEntityCount() {
        return entityCount;
    }

    public void setEntityCount(Integer entityCount) {
        this.entityCount = entityCount;
    }
}
