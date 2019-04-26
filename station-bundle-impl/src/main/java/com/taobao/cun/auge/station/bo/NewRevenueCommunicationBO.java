package com.taobao.cun.auge.station.bo;

import com.taobao.cun.auge.dal.domain.NewRevenueCommunication;
import com.taobao.cun.auge.station.condition.NewRevenueCommunicationCondition;
import com.taobao.cun.auge.station.dto.NewRevenueCommunicationDto;

import java.util.List;


public interface NewRevenueCommunicationBO {



    /**
     * 根据id查询沟通记录
     *
     * @param id
     * @return
     * @
     */
    public NewRevenueCommunication getNewRevenueCommunicationById(Long id) ;


    /**
     * 新增邀约记录
     * @param newRevenueCommunicationDto
     * @return 主键
     * @
     */
    public Long addNewRevenueCommunication(NewRevenueCommunicationDto newRevenueCommunicationDto) ;

    /**
     * 修改邀约记录
     * @param newRevenueCommunicationDto
     * @return
     * @
     */
    public void updateNewRevenueCommunication(NewRevenueCommunicationDto newRevenueCommunicationDto) ;


    /**
     * 查询邀约记录
     * @param newRevenueCommunicationCondition
     * @return
     * @
     */

    public List<NewRevenueCommunication> getNewRevenueCommunicationDtoByCondition(NewRevenueCommunicationCondition newRevenueCommunicationCondition);


    /**
     * 完成邀约记录
     * @param newRevenueCommunicationDto
     * @return
     * @
     */
    public void completeNewRevenueCommunication(NewRevenueCommunicationDto newRevenueCommunicationDto);



    /**
     * 审批邀约记录
     * @param newRevenueCommunicationDto
     * @return
     * @
     */
    public void auditNewRevenueCommunication(NewRevenueCommunicationDto newRevenueCommunicationDto);

}
