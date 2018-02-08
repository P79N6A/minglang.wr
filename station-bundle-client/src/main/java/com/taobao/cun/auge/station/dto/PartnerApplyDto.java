package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.enums.PartnerApplyStateEnum;

/**
 * Created by xiao on 16/8/29.
 */
public class PartnerApplyDto extends OperatorDto implements Serializable{

    private Long taobaoUserId;

    private PartnerApplyStateEnum state;

    public Long getTaobaoUserId() {
        return taobaoUserId;
    }

    public void setTaobaoUserId(Long taobaoUserId) {
        this.taobaoUserId = taobaoUserId;
    }

    public PartnerApplyStateEnum getState() {
        return state;
    }

    public void setState(PartnerApplyStateEnum state) {
        this.state = state;
    }
}
