package com.taobao.cun.auge.station.service;

import java.util.List;

import com.alibaba.cainiao.cuntaonetwork.dto.warehouse.WarehouseDTO;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.dto.CaiNiaoStationRelDto;
import com.taobao.cun.auge.station.dto.SyncAddCainiaoStationDto;
import com.taobao.cun.auge.station.dto.SyncDeleteCainiaoStationDto;
import com.taobao.cun.auge.station.dto.SyncModifyBelongTPForTpaDto;
import com.taobao.cun.auge.station.dto.SyncModifyCainiaoStationDto;
import com.taobao.cun.auge.station.dto.SyncModifyLngLatDto;
import com.taobao.cun.auge.station.dto.SyncTPDegreeCainiaoStationDto;
import com.taobao.cun.auge.station.dto.SyncUpgradeToTPForTpaDto;
import com.taobao.cun.auge.station.enums.StationFeatureOpModeEnum;

/**
 * 菜鸟交互业务服务接口
 * @author quanzhu.wangqz
 *
 */
public interface CaiNiaoService {
	/**
	 * 新增同步
	 * @param addStationDto
	 * @
	 */
	public void addCainiaoStation(SyncAddCainiaoStationDto  syncAddCainiaoStationDto);
	/**
	 * 修改同步
	 * @param udpateStationDto
	 * @
	 */
	public void updateCainiaoStation(SyncModifyCainiaoStationDto  syncModifyCainiaoStationDto);
	/**
	 * 删除同步
	 * @param deleteStationDto
	 * @
	 */
	public void deleteCainiaoStation(SyncDeleteCainiaoStationDto  syncDeleteCainiaoStationDto);
	
	
	/**
	 * 删除不可用的菜鸟服务站
	 * @param deleteStationDto
	 * @
	 */
	public void deleteNotUsedCainiaoStation(Long stationId);
	
	/**
	 * 合伙人降级更新菜鸟station及用户关联关系
	 * @param dddCainiaoStationFutureDto
	 */
	public void updateCainiaoStationFeatureForTPDegree(SyncTPDegreeCainiaoStationDto syncTPDegreeCainiaoStationDto);
	
	/**
	 * 村站解绑合伙人
	 * @param station
	 * @
	 */
	public void unBindAdmin(Long stationId);
	
	/**
	 * 村站绑定合伙人
	 * @param udpateStationDto
	 * @
	 */
	public void bindAdmin(SyncAddCainiaoStationDto  syncAddCainiaoStationDto);
	
	/**
	 * 村站修改合伙人
	 * @param udpateStationDto
	 * @
	 */
	public void updateAdmin(SyncAddCainiaoStationDto syncAddCainiaoStationDto);
	
	/**
	 * 淘帮手更换所属合伙人
	 * @
	 */
	public void updateBelongTPForTpa(SyncModifyBelongTPForTpaDto syncModifyBelongTPForTpaDto);
	
	/**
	 *淘帮手直升成为合伙人 菜鸟业务服务
	 *@
	 */
	public void upgradeToTPForTpa(SyncUpgradeToTPForTpaDto syncUpgradeToTPForTpaDto);
	
	/**
	 * 淘帮手关闭独立物流站
	 * 
	 * @
	 */
	public void closeCainiaoStationForTpa(Long partnerInstanceId, OperatorDto operatorDto);
	
	/**
	 * 停业同步菜鸟
	 * @param syncModifyCainiaoStationDto
	 * @
	 */
	public void closeCainiaoStation(SyncModifyCainiaoStationDto  syncModifyCainiaoStationDto);
	
	/**
	 * 经纬度修改同步
	 * @param SyncModifyLngLatDto
	 * @
	 */
	public void modifyLngLatToCainiao(SyncModifyLngLatDto  syncModifyLngLatDto);
	
	public void updateCainiaoStationFeature(List<Long> cainiaoStationId);
	
	public void synNoWarehouseStationFeature(Long cainiaoStationId,StationFeatureOpModeEnum opMode);
	/**
	 * 菜鸟feature打标
	 * @param stationId
	 * @param featureKey
	 * @param value
	 */
	public void addCainiaoStationFeature(Long stationId,String featureKey,String value);
	
	/**
	 * 菜鸟feature去表
	 * @param stationId
	 * @param featureKey
	 */
	public void removeCainiaoStationFeature(Long stationId,String featureKey);
	
	/**
	 * 根据服务站获取菜鸟站点关系
	 * @param stationId
	 * @return
	 */
	CaiNiaoStationRelDto getCaiNiaoStationRelByStation(Long stationId);
	/**
	 * 根据服务站ID 查询所属菜鸟县仓 是否在 运营中（不是虚拟县仓）
	 * @param stationId
	 * @return
	 */
	public Boolean checkCainiaoCountyIsOperating(Long stationId);
	
	public Boolean queryWarehouseStatusByCainiaoCountyId(Long cnCountyId);
}
