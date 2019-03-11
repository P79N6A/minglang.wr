package com.taobao.cun.auge.station.um.dto;

import com.taobao.cun.auge.common.OperatorDto;

import javax.validation.constraints.NotNull;

/**
 * 批量退出优盟dto
 *
 * @author haihu.fhh
 */
public class BatchQuitUnionMemberDto extends OperatorDto {

    private static final long serialVersionUID = 8398779567570453647L;

    @NotNull(message = "parentStationId not null")
    private Long parentStationId;

    public Long getParentStationId() {
        return parentStationId;
    }

    public void setParentStationId(Long parentStationId) {
        this.parentStationId = parentStationId;
    }
}