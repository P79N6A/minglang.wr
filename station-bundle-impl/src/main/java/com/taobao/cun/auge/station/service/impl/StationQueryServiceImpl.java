package com.taobao.cun.auge.station.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.station.bo.AttachementBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.condition.StationCondition;
import com.taobao.cun.auge.station.convert.StationConverter;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.AttachementBizTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.service.StationQueryService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@HSFProvider(serviceInterface = StationQueryService.class)
public class StationQueryServiceImpl implements StationQueryService {
	
	@Autowired
	StationBO stationBO;
	
	@Autowired
	AttachementBO attachementBO;

	@Override
	public StationDto queryStationInfo(StationCondition stationCondition)
			throws AugeServiceException {
		ValidateUtils.validateParam(stationCondition);
		ValidateUtils.notNull(stationCondition.getId());
		
		Station station = stationBO.getStationById(stationCondition.getId());
		StationDto stationDto = StationConverter.toStationDto(station);
		if (stationCondition.getNeedAttachementInfo()) {
			stationDto.setAttachements(attachementBO.selectAttachementList(stationDto.getId(),AttachementBizTypeEnum.CRIUS_STATION));
		}
		return stationDto;
	}

	@Override
	public List<StationDto> queryStations(List<Long> stationIds) throws AugeServiceException {
		if (CollectionUtils.isEmpty(stationIds)) {
			return Collections.<StationDto> emptyList();
		}

		List<Station> stations = stationBO.getStationById(stationIds);
		return StationConverter.toStationDtos(stations);
	}
}
