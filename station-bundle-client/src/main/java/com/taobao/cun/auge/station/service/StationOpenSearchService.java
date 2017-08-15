package com.taobao.cun.auge.station.service;

import java.util.List;

import com.taobao.cun.auge.station.dto.StationOpenSearchDto;
import com.taobao.cun.auge.station.dto.StationOpenSearchQueryModel;
import com.taobao.mtop.common.Result;

public interface StationOpenSearchService {
	/**
	 * 根据村点的名称模糊查询村点
	 *
	 * @param stationName 村点名称
	 * @return 村点的列表
	 */
	public Result<List<StationOpenSearchDto>> queryStationsByName(String stationName);

	/**
	 * 根据村点的名称模糊查询村点
	 *
	 * @param userId      用户Id
	 * @param stationName 村点名称
	 * @return 村点的列表
	 */
	public Result<List<StationOpenSearchDto>> queryStationsByName(String userId, String stationName);


	/**
	 * 根据经纬度查询村点
	 *
	 * @param lng 经度
	 * @param lat 纬度
	 * @return 村点的列表
	 */
	public Result<List<StationOpenSearchDto>> queryStationsByLngAndLat(String lng, String lat);

	/**
	 * 根据经纬度查询村点
	 *
	 * @param lng          经度
	 * @param lat          纬度
	 * @param taobaoUserId 查询者的taobaoUserId
	 * @return
	 */
	public Result<List<StationOpenSearchDto>> queryStationsByLngAndLatLimit(String lng, String lat, String taobaoUserId);

	/**
	 * 根据地址code,查询村点信息
	 *
	 * @param stationQueryModel
	 * @return
	 */
	public Result<List<StationOpenSearchDto>> queryStationByAddressCode(StationOpenSearchQueryModel stationQueryModel);


	/**
	 * is exsit station by villageCode
	 * @param villageCode
	 * @return
     */
	public Result<Boolean> queryStationByVillageCode(Long villageCode);

}
