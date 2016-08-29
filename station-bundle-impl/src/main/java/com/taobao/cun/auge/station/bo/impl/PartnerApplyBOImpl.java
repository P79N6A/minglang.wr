package com.taobao.cun.auge.station.bo.impl;

import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ResultUtils;
import com.taobao.cun.auge.dal.domain.PartnerApply;
import com.taobao.cun.auge.dal.domain.PartnerApplyExample;
import com.taobao.cun.auge.dal.mapper.PartnerApplyMapper;
import com.taobao.cun.auge.station.bo.PartnerApplyBO;
import com.taobao.cun.auge.dal.domain.PartnerApplyExample.Criteria;
import com.taobao.cun.auge.station.dto.PartnerApplyDto;
import com.taobao.cun.auge.station.enums.PartnerApplyStateEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("partnerApplyBO")
public class PartnerApplyBOImpl implements PartnerApplyBO{

    @Autowired
    PartnerApplyMapper partnerApplyMapper;

    @Override
    public void restartPartnerApplyByUserId(PartnerApplyDto partnerApplyDto) {
        PartnerApplyExample example = new PartnerApplyExample();
        PartnerApply partnerApply = getPartnerApplyByUserId(partnerApplyDto.getTaobaoUserId());
        if (partnerApply != null){
            partnerApply.setState(partnerApplyDto.getState().getCode());
            Criteria criteria = example.createCriteria();
            criteria.andTaobaoUserIdEqualTo(partnerApplyDto.getTaobaoUserId());
            criteria.andIsDeletedEqualTo("n");
            criteria.andStateEqualTo(PartnerApplyStateEnum.STATE_APPLY_COOPERATION.getCode());
            DomainUtils.beforeUpdate(partnerApply, partnerApplyDto.getOperator());
            partnerApplyMapper.updateByExampleSelective(partnerApply, example);
        }
    }

    @Override
    public PartnerApply getPartnerApplyByUserId(Long taobaoUserId) {
        PartnerApplyExample example = new PartnerApplyExample();
        Criteria criteria = example.createCriteria();
        criteria.andTaobaoUserIdEqualTo(taobaoUserId);
        criteria.andIsDeletedEqualTo("n");
        List<PartnerApply> partnerApplyList = partnerApplyMapper.selectByExample(example);
        return ResultUtils.selectOne(partnerApplyList);
    }
}
