package com.taobao.cun.auge.station.dto;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.enums.WisdomCountyStateEnum;

import java.io.Serializable;

/**
 * Created by xiao on 16/10/21.
 */
public class WisdomCountyApplyAuditDto extends OperatorDto implements Serializable {

    private static final long serialVersionUID = -8544217385961062645L;

    private Long id;

    private WisdomCountyStateEnum state;

    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WisdomCountyStateEnum getState() {
        return state;
    }

    public void setState(WisdomCountyStateEnum state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
