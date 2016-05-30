package com.taobao.cun.auge.station.service;


import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.station.condition.PartnerInstanceCondition;
import com.taobao.cun.auge.station.condition.PartnerInstancePageCondition;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;

public interface PartnerInstanceQueryService {

	/**
	 * 查询合伙人实例信息
	 * 
	 * @param partnerStationId
	 * @return
	 */
	public PartnerInstanceDto queryInfo(PartnerInstanceCondition condition) throws AugeServiceException;	
	

	public PageDto<PartnerInstanceDto> queryByPage(PartnerInstancePageCondition pageCondition);

}
