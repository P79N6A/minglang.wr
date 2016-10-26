package com.taobao.cun.auge.partner.service;

import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;

public interface PartnerQueryService {
	
	/**
	 * 根据taobaoUserId查询合伙人信息
	 * 
	 * @param taobaoUserId
	 * @return
	 * @throws AugeServiceException
	 */
	PartnerDto queryPartnerByTaobaoUserId(Long taobaoUserId) throws AugeServiceException;
	
	/**
	 * 根据partnerId查询合伙人信息
	 * 
	 * @param partnerId
	 * @return
	 * @throws AugeServiceException
	 */
	PartnerDto queryPartner(Long partnerId) throws AugeServiceException;

	/**
	 * 根据stationId查询当前站点的合伙人信息
	 *
	 * @param stationId
	 * @return
	 * @throws AugeServiceException
	 */
	PartnerDto queryPartnerByStationId(Long stationId) throws AugeServiceException;
	
}
