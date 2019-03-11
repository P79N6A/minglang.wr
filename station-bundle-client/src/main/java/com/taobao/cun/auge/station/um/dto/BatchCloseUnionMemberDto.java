package com.taobao.cun.auge.station.um.dto;

import com.taobao.cun.auge.common.OperatorDto;

import javax.validation.constraints.NotNull;

/**
 * 批量关闭优盟dto
 *
 * @author haihu.fhh
 */
public class BatchCloseUnionMemberDto extends OperatorDto {

    private static final long serialVersionUID = -273941844178932099L;

    @NotNull(message = "parentStationId not null")
    private Long parentStationId;

    public Long getParentStationId() {
        return parentStationId;
    }

    public void setParentStationId(Long parentStationId) {
        this.parentStationId = parentStationId;
    }
}
