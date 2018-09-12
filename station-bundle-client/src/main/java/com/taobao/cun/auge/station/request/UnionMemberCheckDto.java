package com.taobao.cun.auge.station.request;

import javax.validation.constraints.NotNull;

import com.taobao.cun.auge.common.OperatorDto;

/**
 * 新增之前，校验优盟账号dto
 *
 * @author haihu.fhh
 */
public class UnionMemberCheckDto extends OperatorDto {

    private static final long serialVersionUID = 7609659927919461977L;

    /**
     * 淘宝nick
     */
    @NotNull(message = "taobaoNick not null")
    private String taobaoNick;

    /**
     * 优盟合作店所属村站id
     */
    @NotNull(message = "parentStationId not null")
    private Long parentStationId;

    public String getTaobaoNick() {
        return taobaoNick;
    }

    public void setTaobaoNick(String taobaoNick) {
        this.taobaoNick = taobaoNick;
    }

    public Long getParentStationId() {
        return parentStationId;
    }

    public void setParentStationId(Long parentStationId) {
        this.parentStationId = parentStationId;
    }
}
