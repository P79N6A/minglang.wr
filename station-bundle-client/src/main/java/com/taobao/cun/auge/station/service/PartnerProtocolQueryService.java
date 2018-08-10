package com.taobao.cun.auge.station.service;

import java.util.List;

import com.taobao.cun.auge.station.dto.PartnerProtocolDto;

public interface PartnerProtocolQueryService {
	
	/**
	 * 查询合伙人已经签署的协议
	 * 
	 * @param partnerInstanceId
	 * @return
	 */
	public List<PartnerProtocolDto> queryPartnerSignedProtocols(Long partnerInstanceId);
	/**
	 * 是否已经签署的项目告知协议
	 * @param taobaoUserId
	 * @return
	 */
	public Boolean querySignedProjectNoticeProtocol(Long taobaoUserId);
	
}
