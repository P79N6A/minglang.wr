package com.taobao.cun.auge.station.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.taobao.cun.attachment.enums.AttachmentBizTypeEnum;
import com.taobao.cun.attachment.service.AttachmentService;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.utils.PageDtoUtil;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.station.bo.ShutDownStationApplyBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.condition.StationCondition;
import com.taobao.cun.auge.station.convert.StationConverter;
import com.taobao.cun.auge.station.dto.ShutDownStationApplyDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.service.StationQueryService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("stationQueryService")
@HSFProvider(serviceInterface = StationQueryService.class)
public class StationQueryServiceImpl implements StationQueryService {
	
	@Autowired
	StationBO stationBO;

    @Autowired
    AttachmentService criusAttachmentService;
	
	@Autowired
	ShutDownStationApplyBO shutDownStationApplyBO;
	
	@Override
	public StationDto getStation(Long stationId){
		ValidateUtils.notNull(stationId);
		Station station = stationBO.getStationById(stationId);
		return StationConverter.toStationDto(station);
	}

	@Override
	public StationDto queryStationInfo(StationCondition stationCondition)
		{
		ValidateUtils.validateParam(stationCondition);
		ValidateUtils.notNull(stationCondition.getId());
		
		Station station = stationBO.getStationById(stationCondition.getId());
		StationDto stationDto = StationConverter.toStationDto(station);
		if (stationCondition.getNeedAttachementInfo()) {
			stationDto.setAttachments(criusAttachmentService.getAttachmentList(stationDto.getId(),AttachmentBizTypeEnum.CRIUS_STATION));
		}
		return stationDto;
	}

	@Override
	public List<StationDto> queryStations(List<Long> stationIds){
		if (CollectionUtils.isEmpty(stationIds)) {
			return Collections.<StationDto> emptyList();
		}

		List<Station> stations = stationBO.getStationById(stationIds);
		return StationConverter.toStationDtos(stations);
	}

	@Override
	public List<StationDto> getTpStationsByName(StationCondition stationCondition){
		ValidateUtils.validateParam(stationCondition);
		List<Station> stations = stationBO.getTpStationsByName(stationCondition);
		return stations.stream().map(StationConverter::toStationDto).collect(Collectors.toList());
	}
	
	@Override
	public ShutDownStationApplyDto findShutDownStationApply(Long stationId){
		ValidateUtils.notNull(stationId);
		return shutDownStationApplyBO.findShutDownStationApply(stationId);
	}
	
	@Override
	public ShutDownStationApplyDto findShutDownStationApplyById(Long applyId){
		ValidateUtils.notNull(applyId);
		return shutDownStationApplyBO.findShutDownStationApplyById(applyId);
	}
	
	@Override
	public PageDto<StationDto> queryStations(StationCondition stationCondition){
		ValidateUtils.validateParam(stationCondition);
		Page<Station> page = stationBO.getStations(stationCondition);

		PageDto<StationDto> result = PageDtoUtil.success(page,
				page.stream().map(StationConverter::toStationDto).collect(Collectors.toList()));

		return result;
	}
}
