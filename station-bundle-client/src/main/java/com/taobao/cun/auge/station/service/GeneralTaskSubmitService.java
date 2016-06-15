package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.event.PartnerInstanceStateChangeEvent;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
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
	public void submitDegradePartner(PartnerInstanceDto instanceDto, String operatorId) throws AugeServiceException;
	
	/**
	 * 提交流程
	 * 
	 * @param business
	 * @param stationApplyId
	 * @param stateChangeEvent
	 */
	public void submitApproveProcessTask(ProcessBusinessEnum business, Long stationApplyId,
			PartnerInstanceStateChangeEvent stateChangeEvent);
	
	/**
	 * 发短信
	 * @param taobaoUserId
	 * @param mobile
	 * @param operatorId
	 * @param content
	 */
	public void submitSmsTask(Long taobaoUserId, String mobile, String operatorId, String content);

}
