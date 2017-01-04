package com.taobao.cun.auge.station.service;

import java.util.Date;

import com.taobao.cun.auge.station.dto.PartnerDetailDto;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerFlowerNameApplyDto;
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
	
	/**
	 * 提供给icuntao展现合伙人详情信息
	 * @param taobaoUserId
	 * @return
	 */
	public PartnerDetailDto getPartnerDetail(Long taobaoUserId);
	
	/**
	 * 提供给icuntao修改合伙人信息：手机号、邮箱、生日
	 */
	public void modifyPartnerDetail(Long taobaoUserId,String mobile,String email,Date birthday);
	
	/**
	 * 审批花名
	 */
	public void applyFlowName(PartnerFlowerNameApplyDto dto);
		
		
	/**
	 * 获取花名审批状态
	 */
	public PartnerFlowerNameApplyDto getFlowerNameApplyDetail(Long taobaoUserId);	
		
}
