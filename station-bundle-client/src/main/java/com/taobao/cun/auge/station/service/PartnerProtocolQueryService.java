package com.taobao.cun.auge.station.service;

import java.util.List;

import com.taobao.cun.auge.station.dto.PartnerProtocolDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;

public interface PartnerProtocolQueryService {
	
	/**
	 * 查询合伙人已经签署的协议
	 * 
	 * @param partnerInstanceId
	 * @return
	 */
	public List<PartnerProtocolDto> queryPartnerSignedProtocols(Long partnerInstanceId);
	
}
