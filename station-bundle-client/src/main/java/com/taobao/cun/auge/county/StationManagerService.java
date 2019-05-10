package com.taobao.cun.auge.county;

import java.util.List;

import com.taobao.cun.auge.county.dto.CountyDto;
import com.taobao.cun.auge.county.dto.StationManagerDto;

/**
 * 该接口已废弃，请使用{@link com.taobao.cun.auge.cuncounty.service.CuntaoCountyService}
 */
@Deprecated
public interface StationManagerService {

	List<StationManagerDto> getManagersByStationId(Long stationId);
	
	public void saveOrUpdateStationManager(String operator,CountyDto countyDto);

}
