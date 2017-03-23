package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.station.dto.SyncAddCainiaoStationDto;
import com.taobao.cun.auge.station.dto.SyncDeleteCainiaoStationDto;
import com.taobao.cun.auge.station.dto.SyncModifyBelongTPForTpaDto;
import com.taobao.cun.auge.station.dto.SyncModifyCainiaoStationDto;
import com.taobao.cun.auge.station.dto.SyncTPDegreeCainiaoStationDto;
import com.taobao.cun.auge.station.dto.SyncUpgradeToTPForTpaDto;
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
	public void addCainiaoStation(SyncAddCainiaoStationDto  syncAddCainiaoStationDto) throws AugeServiceException;
	/**
	 * 修改同步
	 * @param udpateStationDto
	 * @throws AugeServiceException
	 */
	public void updateCainiaoStation(SyncModifyCainiaoStationDto  syncModifyCainiaoStationDto) throws AugeServiceException;
	/**
	 * 删除同步
	 * @param deleteStationDto
	 * @throws AugeServiceException
	 */
	public void deleteCainiaoStation(SyncDeleteCainiaoStationDto  syncDeleteCainiaoStationDto) throws AugeServiceException;
	
	
	/**
	 * 删除不可用的菜鸟服务站
	 * @param deleteStationDto
	 * @throws AugeServiceException
	 */
	public void deleteNotUsedCainiaoStation(Long stationId) throws AugeServiceException;
	
	/**
	 * 合伙人降级更新菜鸟station及用户关联关系
	 * @param dddCainiaoStationFutureDto
	 */
	public void updateCainiaoStationFeatureForTPDegree(SyncTPDegreeCainiaoStationDto syncTPDegreeCainiaoStationDto);
	
	/**
	 * 村站解绑合伙人
	 * @param station
	 * @throws AugeServiceException
	 */
	public void unBindAdmin(Long stationId) throws AugeServiceException;
	
	/**
	 * 村站绑定合伙人
	 * @param udpateStationDto
	 * @throws AugeServiceException
	 */
	public void bindAdmin(SyncAddCainiaoStationDto  syncAddCainiaoStationDto) throws AugeServiceException;
	
	/**
	 * 村站修改合伙人
	 * @param udpateStationDto
	 * @throws AugeServiceException
	 */
	public void updateAdmin(SyncAddCainiaoStationDto syncAddCainiaoStationDto) throws AugeServiceException;
	
	/**
	 * 淘帮手更换所属合伙人
	 * @throws AugeServiceException
	 */
	public void updateBelongTPForTpa(SyncModifyBelongTPForTpaDto syncModifyBelongTPForTpaDto) throws AugeServiceException;
	
	/**
	 *淘帮手直升成为合伙人 菜鸟业务服务
	 *@throws AugeServiceException
	 */
	public void upgradeToTPForTpa(SyncUpgradeToTPForTpaDto syncUpgradeToTPForTpaDto) throws AugeServiceException;
}
