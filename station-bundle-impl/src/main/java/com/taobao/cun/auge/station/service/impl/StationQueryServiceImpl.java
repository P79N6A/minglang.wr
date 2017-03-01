package com.taobao.cun.auge.station.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.station.bo.AttachementBO;
import com.taobao.cun.auge.station.bo.ShutDownStationApplyBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.condition.StationCondition;
import com.taobao.cun.auge.station.convert.StationConverter;
import com.taobao.cun.auge.station.dto.ShutDownStationApplyDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.AttachementBizTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.service.StationQueryService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("stationQueryService")
@HSFProvider(serviceInterface = StationQueryService.class)
public class StationQueryServiceImpl implements StationQueryService {
	
	@Autowired
	StationBO stationBO;
	
	@Autowired
	AttachementBO attachementBO;
	
	@Autowired
	ShutDownStationApplyBO shutDownStationApplyBO;

	@Override
	public StationDto queryStationInfo(StationCondition stationCondition)
			throws AugeServiceException {
		ValidateUtils.validateParam(stationCondition);
		ValidateUtils.notNull(stationCondition.getId());
		
		Station station = stationBO.getStationById(stationCondition.getId());
		StationDto stationDto = StationConverter.toStationDto(station);
		if (stationCondition.getNeedAttachementInfo()) {
			stationDto.setAttachements(attachementBO.getAttachementList(stationDto.getId(),AttachementBizTypeEnum.CRIUS_STATION));
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

	@Override
	public List<StationDto> getTpStationsByName(StationCondition stationCondition) throws AugeServiceException {
		ValidateUtils.validateParam(stationCondition);
		List<Station> stations = stationBO.getTpStationsByName(stationCondition);
		return stations.stream().map(StationConverter::toStationDto).collect(Collectors.toList());
	}
	
	@Override
	public ShutDownStationApplyDto findShutDownStationApply(Long stationId) throws AugeServiceException{
		ValidateUtils.notNull(stationId);
		return shutDownStationApplyBO.findShutDownStationApply(stationId);
	}
	
	@Override
	public ShutDownStationApplyDto findShutDownStationApplyById(Long applyId) throws AugeServiceException{
		ValidateUtils.notNull(applyId);
		return shutDownStationApplyBO.findShutDownStationApplyById(applyId);
	}
}
