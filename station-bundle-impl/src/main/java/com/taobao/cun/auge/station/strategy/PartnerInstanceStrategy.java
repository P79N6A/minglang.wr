package com.taobao.cun.auge.station.strategy;

import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.station.dto.PartnerInstanceDeleteDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceQuitDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceSettleSuccessDto;
import com.taobao.cun.auge.station.dto.QuitStationApplyDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.ProcessApproveResultEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;

public interface PartnerInstanceStrategy {
	
	/**
	 * 申请入驻
	 * @param partnerInstanceDto
	 * @throws AugeServiceException
	 */
	public void applySettle(PartnerInstanceDto partnerInstanceDto)throws AugeServiceException;
	
	public void applySettleNewly(PartnerInstanceDto partnerInstanceDto)throws AugeServiceException;
	
	/**
	 * 入驻成功
	 * @param settleSuccessDto
	 * @throws AugeServiceException
	 */
	public void settleSuccess(PartnerInstanceSettleSuccessDto settleSuccessDto,PartnerStationRel rel) throws AugeServiceException;
	
	/**
	 * 申请退出
	 * @param quitDto
	 * @param typeEnum
	 * @throws AugeServiceException
	 */
	public void applyQuit(QuitStationApplyDto quitDto, PartnerInstanceTypeEnum typeEnum) throws AugeServiceException;
	
	/**
	 * 退出审批同意
	 * 
	 * @param partnerInstanceId
	 * @throws AugeServiceException
	 */
	public void handleDifferQuitAuditPass(Long partnerInstanceId) throws AugeServiceException;
	
	/**
	 * 退出成功
	 * @param partnerInstanceQuitDto
	 * @throws AugeServiceException
	 */
	public void quit(PartnerInstanceQuitDto partnerInstanceQuitDto) throws AugeServiceException;
	
	/**
	 * 删除
	 * @param partnerInstanceDeleteDto
	 * @param rel
	 * @throws AugeServiceException
	 */
	public void delete(PartnerInstanceDeleteDto partnerInstanceDeleteDto,PartnerStationRel rel) throws AugeServiceException;
	
	/**
	 * 校验是否还有子成员，例如，校验合伙人下面是否存在淘帮手
	 * 
	 * 退出时，校验
	 * 
	 * @param instanceId
	 * @throws AugeServiceException
	 */
	public void validateExistChildrenForQuit(Long instanceId) throws AugeServiceException;
	
	/**
	 * 校验是否还有子成员，例如，校验合伙人下面是否存在淘帮手
	 * 
	 * 停业时，校验
	 * 
	 * @param instanceId
	 * @throws AugeServiceException
	 */
	public void validateExistChildrenForClose(Long instanceId) throws AugeServiceException;

	public Boolean validateUpdateSettle(Long instanceId) throws AugeServiceException;
}
