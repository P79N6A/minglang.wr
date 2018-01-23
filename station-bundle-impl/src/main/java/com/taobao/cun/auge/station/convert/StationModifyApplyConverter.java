package com.taobao.cun.auge.station.convert;

import java.util.ArrayList;
import java.util.List;

import com.taobao.cun.auge.dal.domain.StationModifyApply;
import com.taobao.cun.auge.station.dto.StationModifyApplyDto;
import com.taobao.cun.auge.station.enums.StationModifyApplyBusitypeEnum;
import com.taobao.cun.auge.station.enums.StationModifyApplyStatusEnum;

public class StationModifyApplyConverter {
	
	public static StationModifyApplyDto toStationModifyApplyDto(StationModifyApply stationModifyApply) {
		if (stationModifyApply == null) {
			return null;
		}

		StationModifyApplyDto StationModifyApplyDto = new StationModifyApplyDto();
		
		StationModifyApplyDto.setId(stationModifyApply.getId());
		StationModifyApplyDto.setBusiType(StationModifyApplyBusitypeEnum.valueof(stationModifyApply.getBusiType()));
		StationModifyApplyDto.setInfo(stationModifyApply.getInfo());
		StationModifyApplyDto.setStationId(stationModifyApply.getStationId());
		StationModifyApplyDto.setStatus(StationModifyApplyStatusEnum.valueof(stationModifyApply.getStatus()));
		return StationModifyApplyDto;
	}

	public static StationModifyApply toStationModifyApply(StationModifyApplyDto stationModifyApplyDto) {
		if (stationModifyApplyDto == null) {
			return null;
		}

		StationModifyApply stationModifyApply = new StationModifyApply();
		stationModifyApply.setId(stationModifyApplyDto.getId());
		stationModifyApply.setBusiType(stationModifyApplyDto.getBusiType()== null? null:stationModifyApplyDto.getBusiType().getCode());
		stationModifyApply.setInfo(stationModifyApplyDto.getInfo());
		stationModifyApply.setStationId(stationModifyApplyDto.getStationId());
		stationModifyApply.setStatus(stationModifyApplyDto.getStatus()== null? null:stationModifyApplyDto.getStatus().getCode());
		
		return stationModifyApply;
	}

	public static List<StationModifyApplyDto> toStationModifyApplyDtos(List<StationModifyApply> stationModifyApply) {
		if (stationModifyApply == null) {
			return null;
		}

		List<StationModifyApplyDto> list = new ArrayList<StationModifyApplyDto>();
		for (StationModifyApply stationModifyApply_ : stationModifyApply) {
			list.add(toStationModifyApplyDto(stationModifyApply_));
		}

		return list;
	}

	public static List<StationModifyApply> toStationModifyApplys(List<StationModifyApplyDto> stationModifyApply) {
		if (stationModifyApply == null) {
			return null;
		}

		List<StationModifyApply> list = new ArrayList<StationModifyApply>();
		for (StationModifyApplyDto stationModifyApplyDto : stationModifyApply) {
			list.add(toStationModifyApply(stationModifyApplyDto));
		}

		return list;
	}
}
