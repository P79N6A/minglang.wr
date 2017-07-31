package com.taobao.cun.auge.partner.service;

import com.taobao.cun.auge.station.dto.PartnerDto;

public interface PartnerQueryService {
	/**
	 * 根据taobaoUserId查询合伙人信息，核心信息已经脱敏
	 * 
	 * @param taobaoUserId
	 * @return
	 */
	PartnerDto queryPartnerByTaobaoUserId(Long taobaoUserId);

	/**
	 * 根据partnerId查询合伙人信息，核心信息已经脱敏
	 * 
	 * @param partnerId
	 * @return
	 */
	PartnerDto queryPartner(Long partnerId);

	/**
	 * 根据stationId查询当前站点的合伙人信息，核心信息已经脱敏
	 *
	 * @param stationId
	 * @return
	 */
	PartnerDto queryPartnerByStationId(Long stationId);

	/**
	 * 根据taobaoUserId查询合伙人信息，核心信息未脱敏
	 * 
	 * @param taobaoUserId
	 * @return
	 */
	PartnerDto getPartnerByTaobaoUserId(Long taobaoUserId);

	/**
	 * 根据stationId查询当前站点的合伙人信息，核心信息未脱敏
	 * 
	 * @param stationId
	 * @return
	 */
	PartnerDto getPartnerByStationId(Long stationId);
}
