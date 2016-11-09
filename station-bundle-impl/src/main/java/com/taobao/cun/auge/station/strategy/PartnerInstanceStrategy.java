package com.taobao.cun.auge.station.strategy;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.station.dto.PartnerInstanceDeleteDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceQuitDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceSettleSuccessDto;
import com.taobao.cun.auge.station.dto.QuitStationApplyDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
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
	public void validateExistChildrenForQuit(Long instanceId);
	
	/**
	 * 校验是否还有子成员，例如，校验合伙人下面是否存在淘帮手
	 * 
	 * 停业时，校验
	 * 
	 * @param partnerStationRel
	 * @throws AugeServiceException
	 */
	public void validateClosePreCondition(PartnerStationRel partnerStationRel);

	public Boolean validateUpdateSettle(Long instanceId) throws AugeServiceException;
	
	public void startClosing(Long instanceId, String stationName, OperatorDto operatorDto) throws AugeServiceException;
	
	public void startQuiting(Long instanceId, String stationName, OperatorDto operatorDto) throws AugeServiceException;

	/**
	 * 校验资产是否已经归还
	 * 
	 * @param instanceId
	 */
	public void validateAssetBack(Long instanceId);

	/**
	 * 校验村点上其他人是否都处于退出待解冻，已退出状态
	 * 
	 * @param instanceId
	 */
	public void validateOtherPartnerQuit(Long instanceId);
}
