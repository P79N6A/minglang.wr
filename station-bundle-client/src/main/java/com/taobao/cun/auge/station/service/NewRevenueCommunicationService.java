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
     * 获取当前邀约的记录
     * @param businessCode
     * @param objectId
     */
    public NewRevenueCommunicationDto getProcessNewRevenueCommunication(String businessCode,String objectId);


    /**
     * 根据ID获取邀约记录
     * @param id
     */
    public NewRevenueCommunicationDto getNewRevenueCommunicationById(Long id);


    /**
     * 获取邀约审批成功的记录(业务保证一种类型只能存在一条审批成功记录)
     * @param businessCode
     * @param objectId
     */
    public NewRevenueCommunicationDto getApprovePassNewRevenueCommunication(String businessCode,String objectId);


    /**
     * 审批邀约记录
     * @param newRevenueCommunicationDto
     */
   public void auditNewRevenueCommunication(NewRevenueCommunicationDto newRevenueCommunicationDto);


}
