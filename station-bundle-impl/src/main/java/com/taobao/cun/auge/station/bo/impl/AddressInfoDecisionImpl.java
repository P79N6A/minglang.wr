package com.taobao.cun.auge.station.bo.impl;

import com.taobao.cun.auge.dal.domain.AddressInfoDecision;
import com.taobao.cun.auge.dal.domain.AddressInfoDecisionExample;
import com.taobao.cun.auge.dal.mapper.AddressInfoDecisionMapper;
import com.taobao.cun.auge.station.bo.AddressInfoDecisionBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @author: Shawn Yu
 * @Date: 2018-10-23 12:41
 */
@Component("addressInfoDecisionBO")
public class AddressInfoDecisionImpl implements AddressInfoDecisionBO {

    @Autowired
    private AddressInfoDecisionMapper  addressInfoDecisionMapper;

    @Override
    public void updateByExampleSelective(AddressInfoDecision addressInfoDecision, AddressInfoDecisionExample addressInfoDecisionExample) {
        addressInfoDecisionMapper.updateByExampleSelective(addressInfoDecision,addressInfoDecisionExample);
    }
}
