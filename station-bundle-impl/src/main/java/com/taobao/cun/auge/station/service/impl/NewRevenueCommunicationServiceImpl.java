package com.taobao.cun.auge.station.service.impl;
import com.taobao.cun.auge.dal.domain.NewRevenueCommunication;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.bo.NewRevenueCommunicationBO;
import com.taobao.cun.auge.station.condition.NewRevenueCommunicationCondition;
import com.taobao.cun.auge.station.convert.NewRevenueCommunicationConverter;
import com.taobao.cun.auge.station.dto.NewRevenueCommunicationDto;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.service.NewRevenueCommunicationService;
import com.taobao.cun.crius.bpm.service.CuntaoWorkFlowService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import com.taobao.util.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;


@Service("newRevenueCommunicationService")
@HSFProvider(serviceInterface = NewRevenueCommunicationService.class)
public class NewRevenueCommunicationServiceImpl implements NewRevenueCommunicationService {

    private static final Logger logger = LoggerFactory.getLogger(NewRevenueCommunicationService.class);

    @Autowired
    NewRevenueCommunicationBO newRevenueCommunicationBO;

    @Override
    public void commitNewRevenueCommunication(NewRevenueCommunicationDto newRevenueCommunicationDto) {

        Assert.notNull(newRevenueCommunicationDto, "newRevenueCommunicationDto not exist");
        Assert.notNull(newRevenueCommunicationDto.getBusinessCode(), "businessCode is null");
        Assert.notNull(newRevenueCommunicationDto.getObjectId(), "objectId is null");
        Assert.notNull(newRevenueCommunicationDto.getCommuTime(), "commuTime is null");
        Assert.notNull(newRevenueCommunicationDto.getCommuAddress(), "commuAddress is null");
        Assert.notNull(newRevenueCommunicationDto.getCommuContent(), "commuContent is null");
        newRevenueCommunicationBO.addNewRevenueCommunication(newRevenueCommunicationDto);
    }

    @Override
    public void completeNewRevenueCommunication(NewRevenueCommunicationDto newRevenueCommunicationDto) {
        Assert.notNull(newRevenueCommunicationDto, "newRevenueCommunicationDto not exist");
        Assert.notNull(newRevenueCommunicationDto.getId(), "newRevenueCommunicationId not exist");
        newRevenueCommunicationBO.completeNewRevenueCommunication(newRevenueCommunicationDto);
    }

    @Override
    public NewRevenueCommunicationDto getLastestUnFinishedNewRevenueCommunication(String businessCode, String objectId) {

        Assert.notNull(businessCode, "businessCode not exist");
        Assert.notNull(objectId, "objectId not exist");
        NewRevenueCommunicationCondition newRevenueCommunicationCondition=new NewRevenueCommunicationCondition();
        newRevenueCommunicationCondition.setBusinessCode(businessCode);
        newRevenueCommunicationCondition.setObjectId(objectId);
        newRevenueCommunicationCondition.setStatus("process");
        List<NewRevenueCommunication>newRevenueCommunicationList= newRevenueCommunicationBO.getNewRevenueCommunicationDtoByCondition(newRevenueCommunicationCondition);

        if(CollectionUtil.isEmpty(newRevenueCommunicationList)){
            return null;
        }

        if(newRevenueCommunicationList.size()==1) {
            return NewRevenueCommunicationConverter.toNewRevenueCommunicationDto(newRevenueCommunicationList.get(0));
        }
        else {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"村点切换转型邀约信息不唯一");
        }
    }

    @Override
    public NewRevenueCommunicationDto getNewRevenueCommunicationById(Long id) {

        Assert.notNull(id, "newRevenueCommunicationDtoId not exist");

        NewRevenueCommunication newRevenueCommunication=newRevenueCommunicationBO.getNewRevenueCommunicationById(id);

        if(newRevenueCommunication==null){
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"无法找到村点切换转型邀约信息");
        }

        return NewRevenueCommunicationConverter.toNewRevenueCommunicationDto(newRevenueCommunication);
    }
}
