package com.taobao.cun.auge.station.bo.impl;

import com.alibaba.common.lang.StringUtil;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.NewRevenueCommunication;
import com.taobao.cun.auge.dal.domain.NewRevenueCommunicationExample;
import com.taobao.cun.auge.dal.mapper.NewRevenueCommunicationMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.bo.NewRevenueCommunicationBO;
import com.taobao.cun.auge.station.condition.NewRevenueCommunicationCondition;
import com.taobao.cun.auge.station.convert.NewRevenueCommunicationConverter;
import com.taobao.cun.auge.station.dto.NewRevenueCommunicationDto;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Component("newRevenueCommunicationBO")
public class NewRevenueCommunicationBOImpl implements NewRevenueCommunicationBO {

    private static final Logger logger = LoggerFactory.getLogger(NewRevenueCommunicationBO.class);

    @Autowired
    NewRevenueCommunicationMapper newRevenueCommunicationMapper;


    @Override
    public NewRevenueCommunication getNewRevenueCommunicationById(Long id) {
        ValidateUtils.notNull(id);
        return newRevenueCommunicationMapper.selectByPrimaryKey(id);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public Long addNewRevenueCommunication(NewRevenueCommunicationDto newRevenueCommunicationDto) {
        ValidateUtils.notNull(newRevenueCommunicationDto);
        NewRevenueCommunication record= NewRevenueCommunicationConverter.toNewRevenueCommunication(newRevenueCommunicationDto);
        Date now = new Date();
        record.setGmtCreate(now);
        record.setGmtModified(now);
        record.setCreator(newRevenueCommunicationDto.getOperator());
        record.setModifier(newRevenueCommunicationDto.getOperator());
        record.setIsDeleted("n");
        record.setStatus("process");
        newRevenueCommunicationMapper.insert(record);
        return record.getId();
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public void updateNewRevenueCommunication(NewRevenueCommunicationDto newRevenueCommunicationDto) {

        ValidateUtils.notNull(newRevenueCommunicationDto);
        ValidateUtils.notNull(newRevenueCommunicationDto.getId());
        NewRevenueCommunication oldRecord = getNewRevenueCommunicationById(newRevenueCommunicationDto.getId());
        if (oldRecord == null) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, "NewRevenueCommunication is null");
        }
        NewRevenueCommunication record = NewRevenueCommunicationConverter.toNewRevenueCommunication(newRevenueCommunicationDto);

        DomainUtils.beforeUpdate(record, newRevenueCommunicationDto.getOperator());
        newRevenueCommunicationMapper.updateByPrimaryKeySelective(record);

    }

    @Override
    public List<NewRevenueCommunication> getNewRevenueCommunicationDtoByCondition(NewRevenueCommunicationCondition newRevenueCommunicationCondition) {
        ValidateUtils.notNull(newRevenueCommunicationCondition);
        NewRevenueCommunicationExample newRevenueCommunicationExample = new NewRevenueCommunicationExample();
        NewRevenueCommunicationExample.Criteria criteria=newRevenueCommunicationExample.createCriteria();
        if(StringUtil.isNotBlank(newRevenueCommunicationCondition.getBusinessCode())){
            criteria.andBusinessCodeEqualTo(newRevenueCommunicationCondition.getBusinessCode());
        }
        if(StringUtil.isNotBlank(newRevenueCommunicationCondition.getObjectId())){
            criteria.andObjectIdEqualTo(newRevenueCommunicationCondition.getObjectId());
        }

        if(StringUtil.isNotBlank(newRevenueCommunicationCondition.getStatus())){
            criteria.andStatusEqualTo(newRevenueCommunicationCondition.getStatus());
        }
        return newRevenueCommunicationMapper.selectByExample(newRevenueCommunicationExample);
    }

    @Override
    public void completeNewRevenueCommunication(NewRevenueCommunicationDto newRevenueCommunicationDto) {

        ValidateUtils.notNull(newRevenueCommunicationDto);
        ValidateUtils.notNull(newRevenueCommunicationDto.getId());
        NewRevenueCommunication oldRecord = getNewRevenueCommunicationById(newRevenueCommunicationDto.getId());
        if (oldRecord == null) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, "NewRevenueCommunication is null");
        }
        NewRevenueCommunication record =new NewRevenueCommunication();
        record.setId(oldRecord.getId());
        record.setStatus("finish");
        DomainUtils.beforeUpdate(record,"sys");
        newRevenueCommunicationMapper.updateByPrimaryKeySelective(record);
    }
}
