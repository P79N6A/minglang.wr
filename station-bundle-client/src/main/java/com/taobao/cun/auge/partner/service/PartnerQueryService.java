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
	public PartnerDto queryPartnerByTaobaoUserId(Long taobaoUserId) throws AugeServiceException;
	
	/**
	 * 根据partnerId查询合伙人信息
	 * 
	 * @param partnerId
	 * @return
	 * @throws AugeServiceException
	 */
	public PartnerDto queryPartner(Long partnerId) throws AugeServiceException;
	
}
