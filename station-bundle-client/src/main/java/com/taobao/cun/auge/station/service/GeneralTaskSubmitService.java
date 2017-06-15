package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.dto.ApproveProcessTask;
import com.taobao.cun.auge.station.dto.BatchMailDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelProcessDto;
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
	 * @param processTask
	 */
	public void submitApproveProcessTask(ApproveProcessTask processTask);

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
	 * 发送邮件
	 * @param mailAddresses
	 * @param operatorId
	 * @param content
	 */
	void submitMailTask(BatchMailDto batchMailDto);
	
	/**
	 * 打标
	 * @param instanceId
	 * @param operator
	 */
	public void submitAddUserTagTasks(Long instanceId,String operator);

	/**
	 * 用户去标
	 * 
	 * @param taobaoUserId
	 * @param taobaoNick
	 * @param partnerType
	 * @param operatorId
	 */
	public void submitRemoveUserTagTasks(Long taobaoUserId, String taobaoNick, PartnerInstanceTypeEnum partnerType, String operatorId,Long instanceId);

	/**
	 * 解冻和正式退出任务
	 * 
	 * @param instanceId
	 * @param operatorDto
	 * @param frozenMoney
	 * @param accountDto
	 * @return
	 */
	public void submitQuitTask(Long instanceId, String accountNo, String frozenMoney, OperatorDto operatorDto);
	
	/**
	 * 提交解冻任务
	 * 
	 * @param instanceId
	 * @param accountNo
	 * @param frozenMoney
	 * @param operatorDto
	 */
	public void submitThawMoneyTask(Long instanceId, String accountNo, String frozenMoney, OperatorDto operatorDto);

	/**
	 * 退出审批通过后去支付标和物流站点等任务
	 * 
	 * @param instanceId
	 * @param stationId
	 * @param taobaoUserId
	 * @param isQuitStation
	 */
	public void submitQuitApprovedTask(Long instanceId,Long stationId, Long taobaoUserId, String isQuitStation);
	
	/**
	 * 撤点审批通过后，删除物流站
	 * 
	 * @param stationId
	 * @param operator
	 */
	public void submitShutdownApprovedTask(Long stationId);

	/**
	 * 已停业村点回到服务中,uic、旺旺打标
	 * @param instanceId
	 * @param taobaoUserId
	 * @param taobaoNick
	 * @param partnerType
	 * @param operator
	 */
	public void submitCloseToServiceTask(Long instanceId, Long taobaoUserId, PartnerInstanceTypeEnum partnerType, String operator);

	
	/**
	 * 启动合伙人层级晋升流程
	 * 
	 * @param business
	 * @param levelProcessDto
	 */
	public void submitLevelApproveProcessTask(ProcessBusinessEnum business, PartnerInstanceLevelProcessDto levelProcessDto);

	/**
	 * 启动激励方案审批流程
	 */
	public void submitIncentiveProgramAuditTask(ApproveProcessTask processTask);
	
	/**
	 * 停业同步菜鸟
	 * 
	 * @param instanceId
	 * @param operatorId
	 * @throws AugeServiceException
	 */
	public void submitClosedCainiaoStation(Long instanceId, String operatorId) throws AugeServiceException;

}
