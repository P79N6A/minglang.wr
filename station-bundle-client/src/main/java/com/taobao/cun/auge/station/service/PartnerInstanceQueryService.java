package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.station.condition.PartnerInstanceCondition;
import com.taobao.cun.auge.station.condition.PartnerInstancePageCondition;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;

public interface PartnerInstanceQueryService {

	/**
	 * 查询合伙人实例信息
	 * 
	 * @param partnerStationId
	 * @return
	 */
	public PartnerInstanceDto queryInfo(PartnerInstanceCondition condition) throws AugeServiceException;

	/**
	 * 
	 * @param pageCondition
	 * @return
	 */
	public PageDto<PartnerInstanceDto> queryByPage(PartnerInstancePageCondition pageCondition);

	/**
	 * 获得状态为活跃[settling,decorating,servicing,closing,closed,quitting(推出待解冻除外)]
	 * 的合伙人实例
	 * 
	 * @param taobaoUserId
	 * @return
	 * @throws AugeServiceException
	 */
	public PartnerInstanceDto getActivePartnerInstance(Long taobaoUserId);

	/**
	 * 根据stationapplyId查询合伙人实例id[过渡阶段使用，即将废弃]
	 * 
	 * @param stationApplyId
	 * @return
	 */
	public Long getPartnerInstanceId(Long stationApplyId);

}
