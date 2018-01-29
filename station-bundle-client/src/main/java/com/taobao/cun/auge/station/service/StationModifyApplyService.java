package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.station.dto.StationModifyApplyDto;
import com.taobao.cun.auge.station.enums.StationModifyApplyBusitypeEnum;
import com.taobao.cun.auge.station.enums.StationModifyApplyStatusEnum;

public interface StationModifyApplyService {

	/**
	 * 新增
	 */
	public Long addStationModifyApply(StationModifyApplyDto dto);

	/**
	 * 查询
	 * 
	 * @param type
	 * @param stationId
	 * @return
	 */
	public StationModifyApplyDto getApplyInfoByStationId(
			StationModifyApplyBusitypeEnum type, Long stationId,StationModifyApplyStatusEnum status);

	/**
	 * 查询
	 * 
	 * @return
	 */
	public StationModifyApplyDto getApplyInfoById(Long id);

}
