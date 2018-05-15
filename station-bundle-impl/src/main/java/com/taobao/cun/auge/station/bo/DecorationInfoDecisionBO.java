package com.taobao.cun.auge.station.bo;

import com.github.pagehelper.Page;
import com.taobao.cun.auge.dal.domain.DecorationInfoDecision;
import com.taobao.cun.auge.station.condition.DecorationInfoPageCondition;
import com.taobao.cun.auge.station.dto.DecorationInfoDecisionDto;

/**
 * 装修信息决策
 * @author alibaba-54766
 *
 */
public interface DecorationInfoDecisionBO {

    public Long addDecorationInfoDecision(DecorationInfoDecisionDto decorationInfoDto);
    
    public void updateDecorationInfo(DecorationInfoDecisionDto decorationInfoDto);
    
    public Page<DecorationInfoDecision> queryDecorationInfoByCondition(DecorationInfoPageCondition condition);
    
    public DecorationInfoDecision queryDecorationInfoById(Long id);
    
    public DecorationInfoDecision queryDecorationInfoByStationId(Long stationId);
}
