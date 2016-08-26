package com.taobao.cun.auge.event;

import com.taobao.cun.auge.common.OperatorDto;

/**
 * Created by xiao on 16/8/25.
 */
public class ChangeTPEvent extends OperatorDto {

    private static final long serialVersionUID = 8910764076222371621L;

    private Long stationId;

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }
}
