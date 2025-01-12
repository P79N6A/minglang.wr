package com.taobao.cun.auge.station.bo.impl;

import java.util.List;

import com.github.pagehelper.Page;
import com.taobao.cun.attachment.enums.AttachmentBizTypeEnum;
import com.taobao.cun.attachment.service.AttachmentService;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ResultUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.DecorationInfoDecision;
import com.taobao.cun.auge.dal.domain.DecorationInfoDecisionExample;
import com.taobao.cun.auge.dal.domain.DecorationInfoDecisionExample.Criteria;
import com.taobao.cun.auge.dal.mapper.DecorationInfoDecisionMapper;
import com.taobao.cun.auge.station.bo.DecorationInfoDecisionBO;
import com.taobao.cun.auge.station.condition.DecorationInfoPageCondition;
import com.taobao.cun.auge.station.convert.OperatorConverter;
import com.taobao.cun.auge.station.convert.StationDecorateConverter;
import com.taobao.cun.auge.station.dto.DecorationInfoDecisionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author alibaba-54766
 *
 */
@Component("decorationInfoDecisionBO")
public class DecorationInfoDecisionBOImpl implements DecorationInfoDecisionBO{

    @Autowired
    DecorationInfoDecisionMapper decorationInfoDecisionMapper;
    
    @Autowired
    AttachmentService criusAttachmentService;
    
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public Long addDecorationInfoDecision(DecorationInfoDecisionDto decorationInfoDto) {
        validateDecorationInfo(decorationInfoDto);
        DecorationInfoDecision record = StationDecorateConverter.toDecorationInfoDecision(decorationInfoDto);
        DomainUtils.beforeInsert(record, decorationInfoDto.getOperator());
        decorationInfoDecisionMapper.insert(record);
        if(decorationInfoDto.getAttachments() != null){
            criusAttachmentService.addAttachmentBatch(decorationInfoDto.getAttachments(), record.getId(), AttachmentBizTypeEnum.DECORATION_INFO_DECISION, OperatorConverter.convert(decorationInfoDto));
        }
        return record.getId();
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public void updateDecorationInfo(DecorationInfoDecisionDto decorationInfoDto) {
        ValidateUtils.validateParam(decorationInfoDto);
        ValidateUtils.notNull(decorationInfoDto.getId());
        DecorationInfoDecision record = StationDecorateConverter.toDecorationInfoDecision(decorationInfoDto);
        DomainUtils.beforeUpdate(record, decorationInfoDto.getOperator());
        
        if(decorationInfoDto.getAttachments() != null){
            criusAttachmentService.modifyAttachementBatch(decorationInfoDto.getAttachments(), decorationInfoDto.getId(), AttachmentBizTypeEnum.STATION_DECORATE, OperatorConverter.convert(decorationInfoDto));
        }
        decorationInfoDecisionMapper.updateByPrimaryKeySelective(record);
        
    }

    @Override
    public Page<DecorationInfoDecision> queryDecorationInfoByCondition(DecorationInfoPageCondition condition) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DecorationInfoDecision queryDecorationInfoById(Long id) {
        return decorationInfoDecisionMapper.selectByPrimaryKey(id);
    }

    
    private void validateDecorationInfo(DecorationInfoDecisionDto decorateInfoDto){
        ValidateUtils.notNull(decorateInfoDto);
        Long stationId = decorateInfoDto.getStationId();
        ValidateUtils.notNull(stationId);
        ValidateUtils.notNull(decorateInfoDto.getStatus());
    }

    @Override
    public DecorationInfoDecision queryDecorationInfoByStationId(Long stationId) {
        ValidateUtils.notNull(stationId);
        DecorationInfoDecisionExample example = new DecorationInfoDecisionExample();
        Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("n");
        criteria.andStationIdEqualTo(stationId);
        List<DecorationInfoDecision> resList = decorationInfoDecisionMapper.selectByExample(example);
        return ResultUtils.selectOne(resList);
    }

}
