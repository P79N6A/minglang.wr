package com.taobao.cun.auge.station.service.impl;

import com.taobao.cun.auge.station.bo.StationModifyApplyBO;
import com.taobao.cun.auge.station.dto.StationModifyApplyDto;
import com.taobao.cun.auge.station.enums.StationModifyApplyBusitypeEnum;
import com.taobao.cun.auge.station.enums.StationModifyApplyStatusEnum;
import com.taobao.cun.auge.station.service.StationModifyApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("stationModifyApplyService")
public class StationModifyApplyServiceImpl implements StationModifyApplyService {
	
	@Autowired
	private StationModifyApplyBO stationModifyApplyBO;
	@Override
	public Long addStationModifyApply(StationModifyApplyDto dto) {
		return stationModifyApplyBO.addStationModifyApply(dto);
	}

	@Override
	public StationModifyApplyDto getApplyInfoByStationId(
			StationModifyApplyBusitypeEnum type, Long stationId,
			StationModifyApplyStatusEnum status) {
		return stationModifyApplyBO.getApplyInfoByStationId(type, stationId, status);
	}

	@Override
	public StationModifyApplyDto getApplyInfoById(Long id) {
		return stationModifyApplyBO.getApplyInfoById(id);
	}

}
