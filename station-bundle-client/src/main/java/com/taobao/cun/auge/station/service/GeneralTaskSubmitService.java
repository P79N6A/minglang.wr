package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PaymentAccountDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.ProcessBusinessEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;

public interface GeneralTaskSubmitService {
	/**
	 * 保证金冻结完成后，入驻流程最后的系统处理环节
	 * 
	 * @param instance
	 * @throws AugeServiceException
	 */
	public void submitSettlingSysProcessTasks(PartnerInstanceDto instance, String operatorId) throws AugeServiceException;

	/**
	 * 更新菜鸟站点
	 * 
	 * @param instanceId
	 * @param operatorId
	 * @throws AugeServiceException
	 */
	public void submitUpdateCainiaoStation(Long instanceId, String operatorId) throws AugeServiceException;

	/**
	 * 合伙人降级
	 * 
	 * @param instanceDto
	 * @param operatorId
	 * @throws AugeServiceException
	 */
	public void submitDegradePartner(PartnerInstanceDto instanceDto, PartnerInstanceDto parentInstanceDto, OperatorDto operatorDto)
			throws AugeServiceException;

	/**
	 * 提交流程
	 * 
	 * @param business
	 * @param businessId
	 * @param stateChangeEvent
	 */
	public void submitApproveProcessTask(ProcessBusinessEnum business, Long businessId, OperatorDto operatorDto, String remark);

	/**
	 * 发短信
	 * 
	 * @param taobaoUserId
	 * @param mobile
	 * @param operatorId
	 * @param content
	 */
	public void submitSmsTask(Long taobaoUserId, String mobile, String operatorId, String content);


	/**
	 * 用户去标
	 * 
	 * @param taobaoUserId
	 * @param taobaoNick
	 * @param partnerType
	 * @param operatorId
	 */
	public void submitRemoveUserTagTasks(Long taobaoUserId, String taobaoNick, PartnerInstanceTypeEnum partnerType, String operatorId);

	/**
	 * 解冻和正式退出任务
	 * 
	 * @param instanceId
	 * @param operatorDto
	 * @param frozenMoney
	 * @param accountDto
	 * @return
	 */
	public void submitQuitTask(Long instanceId, PaymentAccountDto accountDto, String frozenMoney, OperatorDto operatorDto);

	/**
	 * 退出审批通过后去支付标和物流站点等任务
	 * 
	 * @param instanceId
	 * @param taobaoUserId
	 * @param accountNo
	 * @param operator
	 */
	public void submitQuitApprovedTask(Long instanceId, Long taobaoUserId, String accountNo, String operator);

}
