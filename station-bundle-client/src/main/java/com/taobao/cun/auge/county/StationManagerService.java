package com.taobao.cun.auge.county;

import java.util.List;

import com.taobao.cun.auge.county.dto.StationManagerDto;

/**
 * 从center迁移至auge
 * @author yi.shaoy
 *
 */
public interface StationManagerService {

	List<StationManagerDto> getManagersByStationId(Long stationId);

}
