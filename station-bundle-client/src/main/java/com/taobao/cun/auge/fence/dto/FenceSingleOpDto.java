package com.taobao.cun.auge.fence.dto;

/**
 * Created by xiao on 18/7/17.
 */
public class FenceSingleOpDto extends FenceTemplateOpDto {

    private static final long serialVersionUID = -4915828458013360440L;

    private Long stationId;

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }
}
