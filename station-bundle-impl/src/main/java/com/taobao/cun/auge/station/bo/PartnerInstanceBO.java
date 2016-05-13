package com.taobao.cun.auge.station.bo;

import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;

public interface PartnerInstanceBO {
	
    /**
     * 根据taobaoUserId 查询合伙人实例表主键id
     *
     * @param taobaoUserId
     * @return
     */
    public Long findPartnerInstanceId(Long taobaoUserId);
    
    /**
     * 查询子合伙人数量
     * 
     * @param instanceId 父合伙人实例id
     * @param state 子合伙人状态
     * @return
     */
    public int findChildPartners(Long instanceId,PartnerInstanceStateEnum state)throws AugeServiceException ;
    
    
    /**
     * 状态流转
     * 
     * @param instanceId 合伙人实例id
     * @param preState 前置状态
     * @param postState 后置状态
     */
    public void changeState(Long instanceId, PartnerInstanceStateEnum preState, PartnerInstanceStateEnum postState,String operator)throws Exception;

    
}
