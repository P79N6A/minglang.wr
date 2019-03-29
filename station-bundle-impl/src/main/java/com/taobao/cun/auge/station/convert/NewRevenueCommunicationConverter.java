package com.taobao.cun.auge.station.convert;

import com.taobao.cun.auge.dal.domain.NewRevenueCommunication;
import com.taobao.cun.auge.station.dto.NewRevenueCommunicationDto;

public class NewRevenueCommunicationConverter {

    public static NewRevenueCommunication toNewRevenueCommunication(NewRevenueCommunicationDto newRevenueCommunicationDto){

        NewRevenueCommunication newRevenueCommunication=new NewRevenueCommunication();
        newRevenueCommunication.setId(newRevenueCommunicationDto.getId());
        newRevenueCommunication.setBusinessCode(newRevenueCommunicationDto.getBusinessCode());
        newRevenueCommunication.setObjectId(newRevenueCommunicationDto.getObjectId());
        newRevenueCommunication.setCommuTime(newRevenueCommunicationDto.getCommuTime());
        newRevenueCommunication.setCommuAddress(newRevenueCommunicationDto.getCommuAddress());
        newRevenueCommunication.setCommuContent(newRevenueCommunicationDto.getCommuContent());
        newRevenueCommunication.setRemark(newRevenueCommunicationDto.getRemark());
        newRevenueCommunication.setStatus(newRevenueCommunicationDto.getStatus());
        newRevenueCommunication.setAuditStatus(newRevenueCommunicationDto.getAuditStatus());
        return newRevenueCommunication;
    }

    public static NewRevenueCommunicationDto toNewRevenueCommunicationDto(NewRevenueCommunication newRevenueCommunication){

        NewRevenueCommunicationDto newRevenueCommunicationDto=new NewRevenueCommunicationDto();
        newRevenueCommunicationDto.setId(newRevenueCommunication.getId());
        newRevenueCommunicationDto.setBusinessCode(newRevenueCommunication.getBusinessCode());
        newRevenueCommunicationDto.setObjectId(newRevenueCommunication.getObjectId());
        newRevenueCommunicationDto.setCommuTime(newRevenueCommunication.getCommuTime());
        newRevenueCommunicationDto.setCommuAddress(newRevenueCommunication.getCommuAddress());
        newRevenueCommunicationDto.setCommuContent(newRevenueCommunication.getCommuContent());
        newRevenueCommunicationDto.setStatus(newRevenueCommunication.getStatus());
        newRevenueCommunicationDto.setAuditStatus(newRevenueCommunication.getAuditStatus());
        newRevenueCommunicationDto.setRemark(newRevenueCommunication.getRemark());
        return newRevenueCommunicationDto;
    }

}
