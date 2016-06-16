package com.taobao.cun.auge.station.strategy;

import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.station.dto.PartnerInstanceDeleteDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceQuitDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceSettleSuccessDto;
import com.taobao.cun.auge.station.dto.QuitStationApplyDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.ProcessApproveResultEnum;
import com.taobao.cun.auge.station.enums.ProcessBusinessEnum;
import com.taobao.cun.auge.station.enums.ProcessTypeEnum;
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
	 * 审批退出
	 * @param isAgree
	 * @param partnerInstanceId
	 * @throws AugeServiceException
	 */
	public void auditQuit(ProcessApproveResultEnum approveResult, Long partnerInstanceId) throws AugeServiceException;
	
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
	
	public void validateExistValidChildren(Long instanceId)throws AugeServiceException ;
	
	public ProcessBusinessEnum findProcessBusiness(ProcessTypeEnum processType) throws AugeServiceException;

	public Boolean validateUpdateSettle(Long instanceId)throws AugeServiceException;

	/**
	 * 提交支付宝标任务
	 * 
	 * @param taobaoUserId
	 */
	public void submitRemoveAlipayTagTask(Long taobaoUserId);
}
