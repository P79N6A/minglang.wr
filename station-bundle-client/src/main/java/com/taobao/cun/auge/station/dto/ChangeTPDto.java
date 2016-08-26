package com.taobao.cun.auge.station.dto;

import com.taobao.cun.auge.common.OperatorDto;

import java.io.Serializable;

/**
 * Created by xiao on 16/8/25.
 */
public class ChangeTPDto extends OperatorDto implements Serializable {

    private static final long serialVersionUID = -36421095928748724L;

    private Long id;

    private Long newParentStationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNewParentStationId() {
        return newParentStationId;
    }

    public void setNewParentStationId(Long newParentStationId) {
        this.newParentStationId = newParentStationId;
    }
}
