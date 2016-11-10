package com.taobao.cun.auge.event;

import com.taobao.cun.auge.common.OperatorDto;

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
