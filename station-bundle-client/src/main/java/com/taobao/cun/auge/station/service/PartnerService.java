package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;

public interface PartnerService {
	/**
	 * 按ID更新一个合伙人
	 * @param partnerDto
	 */
	public void updateById(PartnerDto partnerDto);
	
	/**
	 * 根据alilanguserid获取一个合伙人
	 * @param aliLangUserId
	 * @return
	 */
	public PartnerDto getPartnerByAlilangUserId(String aliLangUserId);
	
	public PartnerDto getNormalPartnerByTaobaoUserId(Long taobaoUserId) throws AugeServiceException;
}
