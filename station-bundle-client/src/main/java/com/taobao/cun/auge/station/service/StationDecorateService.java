package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.station.dto.StationDecorateAuditDto;
import com.taobao.cun.auge.station.dto.StationDecorateDto;
import com.taobao.cun.auge.station.dto.StationDecorateReflectDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;

/**
 * 服务站装修记录 服务接口
 * @author quanzhu.wangqz
 *
 */
public interface StationDecorateService {

	/**
	 * 装修记录审核
	 * @param stationDecorateAuditDto
	 * @return
	 * @throws AugeServiceException
	 */
	public void audit(StationDecorateAuditDto stationDecorateAuditDto) throws AugeServiceException;
	
	/**
	 * 查询装修记录
	 * @param stationId
	 * @return
	 * @throws AugeServiceException
	 */
	public StationDecorateDto getInfoByTaobaoUserId(Long taobaoUserId) throws AugeServiceException;
	
	/**
	 * 反馈装修记录
	 * @param stationDecorateDto
	 * @throws AugeServiceException
	 */
	public void reflectStationDecorate(StationDecorateReflectDto stationDecorateReflectDto) throws AugeServiceException; 
}
