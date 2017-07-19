package com.taobao.cun.auge.partner.service;

import com.taobao.cun.auge.station.dto.PartnerDto;

public interface PartnerQueryService {
	
	/**
	 * 根据taobaoUserId查询合伙人信息
	 * 
	 * @param taobaoUserId
	 * @return
	 */
	PartnerDto queryPartnerByTaobaoUserId(Long taobaoUserId);
	
	/**
	 * 根据partnerId查询合伙人信息
	 * 
	 * @param partnerId
	 * @return
	 */
	PartnerDto queryPartner(Long partnerId);

	/**
	 * 根据stationId查询当前站点的合伙人信息
	 *
	 * @param stationId
	 * @return
	 */
	PartnerDto queryPartnerByStationId(Long stationId);
	
}
