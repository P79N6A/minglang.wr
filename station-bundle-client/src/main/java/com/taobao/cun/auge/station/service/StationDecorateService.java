package com.taobao.cun.auge.station.service;

import java.util.List;

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
	
	/**
	 * 返回装修记录
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws AugeServiceException
	 */
	public List<StationDecorateDto> getStationDecorateListForSchedule(int pageNum,int pageSize)throws AugeServiceException; 
	
	/**
	 * 返回装修记录总数
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws AugeServiceException
	 */
	public int getStationDecorateListCountForSchedule();
	
	/**
	 * 更新装修记录
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws AugeServiceException
	 */
	public void updateStationDecorate(StationDecorateDto stationDecorateDto);
	
	
}
