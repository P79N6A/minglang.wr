package com.taobao.cun.auge.station.um.dto;

import javax.validation.constraints.NotNull;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.um.enums.UnionMemberStateEnum;

/**
 * 优盟状态变更dto
 *
 * @author haihu.fhh
 */
public class UnionMemberStateChangeDto extends OperatorDto {

    private static final long serialVersionUID = -899239225687161497L;

    @NotNull(message = "stationId not null")
    private Long stationId;

    @NotNull(message = "state not null")
    private UnionMemberStateEnum state;

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public UnionMemberStateEnum getState() {
        return state;
    }

    public void setState(UnionMemberStateEnum state) {
        this.state = state;
    }
}
