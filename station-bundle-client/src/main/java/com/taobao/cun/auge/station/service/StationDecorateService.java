package com.taobao.cun.auge.station.service;

import java.util.List;
import java.util.Map;

import com.taobao.cun.auge.station.dto.StationDecorateAuditDto;
import com.taobao.cun.auge.station.dto.StationDecorateDto;
import com.taobao.cun.auge.station.dto.StationDecorateReflectDto;
import com.taobao.cun.auge.station.enums.StationDecorateStatusEnum;
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
	 * @param taobaoUserId
	 * @return
	 * @throws AugeServiceException
	 */
	public StationDecorateDto getInfoByTaobaoUserId(Long taobaoUserId) throws AugeServiceException;
	
	/**
	 * 查询装修记录
	 * @param stationId
	 * @return
	 * @throws AugeServiceException
	 */
	public StationDecorateDto getInfoByStationId(Long stationId) throws AugeServiceException;
	
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
	
	
	/**
	 * 定时同步订单状态
	 * @param stationDecorateDto
	 * @throws AugeServiceException
	 */
	public void syncStationDecorateFromTaobao(StationDecorateDto stationDecorateDto)throws AugeServiceException;
	
	/**
	 * 根据stationId 获得装修状态
	 * @param stationIds
	 * @return
	 * @throws AugeServiceException
	 */
	public Map<Long,StationDecorateStatusEnum> getStatusByStationId(List<Long> stationIds) throws AugeServiceException;
	
	
	/**
	 * 只有在装修中，装修审核待反馈的时候，才返回url,否则返回null
	 * @param taobaoUserId
	 * @return
	 * @throws AugeServiceException
	 */
	public String getReflectUrl(Long taobaoUserId) throws AugeServiceException;
	
	/**
	 * 合伙人确认进入装修中
	 */
	public void confirmAcessDecorating(Long id);
	
	/**
	 * 判断装修装状态是否允许合伙人退出
	 */
	public void judgeDecorateQuit(Long stationId);
}
