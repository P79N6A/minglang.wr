package com.taobao.cun.auge.fence.dto;

import java.io.Serializable;

import com.taobao.cun.auge.common.OperatorDto;

/**
 * Created by xiao on 18/6/17.
 */
public class FenceTemplateStation extends OperatorDto implements Serializable {

    private static final long serialVersionUID = -3348782058127217645L;

    private Long stationId;

    private Long templateId;

    private String name;

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
