package com.taobao.cun.auge.conversion;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.station.domain.StationDTO;

@Mapper( componentModel = "spring")
@Component
public interface StationConverter {

	StationDTO toStationDTO(Station station);
}
