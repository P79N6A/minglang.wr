package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.station.dto.SyncCainiaoStationDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;

/**
 * 菜鸟交互业务服务接口
 * @author quanzhu.wangqz
 *
 */
public interface CaiNiaoService {
	/**
	 * 新增同步
	 * @param addStationDto
	 * @throws AugeServiceException
	 */
	public void addCainiaoStation(SyncCainiaoStationDto  syncCainiaoStationDto) throws AugeServiceException;
	/**
	 * 修改同步
	 * @param udpateStationDto
	 * @throws AugeServiceException
	 */
	public void updateCainiaoStation(SyncCainiaoStationDto  syncCainiaoStationDto) throws AugeServiceException;
	/**
	 * 删除同步
	 * @param deleteStationDto
	 * @throws AugeServiceException
	 */
	public void deleteCainiaoStation(SyncCainiaoStationDto  syncCainiaoStationDto) throws AugeServiceException;
}
