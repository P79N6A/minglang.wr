package com.taobao.cun.auge.station.bo.impl;

import java.util.List;

import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ResultUtils;
import com.taobao.cun.auge.dal.domain.AddressInfoDecision;
import com.taobao.cun.auge.dal.domain.AddressInfoDecisionExample;
import com.taobao.cun.auge.dal.domain.PartnerApply;
import com.taobao.cun.auge.dal.domain.PartnerApplyExample;
import com.taobao.cun.auge.dal.domain.PartnerApplyExample.Criteria;
import com.taobao.cun.auge.dal.mapper.AddressInfoDecisionMapper;
import com.taobao.cun.auge.dal.mapper.PartnerApplyMapper;
import com.taobao.cun.auge.station.bo.PartnerApplyBO;
import com.taobao.cun.auge.station.dto.PartnerApplyDto;
import com.taobao.cun.recruit.partner.enums.PartnerApplyStateEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("partnerApplyBO")
public class PartnerApplyBOImpl implements PartnerApplyBO{

    @Autowired
    PartnerApplyMapper partnerApplyMapper;

    @Autowired
    AddressInfoDecisionMapper addressInfoDecisionMapper;

    @Override
    @Transactional
    public void restartPartnerApplyByUserId(PartnerApplyDto partnerApplyDto) {
       //根据taobaoUserId查询所有符合条件的PartnerApply数据
        PartnerApplyExample example = new PartnerApplyExample();
        Criteria criteria = example.createCriteria();
        criteria.andTaobaoUserIdEqualTo(partnerApplyDto.getTaobaoUserId());
        criteria.andIsDeletedEqualTo("n");
        List<PartnerApply> partnerApplyList = partnerApplyMapper.selectByExample(example);
        if(CollectionUtils.isNotEmpty(partnerApplyList)){
            partnerApplyList.forEach(partnerApply -> {
                //退出时，partner_apply表中的state状态全部改为待面试
                partnerApply.setState(PartnerApplyStateEnum.STATE_APPLY_INTERVIEW.getCode());
                partnerApply.setAuditOpinion(PartnerApplyStateEnum.STATE_APPLY_INTERVIEW.getDesc());
                DomainUtils.beforeUpdate(partnerApply, partnerApplyDto.getOperator());
                partnerApplyMapper.updateByPrimaryKeySelective(partnerApply);
                //地址决策表删除记录
                AddressInfoDecision addressInfoDecision = new AddressInfoDecision();
                addressInfoDecision.setIsDeleted("y");
                AddressInfoDecisionExample addressExample = new AddressInfoDecisionExample();
                addressExample.createCriteria().andIsDeletedEqualTo("n").andPartnerApplyIdEqualTo(partnerApply.getId());
                DomainUtils.beforeUpdate(addressInfoDecision, partnerApplyDto.getOperator());
                addressInfoDecisionMapper.updateByExampleSelective(addressInfoDecision,addressExample);
            });
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
