package com.taobao.cun.auge.station.um.dto;

import javax.validation.constraints.NotNull;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.um.enums.UnionMemberStateChangeEnum;

/**
 * 优盟状态变更dto
 *
 * @author haihu.fhh
 */
public class UnionMemberStateChangeDto extends OperatorDto {

    private static final long serialVersionUID = -899239225687161497L;

    @NotNull(message = "stationId not null")
    private Long stationId;

    @NotNull(message = "stateChange not null")
    private UnionMemberStateChangeEnum stateChange;

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public UnionMemberStateChangeEnum getStateChange() {
        return stateChange;
    }

    public void setStateChange(UnionMemberStateChangeEnum stateChange) {
        this.stateChange = stateChange;
    }
}
