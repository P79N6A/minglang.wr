package com.taobao.cun.auge.event;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.enums.WisdomCountyStateEnum;

/**
 * Created by xiao on 16/10/22.
 */
public class WisdomCountyApplyEvent extends OperatorDto {

    private static final long serialVersionUID = 5100919814119306345L;

    private Long applyId;

    private String remark;

    private String opinion;

    //报名小二
    private String creator;

    //审核还是报名
    private WisdomCountyStateEnum type;

    public WisdomCountyStateEnum getType() {
        return type;
    }

    public void setType(WisdomCountyStateEnum type) {
        this.type = type;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public Long getApplyId() {
        return applyId;
    }

    public void setApplyId(Long applyId) {
        this.applyId = applyId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
