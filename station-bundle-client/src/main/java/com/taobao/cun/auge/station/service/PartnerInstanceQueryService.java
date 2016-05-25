package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;

public interface PartnerInstanceQueryService {

	/**
	 * 详情，人和村。没有脱敏
	 * 
	 * @param partnerStationId
	 * @return
	 */
	public PartnerInstanceDto queryInfo(Long partnerInstanceId) throws AugeServiceException;
	
	/**
	 * 详情，人和村。已脫敏
	 * 
	 * @param partnerStationId
	 * @return
	 */
	public PartnerInstanceDto querySafedInfo(Long partnerInstanceId) throws AugeServiceException;
	
	
}
