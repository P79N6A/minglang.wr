package com.taobao.cun.auge.station.bo;

public interface PartnerInstanceBO {
	
    /**
     * 根据taobaoUserId 查询合伙人实例表主键id
     *
     * @param taobaoUserId
     * @return
     */
    public Long findPartnerInstanceId(Long taobaoUserId);

    
}
