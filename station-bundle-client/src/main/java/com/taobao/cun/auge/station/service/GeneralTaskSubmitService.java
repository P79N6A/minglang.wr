package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
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

}
