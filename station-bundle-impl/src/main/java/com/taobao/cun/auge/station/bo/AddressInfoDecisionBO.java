package com.taobao.cun.auge.station.bo;

import com.taobao.cun.auge.dal.domain.AddressInfoDecision;
import com.taobao.cun.auge.dal.domain.AddressInfoDecisionExample;

/**
 * @Description:
 * @author: Shawn Yu
 * @Date: 2018-10-23 12:39
 */
public interface AddressInfoDecisionBO {

    /**
     * 根据条件更新
     * @param addressInfoDecision
     * @param addressInfoDecisionExample
     */
    public void updateByExampleSelective(AddressInfoDecision addressInfoDecision, AddressInfoDecisionExample addressInfoDecisionExample);

}
