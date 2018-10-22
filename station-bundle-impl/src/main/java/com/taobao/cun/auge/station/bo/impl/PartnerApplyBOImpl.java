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
import com.taobao.cun.recruit.partner.enums.AddressInfoDecisionStatusEnum;
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
       /*  PartnerApply partnerApply = getPartnerApplyByUserId(partnerApplyDto.getTaobaoUserId());
        if (partnerApply != null){
         partnerApply.setState(partnerApplyDto.getState().getCode());
            PartnerApplyExample example = new PartnerApplyExample();
            Criteria criteria = example.createCriteria();
            criteria.andTaobaoUserIdEqualTo(partnerApplyDto.getTaobaoUserId());
            criteria.andIsDeletedEqualTo("n");
            criteria.andStateEqualTo(PartnerApplyStateEnum.STATE_APPLY_COOPERATION.getCode());
            DomainUtils.beforeUpdate(partnerApply, partnerApplyDto.getOperator());
            partnerApplyMapper.updateByExampleSelective(partnerApply, example);*/

            PartnerApplyExample example = new PartnerApplyExample();
            Criteria criteria = example.createCriteria();
            criteria.andTaobaoUserIdEqualTo(partnerApplyDto.getTaobaoUserId());
            criteria.andIsDeletedEqualTo("n");
            List<PartnerApply> partnerApplyList = partnerApplyMapper.selectByExample(example);
            if(CollectionUtils.isNotEmpty(partnerApplyList)){
                partnerApplyList.forEach(apply ->{
                    DomainUtils.beforeUpdate(apply, partnerApplyDto.getOperator());
                    AddressInfoDecision addressInfoDecision = new AddressInfoDecision();
                    AddressInfoDecisionExample addressExample = new AddressInfoDecisionExample();
                    addressExample.createCriteria().andIsDeletedEqualTo("n").andPartnerApplyIdEqualTo(apply.getId());
                    if(PartnerApplyStateEnum.STATE_ADDRESS_AUDIT_PASS.getCode().equals(apply.getState())){
                        //1.将存在选址审核已通过状态的修改为选址审核不通过状态`
                        apply.setState(PartnerApplyStateEnum.STATE_ADDRESS_AUDIT_NOT_PASS.getCode());
                        apply.setAuditOpinion(PartnerApplyStateEnum.STATE_ADDRESS_AUDIT_NOT_PASS.getDesc());
                        //并将地址信息决策表(address_info_decision)中的status改为auditNotPass（选址未通过）
                        addressInfoDecision.setStatus(AddressInfoDecisionStatusEnum.AUDIT_NOT_PASS.getCode());
                        addressInfoDecision.setMemo(AddressInfoDecisionStatusEnum.AUDIT_NOT_PASS.getDesc());
                    }else if(PartnerApplyStateEnum.STATE_APPLY_COOPERATION.getCode().equals(apply.getState())){
                        //2.将存在已合作状态的修改为待面试状态
                        apply.setState(PartnerApplyStateEnum.STATE_APPLY_INTERVIEW.getCode());
                        apply.setAuditOpinion(PartnerApplyStateEnum.STATE_APPLY_INTERVIEW.getDesc());
                        //并将地址信息决策表(address_info_decision)中记录删除
                        addressInfoDecision.setIsDeleted("y");
                    }
                    partnerApplyMapper.updateByPrimaryKeySelective(apply);
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
