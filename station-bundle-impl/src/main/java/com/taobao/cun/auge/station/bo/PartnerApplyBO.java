package com.taobao.cun.auge.station.bo;

import com.taobao.cun.auge.dal.domain.PartnerApply;
import com.taobao.cun.auge.station.dto.PartnerApplyDto;

/**
 * 招募表查询
 * Created by xiao on 16/8/29.
 */
public interface PartnerApplyBO {

    /**
     * 将招募状态从已合作变成面试通过
     * @param partnerApplyDto
     */
    public void restartPartnerApplyByUserId(PartnerApplyDto partnerApplyDto);

    public PartnerApply getPartnerApplyByUserId(Long taobaoUserId);

}
