package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.station.dto.NewRevenueCommunicationDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceTransDto;

public interface NewRevenueCommunicationService {

    /**
     * 提交切换转型邀约记录
     * @param newRevenueCommunicationDto
     */
    public void commitNewRevenueCommunication(NewRevenueCommunicationDto newRevenueCommunicationDto);


    /**
     * 完成邀约记录
     * @param newRevenueCommunicationDto
     */
    public void completeNewRevenueCommunication(NewRevenueCommunicationDto newRevenueCommunicationDto);


    /**
     * 获取当前正在进行中的邀约记录
     * @param businessCode
     * @param objectId
     */
    public NewRevenueCommunicationDto getLastestUnFinishedNewRevenueCommunication(String businessCode,String objectId);


    /**
     * 根据ID获取邀约记录
     * @param id
     */
    public NewRevenueCommunicationDto getNewRevenueCommunicationById(Long id);



}
